package com.mystudy.web.cache.factory;

import com.mongodb.MongoClient;
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

	@Override
	public void afterPropertiesSet() throws Exception {
		if (host != null) {
			if (port == null) {
				mongo = new MongoClient(host);
			} else {
				mongo = new MongoClient(host, port);
			}
		} else {
			mongo = new MongoClient();
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
