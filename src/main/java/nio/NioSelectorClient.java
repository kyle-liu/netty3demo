package nio;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * User: kyle
 * Date: 13-11-5
 * Time: PM2:49
 */
public class NioSelectorClient {

    public static void main(String[] args)  throws  Exception{
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 9000));
    }
}
