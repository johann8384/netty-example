package io.perspica;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush("AT\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("Incoming: " + msg);
//        ctx.close();
    }
}