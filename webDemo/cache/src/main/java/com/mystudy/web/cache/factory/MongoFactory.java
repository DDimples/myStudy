package com.mystudy.web.cache.factory;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.mongodb.Mongo;

public class MongoFactory implements FactoryBean<Mongo>, InitializingBean,
		DisposableBean {
	private Mongo mongo;
	private String host;
	private Integer port;

	@Override
	public void destroy() throws Exception {
		if (mongo != null) {
			mongo.close();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void afterPropertiesSet() throws Exception {
		if (host != null) {
			if (port == null) {
				mongo = new Mongo(host);
			} else {
				mongo = new Mongo(host, port);
			}
		} else {
			mongo = new Mongo();
		}
	}

	@Override
	public Mongo getObject() throws Exception {
		return mongo;
	}

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
