package com.mystudy.web.common.redis.provider.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisOption;
import com.mystudy.web.common.redis.extend.ExtendJedisCommands;
import com.mystudy.web.common.redis.provider.ClientProvider;
import com.mystudy.web.common.util.AccessUtil;
import redis.clients.jedis.BinaryClient.LIST_POSITION;


public class Parallel extends ClientProvider implements ExtendJedisCommands {
	ParallelClientPool pool;
	Checker checker;

	public Parallel(ClusterConfig config) {
		super(config);
		pool = new ParallelClientPool(config);
		double percent = config.getPercent();
		if (percent > 0) {
			checker = new Logger(percent);
		} else {
			checker = new Checker();
		}
	}

	@Override
	public Boolean exists(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.exists(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void persist(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.persist(info.getCacheKey());
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public String type(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.type(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void expire(String cluster, String key, int seconds) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.expire(info.getCacheKey(), seconds);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public void expireAt(String cluster, String key, long unixTime) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.expireAt(info.getCacheKey(), unixTime);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Long ttl(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return jedis.ttl(info.getCacheKey());
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;
	}

	@Override
	public void del(String cluster, String... keys) {
		for (String key : keys) {
			ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
			Iterable<ParallelJedis> it = pool.borrow(info);
			for (ParallelJedis jedis : it) {
				if (jedis != null) {
					try {
						jedis.del(info.getCacheKey());
					} catch (Exception e) {
						error(jedis, e, key);
					} finally {
						pool.giveback(jedis);
					}
				}
			}
		}

	}

	@Override
	public Set<String> keys(String cluster, String pattern) {
		return null;
	}

	@Override
	public void set(final String cluster, final String key, final Object value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] bs = null;
		byte[] kbs = null;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (bs == null && (bs = object2byte(value)) == null) {
					break;
				}
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					jedis.set(kbs, bs);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Object get(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] kbs = null;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					return checker.check(byte2object(jedis.get(kbs)));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void setex(final String cluster, final String key,
			final int seconds, final Object value) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] bs = null;
		byte[] kbs = null;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (bs == null && (bs = object2byte(value)) == null) {
					break;
				}
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					jedis.setex(kbs, seconds, bs);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
	}

	@Override
	public List<Object> mget(String cluster, String... keys) throws Exception {
		List<Object> list = new ArrayList<Object>(keys.length);
		for (String key : keys) {
			list.add(get(cluster, key));
		}
		return list;
	}

	@Override
	public void mset(String cluster, Map<String, Object> keyvalue)
			throws Exception {
		for (Entry<String, Object> entry : keyvalue.entrySet()) {
			set(cluster, entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void hset(String cluster, String key, String field, Object value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.hset(info.getCacheKey().getBytes(), field.getBytes(),
							object2byte(value));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Object hget(String cluster, String key, String field)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.hget(info.getCacheKey(), field));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void hmset(String cluster, String key, Map<String, Object> hash)
			throws Exception {
		Map<byte[], byte[]> map = null;
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					if (map == null) {
						map = map2byte(hash);
					}
					jedis.hmset(info.getCacheKey().getBytes(), map);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public List<Object> hmget(String cluster, String key, String... fields)
			throws Exception {
		byte[][] fieldbs = null;
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					if (fieldbs == null) {
						fieldbs = new byte[fields.length][];
						for (int i = 0; i < fields.length; i++) {
							fieldbs[i] = fields[i].getBytes();
						}
					}
					return checker.check(byte2object(jedis.hmget(info
							.getCacheKey().getBytes(), fieldbs)));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Boolean hexists(String cluster, String key, String field) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.hexists(info.getCacheKey(),
							field));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void hdel(String cluster, String key, String... field) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.hdel(info.getCacheKey(), field);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Long hlen(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.hlen(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Set<String> hkeys(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.hkeys(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public List<Object> hvals(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2object(jedis.hvals(info
							.getCacheKey().getBytes())));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Map<String, Object> hgetAll(String cluster, String key)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2map(jedis.hgetAll(info
							.getCacheKey().getBytes())));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void rpush(String cluster, String key, Object... value)
			throws Exception {
		byte[][] valbs = null;
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					if (valbs == null) {
						valbs = object2byte(value);
					}
					jedis.rpush(info.getCacheKey().getBytes(), valbs);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public void lpush(String cluster, String key, Object... value)
			throws Exception {
		byte[][] valbs = null;
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					if (valbs == null) {
						valbs = object2byte(value);
					}
					jedis.lpush(info.getCacheKey().getBytes(), valbs);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Long llen(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.llen(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public List<Object> lrange(String cluster, String key, long start, long end)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2object(jedis.lrange(
							key.getBytes(), start, end)));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void ltrim(String cluster, String key, long start, long end) {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.ltrim(info.getCacheKey(), start, end);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Object lindex(String cluster, String key, long index)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker
							.check(jedis.lindex(info.getCacheKey(), index));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void lset(String cluster, String key, long index, Object value)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.lset(info.getCacheKey().getBytes(), index,
							object2byte(value));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Object lpop(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2object(jedis.lpop(info.getCacheKey().getBytes())));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Object rpop(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2object(jedis.rpop(info.getCacheKey().getBytes())));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public void linsert(String cluster, String key, LIST_POSITION where,
			String pivot, Object value) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					jedis.linsert(info.getCacheKey().getBytes(), where,
							pivot.getBytes(), object2byte(value));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public void sadd(String cluster, String key, Object... member)
			throws Exception {
		byte[][] mebbs = null;
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (mebbs == null && (mebbs = object2byte(member)) == null) {
					break;
				}
				try {
					jedis.sadd(info.getCacheKey().getBytes(), mebbs);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}

	}

	@Override
	public Set<Object> smembers(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(byte2set(jedis.smembers(info
							.getCacheKey().getBytes())));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Object spop(String cluster, String key) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.spop(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Long scard(String cluster, String key) {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.scard(info.getCacheKey()));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}

	@Override
	public Boolean sismember(String cluster, String key, Object member)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Read, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				try {
					return checker.check(jedis.sismember(info.getCacheKey()
							.getBytes(), object2byte(member)));
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return null;

	}
	@Override
	public long incr(final String cluster, final String key)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] kbs = null;
		long l = -1;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					l = Math.max(jedis.incr(kbs),l);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l;

	}
	@Override
	public long decr(final String cluster, final String key)
			throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] kbs = null;
		long l = Long.MAX_VALUE;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					l = Math.min(jedis.decr(kbs),l);
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l == Long.MAX_VALUE ? Long.MIN_VALUE : l;
	}
	
	@Override
	public long decr(String cluster, String key, int seconds) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] kbs = null;
		long l = Long.MAX_VALUE;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					l = Math.min(jedis.decr(kbs),l);
					if (seconds > 0) {
						jedis.expire(kbs, seconds);
					}
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l == Long.MAX_VALUE ? Long.MIN_VALUE : l;
		
	}@Override
	public long incr(String cluster, String key, int seconds) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		byte[] kbs = null;
		long l = -1;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (kbs == null) {
					kbs = info.getCacheKey().getBytes();
					// should not be null now
				}
				try {
					l = Math.max(jedis.incr(kbs),l);
					if (seconds > 0) {
						jedis.expire(kbs, seconds);
					}
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l;

	}
	@Override
	public long incrby(String cluster, String key, int seconds, long incrAmount) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		String cacheKey = null;
		long l = incrAmount >= 0 ? -1 : Long.MAX_VALUE;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (cacheKey == null) {
					cacheKey = info.getCacheKey();
					// should not be null now
				}
				try {
					long res = jedis.incrBy(cacheKey,incrAmount);
					// bigger while increase
					l = ((incrAmount >= 0) ^ (res < l)) ? res : l;
					if (seconds > 0) {
						jedis.expire(cacheKey, seconds);
					}
				} catch (Exception e) {
					error(jedis, e, key);
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l == Long.MAX_VALUE ? Long.MIN_VALUE : l;
	}
	@Override
	public long setInitCount(String cluster, String key, int seconds, long initcount) throws Exception {
		ClusterInfo info = getClusterInfo(RedisOption.Write, cluster, key);
		Iterable<ParallelJedis> it = pool.borrow(info);
		String cacheKey = null;
		long l = initcount;
		String countStr = null;
		for (ParallelJedis jedis : it) {
			if (jedis != null) {
				if (cacheKey == null) {
					cacheKey = info.getCacheKey();
					// should not be null now
				}
				if (countStr == null) {
					countStr = String.valueOf(initcount);
				}
				try {
					if (seconds > 0) {
						jedis.setex(cacheKey, seconds, countStr);
					} else {
						jedis.set(cacheKey, countStr);
					}
				} catch (Exception e) {
					error(jedis, e, key);
					l = Long.MIN_VALUE;
				} finally {
					pool.giveback(jedis);
				}
			}
		}
		return l;

	}
	private static class Checker {
		<T> T check(T res) {
			return res;
		}
	}

	private static class Logger extends Checker {

		private AccessUtil.Access access;

		public Logger(double percent) {
			access = AccessUtil.getAccess(percent);
		}

		@Override
		<T> T check(T res) {
			if (access.access()) {
				if (res == null) {
					LogUtil.getCommonLogger().info("parallel-null");
				} else {
					LogUtil.getCommonLogger().info("parallel-notNull");
				}
			}
			return super.check(res);
		}

	}

	@Override
	public void close() {
		pool.close();
	}

	private void error(ParallelJedis ej, Exception e, String key) {
		if (ej != null) {
			ej.unable();
			LogUtil.getCommonLogger().warn(
					"unable:" + ej.getHost() + ":" + ej.getPort());
			LogUtil.getCommonLogger().error(
					"error in using jedis ip:[" + ej.getHost() + "],port:["
							+ ej.getPort() + "] key:[" + key + "] ", e);
		} else {
			LogUtil.getCommonLogger().error(
					"could not get an idle jedis, for ["
							+ e.getClass().getName() + "] "
							+ e.getLocalizedMessage() + "\n"
							+ e.getStackTrace()[0].toString());
		}
	}
}
