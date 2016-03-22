package com.mystudy.web.cache;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.mystudy.web.cache.config.CacheConfig;
import com.mystudy.web.cache.config.CacheItemConfig;
import com.mystudy.web.cache.config.CacheLevel;
import com.mystudy.web.cache.config.CacheSetting;
import com.mystudy.web.cache.ehcache.EhCacheProvider;
import com.mystudy.web.common.SpringContext;
import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.util.AccessUtil;
import com.mystudy.web.common.util.LogHolder;
import com.mystudy.web.common.util.StringUtil;
import com.mystudy.web.common.util.Wrapper;

public class CacheClient {
	private final Cache[] caches;
	private CacheItemConfig config;
	private static final Map<CacheLevel, CacheProvider> providerMap = new EnumMap<CacheLevel, CacheProvider>(
			CacheLevel.class);
	private static final Map<CacheItemConfig, CacheClient> clientMap = new IdentityHashMap<CacheItemConfig, CacheClient>();

	private static final ThreadPoolExecutor futureExecutor = new ThreadPoolExecutor(
			5, 500, 4, TimeUnit.HOURS, new SynchronousQueue<Runnable>(),
			new CallerRunsPolicy());
	private static final ThreadPoolExecutor voidExecutor = new ThreadPoolExecutor(
			5, 500, 4, TimeUnit.HOURS, new SynchronousQueue<Runnable>());
	private static final ThreadLocal<ReentrantLock> lockLocal = new ThreadLocal<ReentrantLock>() {
		@Override
		protected ReentrantLock initialValue() {
			return new ReentrantLock();
		}
	};

	private CacheClient(final CacheItemConfig config, boolean usingasync,
			float percent) {
		if (config == null || config.getRegion() == null) {
			throw new IllegalArgumentException("error in create cacheclient");
		}
		this.config = config;
		List<CacheSetting> settingList = config.getCacheSettingMap();
		if (settingList != null) {
			List<Cache> cacheList = new ArrayList<Cache>(settingList.size());
			for (CacheSetting setting : settingList) {
				Cache currCache = getCache(setting);
				if (currCache != null) {
					cacheList.add(currCache);
				}
			}
			caches = cacheList.toArray(new Cache[cacheList.size()]);
		} else {
			caches = new Cache[0];
		}
		SyncExecutor executorTmp = new SyncExecutor(create(percent));
		executor = usingasync ? new AsyncExecutor(executorTmp) : executorTmp;
	}

	private Cache getCache(CacheSetting setting) {
		final CacheLevel level = setting.getCacheLevel();
		if (level == null) {
			return null;
		}
		final Map<CacheLevel, CacheProvider> providerMap = CacheClient.providerMap;
		CacheProvider provider = providerMap.get(level);
		if (provider == null) {
			synchronized (providerMap) {
				provider = providerMap.get(level);
				if (provider == null) {
					switch (level) {
					case Level_3:
//						provider = new MongoCacheProvider();
						break;
					case Level_2:
//						provider = new RedisCacheProvider();
						break;
					default:
						provider = new EhCacheProvider();
						break;
					}
					try {
						provider.start();
					} catch (CacheException e) {
						try {
							LogUtil.getCommonLogger().error(
									"error in start", e);
							provider.stop();
						} catch (Exception e1) {
							LogUtil.getCommonLogger().error(
									"error in stop after fail to start", e);
						} finally {
							provider = null;
						}
					}
					if (provider != null) {
						providerMap.put(level, provider);
					} else {
						return null;
					}
				}
			}
		}
		try {
			return provider.buildCache(setting);
		} catch (CacheException e) {
			LogUtil.getCommonLogger().error("error in buildCache", e);
			return null;
		}
	}

	private static volatile CacheConfig cacheconfig = null;

	public static synchronized void setCacheConfig(CacheConfig config) {
		if (config != null && cacheconfig == null) {
			cacheconfig = config;
		}
	}

	public final static CacheClient getInstance(String region, String configName) {
		if (cacheconfig == null) {
			setCacheConfig(SpringContext.getBean(CacheConfig.class));
		}
		CacheItemConfig itemConfig = cacheconfig.getConfig(region, configName);
		if (itemConfig == null)
			throw new CacheException("no cacheitem config:region[" + region
					+ "],configName[" + configName + "]");
		return getInstance(itemConfig);
	}

	public final static CacheClient getInstance(CacheItemConfig config) {
		return getInstance(config, cacheconfig.isUsingasync(),
				cacheconfig.getPercent());
	}

