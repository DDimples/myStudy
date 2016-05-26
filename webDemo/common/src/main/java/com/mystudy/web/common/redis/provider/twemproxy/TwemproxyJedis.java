package com.mystudy.web.common.redis.provider.twemproxy;


import com.mystudy.web.common.redis.extend.ExtendJedis;

public class TwemproxyJedis extends ExtendJedis {

	public TwemproxyJedis(TwemproxyStatus twemproxyStatus) {
		super(twemproxyStatus.getJedisInfo());
		this.twemproxyStatus = twemproxyStatus;
	}

	private final TwemproxyStatus twemproxyStatus;

	public TwemproxyStatus getTwemproxyStatus() {
		return twemproxyStatus;
	}

}
