package com.github.yyxff.nexusrpc.core.connectionpool;

import io.netty.channel.Channel;

public class PooledChannel {

    private final Channel channel;

    public PooledChannel(Channel channel) {
        this.channel = channel;
    }
}
