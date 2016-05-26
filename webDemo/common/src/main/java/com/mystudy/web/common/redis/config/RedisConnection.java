package com.mystudy.web.common.redis.config;

public class RedisConnection implements Cloneable {
	private String host;
	private int port;
	private String name;
	private RedisOption option;
	// default 500ms
	private int timeout = 500;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public RedisOption getOption() {
		return option;
	}

	public void setOption(RedisOption option) {
		this.option = option;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public RedisConnection[] splitConnections() {
		if (this.getOption() == RedisOption.ReadAndWrite) {
			try {
				RedisConnection read = (RedisConnection) clone();
				read.setOption(RedisOption.Read);
				RedisConnection write = (RedisConnection) clone();
				write.setOption(RedisOption.Write);
				return new RedisConnection[] { read, write };
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getConnectionInfo() {
		return host + ":" + port + ":" + timeout;
	}

	@Override
	public String toString() {
		return "RedisConnection [host=" + host + ", port=" + port + ", name="
				+ name + ", option=" + option + ", timeout=" + timeout + "]";
	}

}
