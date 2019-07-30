package com.suql.devicecollect.service;

public interface RedisService {

    /**
     * 保存到redis
     *
     * @param key
     * @param value
     * @return
     */
    void set(final String key, String value);


    /**
     * 从Redis取出
     *
     * @param key
     * @return
     */
    String get(final String key);
}
