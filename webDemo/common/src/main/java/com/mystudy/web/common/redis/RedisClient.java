package com.mystudy.web.common.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.RedisConfig;
import com.mystudy.web.common.redis.extend.ExtendJedisCommands;
import com.mystudy.web.common.redis.provider.CommandFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;



public class RedisClient implements ExtendJedisCommands {

	private RedisConfig config;
	private Map<String, ExtendJedisCommands> commandList;

	public RedisClient(RedisConfig config) {
		this.config = config;
		commandList = new HashMap<String, ExtendJedisCommands>();
		for (ClusterConfig clusterConfig : config.getClusterList()) {
			clusterConfig.setClusterList(config.getClusterName());
			commandList.put(clusterConfig.getCluster(),
					CommandFactory.getCommand(clusterConfig));
		}
	}

	public String getSplitTag(String cluster) {
		String clusterName = config.getClusterName(cluster);
		if (clusterName != null) {
			for (ClusterConfig clusterConfig : config.getClusterList()) {
				if (clusterName.equalsIgnoreCase(clusterConfig.getCluster()))
					return clusterConfig.getSplitTag();
			}
		}
		return ":";
	}

	@Override
	public Boolean exists(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).exists(cluster,
				key);
	}

	@Override
	public void persist(String cluster, String key) throws Exception {
		commandList.get(config.getClusterName(cluster)).persist(cluster, key);

	}

	@Override
	public String type(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).type(cluster,
				key);
	}

	@Override
	public void expire(String cluster, String key, int seconds)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).expire(cluster, key,
				seconds);

	}

	@Override
	public void expireAt(String cluster, String key, long unixTime)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).expireAt(cluster, key,
				unixTime);

	}

	@Override
	public Long ttl(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster))
				.ttl(cluster, key);
	}

	@Override
	public void del(String cluster, String... keys) throws Exception {
		commandList.get(config.getClusterName(cluster)).del(cluster, keys);

	}

	@Override
	public Set<String> keys(String cluster, String pattern) throws Exception {
		return commandList.get(config.getClusterName(cluster)).keys(cluster,
				pattern);
	}

	@Override
	public void set(String cluster, String key, Object value) throws Exception {
		commandList.get(config.getClusterName(cluster))
				.set(cluster, key, value);

	}

	@Override
	public Object get(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster))
				.get(cluster, key);
	}

	@Override
	public void setex(String cluster, String key, int seconds, Object value)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).setex(cluster, key,
				seconds, value);

	}

	@Override
	public List<Object> mget(String cluster, String... keys) throws Exception {
		return commandList.get(config.getClusterName(cluster)).mget(cluster,
				keys);
	}

	@Override
	public void mset(String cluster, Map<String, Object> keyvalue)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).mset(cluster, keyvalue);

	}

	@Override
	public void hset(String cluster, String key, String field, Object value)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).hset(cluster, key,
				field, value);

	}

	@Override
	public Object hget(String cluster, String key, String field)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).hget(cluster,
				key, field);
	}

	@Override
	public void hmset(String cluster, String key, Map<String, Object> hash)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).hmset(cluster, key,
				hash);

	}

	@Override
	public List<Object> hmget(String cluster, String key, String... fields)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).hmget(cluster,
				key, fields);
	}

	@Override
	public Boolean hexists(String cluster, String key, String field)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).hexists(cluster,
				key, field);
	}

	@Override
	public void hdel(String cluster, String key, String... field)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).hdel(cluster, key,
				field);
	}

	@Override
	public Long hlen(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).hlen(cluster,
				key);
	}

	@Override
	public Set<String> hkeys(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).hkeys(cluster,
				key);
	}

	@Override
	public List<Object> hvals(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).hvals(cluster,
				key);
	}

	@Override
	public Map<String, Object> hgetAll(String cluster, String key)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).hgetAll(cluster,
				key);
	}

	@Override
	public void rpush(String cluster, String key, Object... value)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).rpush(cluster, key,
				value);

	}

	@Override
	public void lpush(String cluster, String key, Object... value)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).lpush(cluster, key,
				value);

	}

	@Override
	public Long llen(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).llen(cluster,
				key);
	}

	@Override
	public List<Object> lrange(String cluster, String key, long start, long end)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).lrange(cluster,
				key, start, end);
	}

	@Override
	public void ltrim(String cluster, String key, long start, long end)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).ltrim(cluster, key,
				start, end);

	}

	@Override
	public Object lindex(String cluster, String key, long index)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).lindex(cluster,
				key, index);
	}

	@Override
	public void lset(String cluster, String key, long index, Object value)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).lset(cluster, key,
				index, value);

	}

	@Override
	public Object lpop(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).lpop(cluster,
				key);
	}

	@Override
	public Object rpop(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).rpop(cluster,
				key);
	}

	@Override
	public void linsert(String cluster, String key, LIST_POSITION where,
			String pivot, Object value) throws Exception {
		commandList.get(config.getClusterName(cluster)).linsert(cluster, key,
				where, pivot, value);

	}

	@Override
	public void sadd(String cluster, String key, Object... member)
			throws Exception {
		commandList.get(config.getClusterName(cluster)).sadd(cluster, key,
				member);

	}

	@Override
	public Set<Object> smembers(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).smembers(
				cluster, key);
	}

	@Override
	public Object spop(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).spop(cluster,
				key);
	}

	@Override
	public Long scard(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).scard(cluster,
				key);
	}

	@Override
	public Boolean sismember(String cluster, String key, Object member)
			throws Exception {
		return commandList.get(config.getClusterName(cluster)).sismember(
				cluster, key, member);
	}

	@Override
	public void close() throws Exception {
		for (ExtendJedisCommands commands : commandList.values()) {
			commands.close();
		}
	}

	@Override
	public long incr(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).incr(
				cluster, key);
	}

	@Override
	public long decr(String cluster, String key) throws Exception {
		return commandList.get(config.getClusterName(cluster)).decr(cluster, key);
	}

	@Override
	public long decr(String cluster, String key, int seconds) throws Exception {
		return commandList.get(config.getClusterName(cluster)).decr(cluster, key, seconds);
	}

	@Override
	public long incr(String cluster, String key, int seconds) throws Exception {
		return commandList.get(config.getClusterName(cluster)).incr(cluster, key, seconds);
	}

	@Override
	public long setInitCount(String cluster, String key, int seconds, long initcount) throws Exception {
		return commandList.get(config.getClusterName(cluster)).setInitCount(cluster, key, seconds, initcount);
	}
	@Override
	public long incrby(String cluster, String key, int seconds, long incrAmount) throws Exception {
		return commandList.get(config.getClusterName(cluster)).incrby(cluster, key, seconds, incrAmount);
	}
}
