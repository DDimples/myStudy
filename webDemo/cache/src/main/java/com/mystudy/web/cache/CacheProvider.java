package com.mystudy.web.cache;

import com.mystudy.web.cache.config.CacheSetting;

/**
 * Created by 程祥 on 16/3/22.
 * Function：
 */
public interface CacheProvider {

    public String name();

    Cache buildCache(CacheSetting setting)
            throws CacheException;

    void start() throws CacheException;

    void stop();

}
