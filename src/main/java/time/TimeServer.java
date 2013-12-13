package time;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: AM11:51
 */
public class TimeServer {

    static  final ChannelGroup allChannels = new DefaultChannelGroup("time-server");


    public static void main(String[] args) {
        ChannelFactory factory =
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {


            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new TimeEncoder(), new TimeServerHandler());
            }
        });

        bootstrap.setOption("tcpNoDelay", true);

        bootstrap.setOption("keepAlive", true);


        Channel channel = bootstrap.bind(new InetSocketAddress(8080));
        allChannels.add(channel);
        System.out.println("time server started...");

    }
}