	public final static CacheClient getInstance(CacheItemConfig config,
			boolean usingasync, float percent) {
		final Map<CacheItemConfig, CacheClient> clientMap = CacheClient.clientMap;
		CacheClient instance = clientMap.get(config);
		if (instance == null && config != null) {
			synchronized (clientMap) {
				instance = clientMap.get(config);
				if (instance == null) {
					float itemPercent = config.getPercent();
					instance = new CacheClient(config, usingasync,
							Float.isNaN(itemPercent) ? percent : itemPercent);
					clientMap.put(config, instance);
				}
			}
		}

		return instance;
	}

	private interface Sleeper {
		void wakeup();
	}

	private abstract class AsyncHandler implements Runnable {

		protected final LogHolder.LogProp logProp;// 解决异步线程没有拦截器获取的LogHolder的问题

		protected AsyncHandler() {
			logProp = LogHolder.getLogProp();
		}

		abstract protected void execute() throws Exception;

		@Override
		public final void run() {
			try {
				LogHolder.copyLogProps(logProp);
				subExec();
			} catch (Exception e) {
				LogUtil.getCommonLogger()
						.error("error in asyncHandler", e);
			}
		}

		abstract protected void subExec() throws Exception;
	}

	private abstract class AsyncFutureHandler extends AsyncHandler implements
			Sleeper {
		protected final ReentrantLock lock;
		protected volatile boolean status = false;
		protected volatile Condition condition;
		protected volatile Exception e;

		public AsyncFutureHandler() {
			lock = lockLocal.get();
		}

		@Override
		protected void execute() throws Exception {
			try {
				condition = lock.newCondition();
				futureExecutor.execute(this);
				lock.lock();
				try {
					if (!status) {
						condition.await(config.getAsyncTimeout(),
								TimeUnit.MILLISECONDS);
					}
					// already have result
				} finally {
					// set true whatever
					if (!status) {
						status = true;
					}
					lock.unlock();
				}
			} catch (Exception e1) {
				e = e1;
			}
			if (e != null) {
				throw e;
			}
		}

		@Override
		public void wakeup() {
			if (status) {
				// it is too late to wake up or it has been done
				return;
			}
			lock.lock();
			try {
				if (!status) {
					condition.signalAll();
				}
				status = true;
			} catch (Exception e) {
				// should not happen
				LogUtil.getCommonLogger().error(
						"unexpected error in callback", e);
			} finally {
				lock.unlock();
			}
		}
	}

	public CacheObject get(final String key) {
		final CacheObject obj = new CacheObject();
		obj.setConfig(config);
		obj.setKey(key);
		try {
			execCallBack(new CallBack<Object>(key) {

				@Override
				Object get(Cache cache) {
					return cache.get(key);
				}

				@Override
				boolean callBackAndNeedSave(Object value) {
					obj.setValue(value);
					return value != null;
				}

				@Override
				void reSave(Cache cache, Object value) {
					cache.put(key, value);
				}
			});
		} catch (Exception ex) {
			LogUtil.getCommonLogger().error("error in get", ex);
		}
		return obj;
	}

	private abstract class SetHandler extends AsyncHandler {

		@Override
		protected void execute() throws Exception {
			voidExecutor.execute(this);
		}

	}

	public void set(final String key, final Object value) {
		try {
			execSave(new Save() {
				@Override
				public void set(Cache cache) {
					cache.put(key, value);
				}
			});
		} catch (Exception e) {
			LogUtil.getCommonLogger().error("error in set", e);
		}
	}

	public void setMap(final String key, final String field, final Object value) {
		if (StringUtil.isNotEmpty(key) && value != null) {
			execSave(new Save() {
				@Override
				public void set(Cache cache) {
					cache.setMap(key, field, value);
				}
			});
		}
	}

	public void setMap(final String key, final Map<String, Object> map) {
		if (StringUtil.isNotEmpty(key) && map != null && map.size() > 0) {
			execSave(new Save() {
				@Override
				public void set(Cache cache) {
					cache.setMap(key, map);
				}
			});
		}
	}

	public CacheObject getMap(final String key, final String... fields) {
		final CacheItemConfig config = this.config;
		final CacheObject obj = new CacheObject();
		obj.setConfig(config);
		obj.setKey(key);
		if (config.getConfigKey() != null && key != null) {
			execCallBack(new CallBack<Map<String, Object>>(key) {

				@Override
				Map<String, Object> get(Cache cache) {
					return cache.getMap(key, fields);
				}

				@Override
				boolean callBackAndNeedSave(Map<String, Object> value) {
					obj.setValue(value);
					return value != null;
				}

				@Override
				void reSave(Cache cache, Map<String, Object> value) {
					cache.setMap(key, value);
				}
			});
		}
		return obj;
	}

