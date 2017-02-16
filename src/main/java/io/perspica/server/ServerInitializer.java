package io.perspica.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

/**
 * Created by jcreasy on 2/15/17.
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    static final boolean SSL = System.getProperty("ssl") != null;

    private SslContext getSSLContext() throws SSLException, CertificateException {
        // Determine if we should use SSL or not
        final SslContext sslCtx;

        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        }

        return null;
    }

    @Override
    public void initChannel(SocketChannel ch) throws CertificateException, SSLException {
        final SslContext sslCtx = getSSLContext();

        ChannelPipeline pipeline = ch.pipeline();

        // If we are using SSL, add that handler first to decrypt the incoming data
        if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }

        // Now add our standard handlers
        // Break up the data into Lines
        // Handle turning lines into strings
        pipeline.addLast(
                new LineBasedFrameDecoder(32768),
                new StringEncoder(),
                new StringDecoder()
        );

        // This is our Server code, it now only has to deal with the input line by line
        pipeline.addLast(
                new ServerHandler()
        );
    }
}
