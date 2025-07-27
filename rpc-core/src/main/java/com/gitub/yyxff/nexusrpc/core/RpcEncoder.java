package com.gitub.yyxff.nexusrpc.core;

import com.github.yyxff.nexusrpc.common.RpcRequest;
import com.github.yyxff.nexusrpc.common.RpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;

public class RpcEncoder extends MessageToByteEncoder<Object> {

    private static final Log log = LogFactory.getLog(RpcEncoder.class);
    private final Serializer serializer;

    private static final Logger logger = Logger.getLogger(RpcEncoder.class.getName());


    public RpcEncoder(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
//        logger.info("RpcEncoder.encode");
        int headIndex = out.writerIndex();
        out.writeInt(0);

        if (msg.getClass().equals(RpcRequest.class)) {
            out.writeByte(0x00);
            out.writeBytes(serializer.serialize((RpcRequest) msg));
        } else if (msg.getClass().equals(RpcResponse.class)) {
            out.writeByte(0x01);
            out.writeBytes(serializer.serialize((RpcResponse) msg));
        } else {
            throw new RuntimeException("Unsupported msg type: " + msg.getClass());
        }

        int bodyBytesLength = out.writerIndex() - headIndex - 5;
        out.setInt(headIndex, bodyBytesLength);
//        logger.info("Done.encode");
    }
}