	/**
	 * <b>warning</b>:type of value is <b>List&lt?&gt</b>
	 * 
	 * @return
	 */
	public CacheObject keys() {
		final CacheItemConfig config = this.config;
		final CacheObject obj = new CacheObject();
		obj.setConfig(config);
		if (config.getConfigKey() != null) {
			execCallBack(new CallBack<List<?>>("keys") {
				@Override
				List<?> get(Cache cache) {
					return cache.keys();
				}

				@Override
				boolean callBackAndNeedSave(List<?> value) {
					obj.setValue(value);
					return false;
				}

				@Override
				void reSave(Cache cache, List<?> value) {

				}
			});
		}
		return obj;
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (StringUtil.isNotEmpty(key)) {
			execSave(new Save() {
				@Override
				public void set(Cache cache) {
					cache.remove(key);
				}
			});
		}
	}
	public long incr(final String key){
		final Wrapper<Long> wrapper = new Wrapper<Long>(0l);
		try {
			execCallBack(new CallBack<Long>(key) {
				@Override
				Long get(Cache cache) {
					if (cache.incrsupport()) {
						return cache.incr(key);
					}
					return null;
				}

				@Override
				boolean callBackAndNeedSave(Long value) {
					wrapper.setT(value);
					return false;
				}

				@Override
				void reSave(Cache cache, Long value) {
				}
			});
		} catch (Exception ex) {
			LogUtil.getCommonLogger().error("error in get", ex);
		}
		return wrapper.getT();
	}

	public CacheItemConfig getConfig() {
		return config;
	}

	public void setConfig(CacheItemConfig config) {
		this.config = config;
	}

	private abstract class CallBack<T> {
		String name;

		CallBack(String name) {
			this.name = name;
		}

		abstract T get(Cache cache);

		abstract boolean callBackAndNeedSave(T value);

		abstract void reSave(Cache cache, T value);

		protected void markErrorForGet(Cache cache, Exception e) {
			LogUtil.getCommonLogger().error(getErrMsgForGet(cache), e);
		}

		protected String getErrMsgForGet(Cache cache) {
			return "error in get " + cache.getSetting().getCacheLevel();
		}

		protected void markErrorForReSave(Cache cache, Exception e) {
			LogUtil.getCommonLogger().error(getErrMsgForReSave(cache), e);
		}

		protected String getErrMsgForReSave(Cache cache) {
			return "error in re-save " + cache.getSetting().getCacheLevel();
		}
	}

	private abstract class Save {
		abstract void set(Cache cache);

		protected void markError(Cache cache, Exception e) {
			LogUtil.getCommonLogger().error(getErrMsg(cache), e);
		}

		protected String getErrMsg(Cache cache) {
			return "error in save " + cache.getSetting().getCacheLevel();
		}
	}

	final Marker create(float percent) {
		return new Marker(percent);
	}

	private static class CostLogger {

		// chars will be faster!
		static final char[] NULL = "NULL;".toCharArray();
		static final char[] NON_NULL = "Non-NULL;".toCharArray();
		static final char[] INIT = "time cost by CacheClient(μs):["
				.toCharArray();
		static final CostLogger donothing = new CostLogger(false);
		long timetmp;
		final StringBuilder sbd;

		public CostLogger(String name, CacheClient cacheClient) {
			CacheItemConfig config = cacheClient.getConfig();
			sbd = new StringBuilder(64).append(INIT).append(config.getRegion())
					.append(' ').append(config.getConfigKey()).append(' ')
					.append(name).append(']');
		}

		private CostLogger(boolean ignore) {
			sbd = null;
		}

		void begin(Cache cache) {
			if (sbd != null && cache != null) {
				timetmp = System.nanoTime();
			}
		}

		void end(Cache cache, Object value) {
			if (sbd != null && cache != null) {
				CacheLevel level = cache.getSetting().getCacheLevel();
				// 10000>>10 = 10000/1024 ≈ 10000/1000
				sbd.append(level).append('[')
						.append((System.nanoTime() - timetmp) >> 10)
						.append(']').append(value == null ? NULL : NON_NULL);
			}
		}

		void dolog() {
			if (sbd != null) {
				LogUtil.getCommonLogger().info(sbd.toString());
			}
		}
	}

	private class Marker {
		final AccessUtil.Access access;

