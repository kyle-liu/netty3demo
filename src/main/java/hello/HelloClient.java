package hello;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: kyle
 * Date: 13-11-7
 * Time: PM2:38
 */
public class HelloClient {
    public void run() {

        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()
                )
        );

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handler", new HelloWorldClientHandler());
                return pipeline;
            }
        });

        ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 9999));

        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }

    private static class HelloWorldClientHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(
                ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            System.out.println("client recevied message");
            String msg = (String) e.getMessage();
            System.out.println(msg);
            e.getChannel().close();
        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

            e.getChannel().close();

        }

    }


    public static void main(String[] args) {


        new HelloClient().run();

    }
}
