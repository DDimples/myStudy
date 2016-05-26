package com.mystudy.web.common.redis.extend;

public class JedisConfig {
	private int timeout = 500;
	private final String host;
	private final int port;
	private String password = null;
	private String name = null;

	public JedisConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public JedisConfig(String host, int port, int timeout) {
		this(host, port);
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
