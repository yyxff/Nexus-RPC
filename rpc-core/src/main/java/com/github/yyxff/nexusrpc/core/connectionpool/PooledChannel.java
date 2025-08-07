package com.github.yyxff.nexusrpc.core.connectionpool;

import com.github.yyxff.nexusrpc.core.messagestruct.RpcRequest;
import io.netty.channel.Channel;

import java.util.concurrent.atomic.AtomicBoolean;

public class PooledChannel {

    private final Channel channel;
    private final AtomicBoolean using = new AtomicBoolean(false);

    public PooledChannel(Channel channel) {
        this.channel = channel;
    }

    public void writeAndFlush(RpcRequest request) {
        try {
            channel.writeAndFlush(request).sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        release();
    }

    public boolean isUsing() {
        return using.get();
    }

    public boolean tryUse() {
        return using.compareAndSet(false, true);
    }

    private void release() {
        using.set(false);
    }
}
