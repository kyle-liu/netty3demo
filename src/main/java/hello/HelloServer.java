package hello;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: kyle
 * Date: 13-11-6
 * Time: PM5:04
 */
public class HelloServer {

    public void run() {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()
                )
        );
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new StringDecoder());
                pipeline.addLast("encoder", new StringEncoder());
                pipeline.addLast("handler", new HelloWorldServerHandler());
                return  pipeline;
            }
        });
        bootstrap.bind(new InetSocketAddress(9999));

        System.out.println("server started...");
    }

    private static class HelloWorldServerHandler extends SimpleChannelHandler {


        @Override
        public void channelConnected(
                ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("server rec client connected ");

             e.getChannel().write("Hello World");

        }

        public void exceptionCaught(
                ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            e.getCause().printStackTrace();
            e.getChannel().close();
        }

    }

    public static void main(String[] args) {

        new HelloServer().run();

    }
}
