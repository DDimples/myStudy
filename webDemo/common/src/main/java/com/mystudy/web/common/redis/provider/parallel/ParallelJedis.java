package com.mystudy.web.common.redis.provider.parallel;


import com.mystudy.web.common.redis.extend.ExtendJedis;

public class ParallelJedis extends ExtendJedis {
	private final ParallelStatus parallelStatus;

	public ParallelJedis(ParallelStatus parallelStatus) {
		super(parallelStatus.getRedisConnection());
		this.parallelStatus = parallelStatus;
	}

	public ParallelStatus getParallelStatus() {
		return parallelStatus;
	}

}
