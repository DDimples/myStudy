package com.mystudy.web.common.redis.provider;


import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.extend.ExtendJedisCommands;
import com.mystudy.web.common.redis.provider.parallel.Parallel;
import com.mystudy.web.common.redis.provider.shared.Sharded;
import com.mystudy.web.common.redis.provider.twemproxy.Twemproxy;

public class CommandFactory {

	public static ExtendJedisCommands getCommand(ClusterConfig config) {
		ExtendJedisCommands command =null;

		switch (config.getClusterType()) {
		case Twemproxy: 
           command = new Twemproxy(config);
			break;
		case Sharded:
			 command = new Sharded(config);
			break;
		case ParallelWrite:
			 command = new Parallel(config);
			break;
		default:
			break;
		}
		return command;
	}

}
