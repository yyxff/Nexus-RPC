package com.gitub.yyxff.nexusrpc.core;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RpcEncoder extends MessageToByteEncoder<Object> {

    private final Serializer serializer;

    public RpcEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        int headIndex = out.writerIndex();
        out.writeInt(0);

        if (msg.getClass().equals(RpcRequest.class)) {
            out.writeByte(0x00);
        } else {
            out.writeByte(0x01);
        }

        out.writeBytes(serializer.serialize((RpcRequest) msg));
        int bodyBytesLength = out.writerIndex() - headIndex - 5;
        out.setInt(headIndex, bodyBytesLength);
    }
}
