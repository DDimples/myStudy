package com.mystudy.web.common.redis.provider;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisOption;
import com.mystudy.web.common.redis.extend.ExtendJedis;
import com.mystudy.web.common.redis.extend.ExtendJedisCommands;
import redis.clients.jedis.BinaryClient.LIST_POSITION;


public class PoolProvider<K extends ExtendJedis> extends ClientProvider
		implements ExtendJedisCommands {

	protected RedisClientPool<K> pool;

	private static final int TRY_TIMES = 2;

	public PoolProvider(ClusterConfig config) {
		super(config);

	}

	@Override
	public Boolean exists(String cluster, String key) throws Exception {
		Boolean exits = false;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				exits = ej.exists(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return exits;
	}

	@Override
	public void persist(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.persist(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public String type(String cluster, String key) throws Exception {
		String typeStr = "";
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.type(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return typeStr;
	}

	@Override
	public void expire(String cluster, String key, int seconds)
			throws Exception {

		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.expire(info.getCacheKey(), seconds);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public void expireAt(String cluster, String key, long unixTime)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.expireAt(info.getCacheKey(), unixTime);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Long ttl(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		Long ttl = 0l;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ttl = ej.ttl(info.getCacheKey());
				/*
				 * In Redis 2.6 or older the command returns -1 if the key does
				 * not exist or if the key exist but has no associated expire.
				 * 
				 * Starting with Redis 2.8 the return value in case of error
				 * changed:
				 * The command returns -2 if the key does not exist.
				 * The command returns -1 if the key exists but has no
				 * associated expire.
				 */
				if (ttl != null && ttl > 0) {
					break;
				}
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return ttl;
	}

	@Override
	public void del(String cluster, String... keys) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, keys);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.del(getKeys(cluster, keys));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Set<String> keys(String cluster, String pattern) throws Exception {
		Set<String> keySet = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, pattern);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				keySet = ej.keys(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return keySet;
	}

	@Override
	public void set(String cluster, String key, Object value) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte [] bs = object2byte(value);
		if (bs != null) {
			do {
				try {
					ej = pool.borrow(info);
					if (ej == null) {
						continue;
					}
					ej.set(info.getCacheKey().getBytes(), bs);
					break;
				} catch (Exception e) {
					error(ej, e, info.getCacheKey(), i);
				} finally {
					if (ej != null) {
						pool.giveback(ej, info);
						ej = null;
					}
				}
			} while (++i < TRY_TIMES);
		}
	}

	@Override
	public Object get(String cluster, String key) throws Exception {
		Object value = null;
		byte[] valuebs;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				byte keybs[] = info.getCacheKey().getBytes();
				valuebs = safeGet(ej, keybs, i);
				value = this.byte2object(valuebs);
//				if (value != null) {
//					if (ValueInfo.class.isInstance(value)) {
//						value = null;
//						continue;
//					}
//					try {
//						byte[] infoKeybs = (info.getCacheKey() + ValueInfo.suf)
//								.getBytes();
//						byte[] infobs = safeGet(ej, infoKeybs, i);
//						if (infobs != null) {
//							Object valueInfo = this.byte2object(infobs);
//							if (ValueInfo.class.isInstance(valueInfo)) {
//								ValueInfo vinfo = (ValueInfo) valueInfo;
//								String valueClassName = value.getClass()
//										.getName();
//								if (vinfo.clzzName.equals(valueClassName)) {
//									if (valuebs.length != vinfo.length) {
//										LogUtil.getApplicationLogger()
//												.error("byte array for ["
//														+ valueClassName
//														+ "] has different length, get ["
//														+ new ValueInfo(info
//																.getCacheKey(),
//																valueClassName,
//																valuebs.length)
//														+ "] expect [" + vinfo
//														+ "] with " + ej + " "
//														+ i);
//										value = null;
//										continue;
//									}
//								} else {
//									LogUtil.getApplicationLogger().error(
//											"classname is not same, get ["
//													+ new ValueInfo(info
//															.getCacheKey(),
//															valueClassName,
//															valuebs.length)
//													+ "] expect [" + vinfo
//													+ "] with " + ej + " " + i);
//									value = null;
//									continue;
//								}
//							} else {
//								if (valuebs.length == infobs.length) {
//
//								}
//								LogUtil.getApplicationLogger().error(
//										"error in cast valueInfo for:"
//												+ getValueInfoDetail(valueInfo)
//												+ " key[" + info.getCacheKey()
//												+ "] " + ej + " " + i);
//								if (valueInfo != null) {
//									value = null;
//									continue;
//								}
//							}
//						} else {
//
//							// ignore
//							LogUtil.getApplicationLogger().warn(
//									"could not get valueInfo for:"
//											+ (value == null ? "null " : (value
//													.getClass() + "@" + value
//													.hashCode())) + " key["
//											+ info.getCacheKey() + "] " + ej
//											+ " " + i);
//							value = null;
//							continue;
//						}
//					} catch (Exception e) {
//						LogUtil.getApplicationLogger().error(
//								"error in get valueInfo  key["
//										+ info.getCacheKey() + "] " + ej + " "
//										+ i, e);
//						value = null;
//						error(ej, e, key, i);
//						continue;
//					}
//				}
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	private static byte[] safeGet(ExtendJedis ej, byte[] keybs, int times) {
		byte[] valuebs = ej.get(keybs);
		if (valuebs != null && valuebs.length > 0) {
			Long len = ej.strlen(keybs);
			if (len != null && len.intValue() == valuebs.length) {
				return valuebs;
			}
			LogUtil.getCommonLogger().error(
					"error in check STRLEN for value, length of value is "
							+ valuebs.length + " while result of STRLEN is "
							+ len + " " + ej + " " + times);
		}
		return null;
	}

//	String getValueInfoDetail(Object info) {
//		if (info == null) {
//			return null;
//		}
//		if (Number.class.isInstance(info)
//				|| CharSequence.class.isInstance(info)
//				|| info.getClass().isPrimitive()) {
//			return String.valueOf(info);
//		}
//		return info.getClass().toString();
//	}

	@Override
	public void setex(String cluster, String key, int seconds, Object value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		byte[] bs = object2byte(value);
		if (bs == null) {
			if (value != null) {
				LogUtil.getCommonLogger().warn(
						"could not serialize value for " + value.getClass());
			}
			return;
		}
//		byte[] infobs = object2byte(new ValueInfo(info.getCacheKey(), value
//				.getClass().getName(), bs.length));
//		if (infobs == null) {
//			// should not happen
//			LogUtil.getApplicationLogger().error(
//					"could not serialize valueinfo");
//		}
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.setex(info.getCacheKey().getBytes(), seconds, bs);
//				if (infobs != null) {
//					ej.setex((info.getCacheKey() + ValueInfo.suf).getBytes(),
//							seconds, infobs);
//				}
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public List<Object> mget(String cluster, String... keys) throws Exception {
		List<Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, keys);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.mget(list2byte(cluster, keys)));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public void mset(String cluster, Map<String, Object> keyvalue)
			throws Exception {
		String[] keys = new String[keyvalue.size()];
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, keyvalue
				.keySet().toArray(keys));
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.mset(list2byte(cluster, keyvalue));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public void hset(String cluster, String key, String field, Object value)
			throws Exception {

		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.hset(info.getCacheKey().getBytes(), field.getBytes(),
						object2byte(value));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Object hget(String cluster, String key, String field)
			throws Exception {

		Object value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.hget(info.getCacheKey().getBytes(),
						field.getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;

	}

	@Override
	public void hmset(String cluster, String key, Map<String, Object> hash)
			throws Exception {
		String[] keys = new String[hash.size()];
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, hash
				.keySet().toArray(keys));
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.hmset(info.getCacheKey().getBytes(), map2byte(hash));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public List<Object> hmget(String cluster, String key, String... fields)
			throws Exception {
		List<Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.hmget(info.getCacheKey().getBytes(),
						string2byte(fields)));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public Boolean hexists(String cluster, String key, String field)
			throws Exception {
		Boolean exist = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				exist = ej.hexists(info.getCacheKey(), field);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return exist;
	}

	@Override
	public void hdel(String cluster, String key, String... field)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.hdel(info.getCacheKey(), field);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);

	}

	@Override
	public Long hlen(String cluster, String key) throws Exception {
		Long len = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				len = ej.hlen(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return len;
	}

	@Override
	public Set<String> hkeys(String cluster, String key) throws Exception {
		Set<String> keySet = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				keySet = ej.hkeys(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return keySet;
	}

	@Override
	public List<Object> hvals(String cluster, String key) throws Exception {
		List<Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.hvals(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public Map<String, Object> hgetAll(String cluster, String key)
			throws Exception {

		Map<String, Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2map(ej.hgetAll(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;

	}

	@Override
	public void rpush(String cluster, String key, Object... value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.rpush(info.getCacheKey().getBytes(), object2byte(value));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public void lpush(String cluster, String key, Object... value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.lpush(info.getCacheKey().getBytes(), object2byte(value));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Long llen(String cluster, String key) throws Exception {
		Long len = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				len = ej.llen(info.getCacheKey());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return len;
	}

	@Override
	public List<Object> lrange(String cluster, String key, long start, long end)
			throws Exception {
		List<Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.lrange(info.getCacheKey().getBytes(),
						start, end));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public void ltrim(String cluster, String key, long start, long end)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.ltrim(info.getCacheKey().getBytes(), start, end);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Object lindex(String cluster, String key, long index)
			throws Exception {
		Object value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.lindex(info.getCacheKey().getBytes(),
						index));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public void lset(String cluster, String key, long index, Object value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.lset(info.getCacheKey().getBytes(), index,
						object2byte(value));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Object lpop(String cluster, String key) throws Exception {
		Object value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.lpop(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public Object rpop(String cluster, String key) throws Exception {
		Object value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.rpop(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public void linsert(String cluster, String key, LIST_POSITION where,
			String pivot, Object value) throws Exception {

		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.linsert(info.getCacheKey().getBytes(), where,
						pivot.getBytes(), object2byte(value));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public void sadd(String cluster, String key, Object... member)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				ej.sadd(info.getCacheKey().getBytes(), object2byte(member));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
	}

	@Override
	public Set<Object> smembers(String cluster, String key) throws Exception {
		Set<Object> value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2set(ej.smembers(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public Object spop(String cluster, String key) throws Exception {
		Object value = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				value = byte2object(ej.spop(info.getCacheKey().getBytes()));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return value;
	}

	@Override
	public Long scard(String cluster, String key) throws Exception {
		Long len = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				len = ej.scard(info.getCacheKey().getBytes());
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return len;
	}

	@Override
	public Boolean sismember(String cluster, String key, Object member)
			throws Exception {
		Boolean exist = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		K ej = null;
		int i = 0;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				exist = ej.sismember(info.getCacheKey().getBytes(),
						object2byte(member));
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return exist;
	}

	private void error(ExtendJedis ej, Exception e, String key, int times)
			throws Exception {
		if (ej != null) {
			ej.unable();
			LogUtil.getCommonLogger().warn("unable " + ej);
			LogUtil.getCommonLogger().error(
					"error in using jedis " + ej + " key:[" + key + "] times:"
							+ times, e);
		} else {
			LogUtil.getCommonLogger().error(
					"could not get an idle jedis, times:" + times + " for ["
							+ e.getClass().getName() + "] "
							+ e.getLocalizedMessage() + "\n"
							+ e.getStackTrace()[0].toString());
		}
	}

//	private static class ValueInfo implements Serializable {
//		/**
//		 * 
//		 */
//		private static final long serialVersionUID = 6256453213302516237L;
//		private static final String suf = "_valueInfo";
//		String clzzName;
//		int length;
//		String key;
//
//		ValueInfo(String key, String clzzName, int length) {
//			this.key = key;
//			this.clzzName = clzzName;
//			this.length = length;
//		}
//
//		@Override
//		public String toString() {
//			return new StringBuilder("key:[").append(this.key)
//					.append("],className:[").append(this.clzzName)
//					.append("],byteArrayLength:[").append(this.length)
//					.append("]").toString();
//		}
//	}

	@Override
	public void close() throws Exception {
		pool.close();
	}

	@Override
	public long incr(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte[] kbs = info.getCacheKey().getBytes();
		long l = -1;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				l = ej.incr(kbs);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return l;
	}
	
	@Override
	public long decr(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte[] kbs = info.getCacheKey().getBytes();
		long l = Long.MIN_VALUE;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				l = ej.decr(kbs);
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return l;
	}

	@Override
	public long decr(String cluster, String key, int seconds) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte[] kbs = info.getCacheKey().getBytes();
		long l = Long.MIN_VALUE;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				l = ej.decr(kbs);
				if (seconds > 0) {
					ej.expire(key, seconds);
				}
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return l;
	}

	@Override
	public long incr(String cluster, String key, int seconds) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte[] kbs = info.getCacheKey().getBytes();
		long l = -1;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				l = ej.incr(kbs);
				if (seconds > 0) {
					ej.expire(key, seconds);
				}
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return l;
	}

	@Override
	public long setInitCount(String cluster, String key, int seconds, long initcount) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		String cacheKey = info.getCacheKey();
		String countStr = String.valueOf(initcount);
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				if (seconds > 0) {
					ej.setex(cacheKey, seconds, countStr);
				} else {
					ej.set(cacheKey, countStr);
				}
				return initcount;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return Integer.MIN_VALUE;
	}
	@Override
	public long incrby(String cluster, String key, int seconds, long incrAmount) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		K ej = null;
		int i = 0;
		byte[] kbs = info.getCacheKey().getBytes();
		long l = -1;
		do {
			try {
				ej = pool.borrow(info);
				if (ej == null) {
					continue;
				}
				l = ej.incrBy(kbs, incrAmount);
				if (seconds > 0) {
					ej.expire(key, seconds);
				}
				break;
			} catch (Exception e) {
				error(ej, e, info.getCacheKey(), i);
			} finally {
				if (ej != null) {
					pool.giveback(ej, info);
					ej = null;
				}
			}
		} while (++i < TRY_TIMES);
		return l;
	}
}
