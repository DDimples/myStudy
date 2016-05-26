package com.mystudy.web.common.redis.provider.shared;

import java.net.URI;


import com.mystudy.web.common.redis.extend.ExtendJedis;
import redis.clients.jedis.Protocol;
import redis.clients.util.ShardInfo;
import redis.clients.util.Sharded;

public class ExtendShardInfo extends ShardInfo<ExtendJedis> {

	public String toString() {
		return host + ":" + port + "*" + getWeight();
	}

	private int timeout = 500;
	private final String host;
	private final int port;
	private String password = null;
	private String name = null;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public ExtendShardInfo(String host) {
		super(Sharded.DEFAULT_WEIGHT);
		URI uri = URI.create(host);
		if ("redis".equals(uri.getScheme())) {
			this.host = uri.getHost();
			this.port = uri.getPort();
			this.password = uri.getUserInfo().split(":", 2)[1];
		} else {
			this.host = host;
			this.port = Protocol.DEFAULT_PORT;
		}
	}

	public ExtendShardInfo(String host, String name) {
		this(host, Protocol.DEFAULT_PORT, name);
	}

	public ExtendShardInfo(String host, int port, int weight) {
		super(weight);
		this.host = host;
		this.port = port;
	}

	public ExtendShardInfo(String host, int port, String name) {
		this(host, port, Sharded.DEFAULT_WEIGHT);
		this.name = name;
	}

	public ExtendShardInfo(String host, int port, int timeout, String name) {
		this(host, port, Sharded.DEFAULT_WEIGHT);
		this.timeout = timeout;
	}

	public ExtendShardInfo(String host, int port, int timeout, String name,
			int weight) {
		this(host, port, weight);
		this.name = name;
		this.timeout = timeout;
	}

	public ExtendShardInfo(URI uri) {
		super(Sharded.DEFAULT_WEIGHT);
		this.host = uri.getHost();
		this.port = uri.getPort();
		this.password = uri.getUserInfo().split(":", 2)[1];
	}

	public String getPassword() {
		return password;
	}

	public int getTimeout() {
		return timeout;
	}

	@Override
	protected ExtendJedis createResource() {
		return new ExtendJedis(this);
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * only check host and port.notice: null is not same as null
	 * 
	 * @param info
	 * @param otherinfo
	 * @return
	 */

	public static boolean isSame(ExtendShardInfo info, ExtendShardInfo otherinfo) {
		return (info != null)
				&& (otherinfo != null)
				&& (otherinfo == info || ((info.host == null ? otherinfo.host == null
						: info.host.equals(otherinfo.host)) && (info.port == otherinfo.port)));
	}

	@Override
	public boolean equals(Object obj) {
		if (ExtendShardInfo.class.isInstance(obj)) {
			ExtendShardInfo other = (ExtendShardInfo) obj;
			return (this.host == null ? other.host == null : this.host
					.equals(other.host))
					&& (this.port == other.port)
					&& (this.name == null ? other.name == null : this.name
							.equals(other.name))
					&& (this.password == null ? other.password == null
							: this.password.equals(other.password))
					&& (this.getWeight() == other.getWeight())
					&& (this.timeout == other.timeout);

		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = (port | (this.getWeight() << 16));
		hash = (hash << 13) | (hash >>> -13);
		hash ^= timeout;
		return new StringBuilder(this.host).append(this.name)
				.append(this.password).toString().hashCode()
				^ hash;
	}

}