		Marker(float percent) {
			access = AccessUtil.getAccess(percent);
		}

		/**
		 * could not be NULL
		 * 
		 * @param name
		 * 
		 * @return
		 */
		CostLogger getLogger(String name) {
			CostLogger logger = access.access() ? new CostLogger(name,
					CacheClient.this) : CostLogger.donothing;
			return logger;
		}
	}

	interface Executor {

		void set(Save save);

		<T> void get(CallBack<T> callBack);

	}

	private class SyncExecutor implements Executor {
		protected final Marker marker;

		public SyncExecutor(Marker marker) {
			this.marker = marker;
		}

		@Override
		public void set(Save save) {
			final Cache caches[] = CacheClient.this.caches;
			for (int i = 0; i < caches.length; i++) {
				try {
					save.set(caches[i]);
				} catch (Exception e) {
					save.markError(caches[i], e);
				}
			}
		}

		@Override
		public <T> void get(CallBack<T> callBack) {
			get(callBack, null, this.marker);
		}

		<T> void get(CallBack<T> callBack, Sleeper sleeper) {
			get(callBack, sleeper, this.marker);
		}

		final <T> void get(CallBack<T> callBack, Sleeper sleeper, Marker marker) {
			final Cache caches[] = CacheClient.this.caches;
			if (caches.length > 0) {
				Cache cache;
				T value = null;
				int i = 0;
				CostLogger logger = marker.getLogger(callBack.name);
				do {
					cache = caches[i];
					try {
						logger.begin(cache);
						value = callBack.get(cache);
						logger.end(cache, value);
					} catch (Exception e) {
						callBack.markErrorForGet(cache, e);
					}
					if (value != null)
						break;
					i++;
				} while (i < caches.length);
				if (callBack(callBack, value, sleeper)) {
					while (i > 0) {
						i--;
						cache = caches[i];
						try {
							callBack.reSave(cache, value);
						} catch (Exception e) {
							callBack.markErrorForReSave(cache, e);
						}
					}
				}
				logger.dolog();
			}
		}

		<T> boolean callBack(CallBack<T> callBack, T value, Sleeper sleeper) {
			try {
				return callBack.callBackAndNeedSave(value);
			} finally {
				if (sleeper != null) {
					sleeper.wakeup();
				}
			}
		}
	}

	private class AsyncExecutor implements Executor {
		private final SyncExecutor executor;

		AsyncExecutor(SyncExecutor executor) {
			this.executor = executor;
		}

		@Override
		public void set(final Save save) {
			try {
				new SetHandler() {
					@Override
					public void subExec() throws Exception {
						executor.set(save);
					}
				}.execute();
			} catch (Exception e) {
				LogUtil.getCommonLogger().error(
						"error in execute setHandler", e);
			}
		}

		@Override
		public <T> void get(final CallBack<T> callBack) {
			try {
				new AsyncFutureHandler() {
					@Override
					protected void subExec() throws Exception {
						try {
							executor.get(callBack, this);
						} finally {
							wakeup();
						}
					}
				}.execute();
			} catch (Exception e) {
				LogUtil.getCommonLogger().error(
						"error in execute futureHandler", e);
			}
		}
	}

	private final Executor executor;

	private void execSave(Save save) {
		executor.set(save);
	}

	private <T> void execCallBack(CallBack<T> callBack) {
		executor.get(callBack);
	}

	private void close() {
		for (Cache cache : caches) {
			if (cache != null) {
				try {
					cache.destroy();
				} catch (Exception e) {
					LogUtil.getCommonLogger().error("error in close", e);
				}
			}
		}
	}

	public static void destroy(CacheConfig config) {
		if (cacheconfig != null && cacheconfig == config) {
			// it's easy to shutdown for SyncExecutor
			futureExecutor.shutdown();
			voidExecutor.shutdown();
			final Map<CacheItemConfig, CacheClient> clientMap = CacheClient.clientMap;
			synchronized (clientMap) {
				for (CacheClient client : clientMap.values()) {
					try {
						client.close();
					} catch (Exception e) {
						LogUtil.getCommonLogger().error(
								"error in destory", e);
					}
				}
			}
			final Map<CacheLevel, CacheProvider> providerMap = CacheClient.providerMap;
			synchronized (providerMap) {
				for (CacheProvider provider : providerMap.values()) {
					if (provider != null) {
						try {
							provider.stop();
						} catch (Exception e) {
							LogUtil.getCommonLogger().error(
									"error in destory", e);
						}
					}
				}
			}
		}
	}
}
