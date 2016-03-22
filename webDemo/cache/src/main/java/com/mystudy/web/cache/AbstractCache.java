package com.mystudy.web.cache;

import java.util.List;
import java.util.Map;

public abstract class AbstractCache implements Cache {

	@Override
	public List<?> keys() throws CacheException {
		return null;
	}

	@Override
	public List<?> mget(String... keys) throws CacheException {
		return null;
	}

	@Override
	public void clear() throws CacheException {

	}

	@Override
	public void setMap(String key, String field, Object value) {

	}

	@Override
	public Map<String, Object> getMap(String key, String... fields) {
		return null;
	}

	@Override
	public void setMap(String key, Map<String, Object> value) {

	}

	@Override
	public void lpush(String key, Object... value) {
	}

	@Override
	public Object lpop(String key) {
		return null;
	}

	@Override
	public void hset(String key, String field, Object value) {

	}

	@Override
	public Object hget(String key, String field) {
		return null;
	}

	@Override
	public void hmset(String key, Map<String, Object> hash) {

	}

	@Override
	public List<Object> hmget(String key, String... fields) {
		return null;
	}
	
	@Override
	public long incr(String key) {
		return 0;
	}

	@Override
	public boolean hsupport() {
		return false;
	}

	@Override
	public boolean msupport() {
		return false;
	}

	@Override
	public boolean hmsupport() {
		return false;
	}

	@Override
	public boolean lsupport() {
		return false;
	}

	@Override
	public boolean incrsupport() {
		return false;
	}
}
