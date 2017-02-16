package io.perspica.server;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import io.perspica.common.metrics;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.perspica.worker.worker;
import com.codahale.metrics.annotation.Timed;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.Metered;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class ServerHandler extends SimpleChannelInboundHandler<String> {
    // Technically, these two metrics duplicate the @Metered and @Counted annotations
    private final Meter requests = metrics.registry.meter("requests");
    private final Counter readLines = metrics.registry.counter("lines.read");

    private static final Logger LOG = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    @Metered(name = "requests")
    public void channelActive(ChannelHandlerContext ctx) {
        requests.mark();
        ctx.writeAndFlush("Connection Accepted\n");
        LOG.info("Connection Accepted {}", ctx.channel().remoteAddress().toString());
    }

    @Override
    @Timed
    @Counted(name = "lines.read")
    public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        readLines.inc();
        LOG.debug("{} received {}", ctx.channel(), msg);
        worker.parseLine(msg);
    }
}