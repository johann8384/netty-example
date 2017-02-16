package io.perspica.server;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import io.perspica.common.metrics;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.perspica.worker.worker;

class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final Meter requests = metrics.registry.meter("requests");
    private final Counter readLines = metrics.registry.counter("lines.read");

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        requests.mark();
        ctx.writeAndFlush("Connection Accepted\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        readLines.inc();
        worker.parseLine(msg);
    }
}