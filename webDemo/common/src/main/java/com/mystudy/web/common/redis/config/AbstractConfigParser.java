package com.mystudy.web.common.redis.config;

import com.mystudy.web.common.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractConfigParser {
	protected List<RedisConnection> split(String hostList) {
		List<RedisConnection> connectionList = new ArrayList<RedisConnection>();
		String[] connectionConfig = hostList.split("\\s*,\\s*");
		for (String connectionInfo : connectionConfig) {
			if (StringUtil.isNotEmpty(connectionInfo)) {
				RedisConnection connection = parse(connectionInfo);
				if (connection != null) {
					connectionList.add(connection);
				}
			}
		}
		return connectionList;
	}

	protected RedisConnection parse(String hostInfo) {
		RedisConnection connection = null;
		if (StringUtil.isNotBlank(hostInfo)) {
			String[] info = hostInfo.split("\\s*:\\s*");
			if (info.length >= 3) {
				connection = new RedisConnection();
				switch (info.length) {
				// how could the length of an array be a negative number?
				default:
					if (StringUtil.isNotBlank(info[4])) {
						connection.setTimeout(Integer.parseInt(info[4].trim()));
					}
				case 4:
					connection.setName(info[3].trim());
				case 3:
					if (StringUtil.isNoneBlank(info[2], info[1], info[0])) {
						connection.setOption(RedisOption.values()[Integer
								.parseInt(info[2].trim())]);
						connection.setPort(Integer.valueOf(info[1].trim()));
						connection.setHost(info[0].trim());
					}
					break;
				case 2:
				case 1:
				case 0:
					// could not happen
					connection = null;
					break;
				}
			}
		}
		return connection;
	}

	protected Map<String, List<RedisConnection>> getConnections(
			String hostList, ClusterConfig config, boolean isSplitWR) {
		Map<String, List<RedisConnection>> splitingList = new HashMap<String, List<RedisConnection>>();
		if (StringUtil.isNotEmpty(hostList)) {
			List<RedisConnection> connectionList = split(hostList);
			for (RedisConnection connection : connectionList) {
				if (connection.getOption() == RedisOption.ReadAndWrite
						&& isSplitWR) {
					RedisConnection[] connections = connection
							.splitConnections();
					if (connections != null) {
						for (RedisConnection connection2 : connections) {
							saveConnection(config.getClusterKey(connection2
									.getOption()), splitingList, connection);
						}
					}
				} else {
					saveConnection(config.getClusterKey(connection.getOption()),
							splitingList, connection);
				}
			}
		}
		return splitingList;
	}

	public Map<String, List<RedisConnection>> getConnectionPool(
			ClusterConfig config) {
		String hostList = config.getHostList();
		return getConnections(hostList, config, true);
	}

	protected void saveConnection(String key,
			Map<String, List<RedisConnection>> connectionMap,
			RedisConnection connection) {
		List<RedisConnection> connections = connectionMap.get(key);
		if (connections == null) {
			connections = new ArrayList<RedisConnection>();
			connectionMap.put(key, connections);
		}
		connections.add(connection);
	}

}
