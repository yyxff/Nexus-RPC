package com.github.yyxff.nexusrpc.core;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.logging.Logger;

public class RpcDecoder extends ByteToMessageDecoder{

    /**
     * The first 4 bytes: message length
     * The 5th bytes: 0x00 = RpcRequest
     *                0x01 = RpcResponse
     */
    private final int headerBytes = 5;

    private final Serializer serializer;

    private static final Logger logger = Logger.getLogger(RpcDecoder.class.getName());


    public RpcDecoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        logger.info("RpcDecoder decode start");
        // Not enough header bytes
        if (in.readableBytes() < headerBytes) {
            return;
        }
        // Not enough message length
        int length = in.getInt(in.readerIndex());
//        logger.info("decode length: " + length);
        if (in.readableBytes() + headerBytes < length) {
            return;
        }


        in.skipBytes(headerBytes - 1);
        byte msgTypeFlag = in.readByte();
        Class<?> msgType = null;
        if (msgTypeFlag == 0x00){
            msgType = RpcRequest.class;
        } else if (msgTypeFlag == 0x01){
            msgType = RpcResponse.class;
        } else {
            throw new RuntimeException("invalid message type");
        }

        byte[] data = new byte[length];
        in.readBytes(data);

        out.add(serializer.deserialize(data, msgType));
    }
}
