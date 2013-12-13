package echo;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * Sends one message when a connection is open and echoes back any received
 * data to the server.  Simply put, the echo client initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClient {

    private final String host;
    private final int port;
    private final int firstMessageSize;

    public EchoClient(String host, int port, int firstMessageSize) {
        this.host = host;
        this.port = port;
        this.firstMessageSize = firstMessageSize;
    }

    public void run() {
        // Configure the client.
        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new EchoClientHandler(firstMessageSize));
            }
        });

        // Start the connection attempt.
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));

        // Wait until the connection is closed or the connection attempt fails.
        future.getChannel().getCloseFuture().awaitUninterruptibly();

        // Shut down thread pools to exit.
        bootstrap.releaseExternalResources();
    }

    private static class EchoClientHandler extends SimpleChannelUpstreamHandler {

        private static final Logger logger = Logger.getLogger(
                EchoClientHandler.class.getName());

        private final ChannelBuffer firstMessage;
        private final AtomicLong transferredBytes = new AtomicLong();

        /**
         * Creates a client-side handler.
         */
        public EchoClientHandler(int firstMessageSize) {
            if (firstMessageSize <= 0) {
                throw new IllegalArgumentException(
                        "firstMessageSize: " + firstMessageSize);
            }
            firstMessage = ChannelBuffers.buffer(firstMessageSize);
            for (int i = 0; i < firstMessage.capacity(); i++) {
                firstMessage.writeByte((byte) i);
            }
        }

        public long getTransferredBytes() {
            return transferredBytes.get();
        }

        @Override
        public void channelConnected(
                ChannelHandlerContext ctx, ChannelStateEvent e) {
            // Send the first message.  Server will not send anything here
            // because the firstMessage's capacity is 0.
            e.getChannel().write(firstMessage);
        }

        @Override
        public void messageReceived(
                ChannelHandlerContext ctx, MessageEvent e) {
            // Send back the received message to the remote peer.
            transferredBytes.addAndGet(((ChannelBuffer) e.getMessage()).readableBytes());
            System.out.println("messageReceived");
            // e.getChannel().write(e.getMessage());
            ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
            System.out.println(buffer.toString());


        }

        @Override
        public void exceptionCaught(
                ChannelHandlerContext ctx, ExceptionEvent e) {
            // Close the connection when an exception is raised.
            logger.log(Level.WARNING,
                    "Unexpected exception from downstream.",
                    e.getCause());
            e.getChannel().close();
        }

    }

    public static void main(String[] args) throws Exception {
        args = new String[3];
        args[0] = "localhost";
        args[1] = "8080";
        args[2] = "10";
        // Print usage if no argument is specified.
        if (args.length < 2 || args.length > 3) {
            System.err.println(
                    "Usage: " + EchoClient.class.getSimpleName() +
                            " <host> <port> [<first message size>]");
            return;
        }

        // Parse options.
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        final int firstMessageSize;
        if (args.length == 3) {
            firstMessageSize = Integer.parseInt(args[2]);
        } else {
            firstMessageSize = 256;
        }

        new EchoClient(host, port, firstMessageSize).run();
    }
}
