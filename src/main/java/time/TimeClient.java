package time;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: AM11:39
 */
public class TimeClient {
    public static void main(String[] args) {

        String  host  = "localhost";
        int port = 8080;

        ChannelFactory factory = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()
        );

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new TimeDecoder(),
                        new TimeClientHandler());
            }
        });

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive",true);
         ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        future.awaitUninterruptibly();

        if( !future.isSuccess()) {
            future.getCause().printStackTrace();

        }

        future.getChannel().getCloseFuture().awaitUninterruptibly();
        factory.releaseExternalResources();

    }
}
