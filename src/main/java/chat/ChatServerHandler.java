package chat;

import org.jboss.netty.channel.*;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: PM7:29
 */
public class ChatServerHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(
            ChatServerHandler.class.getName());
    static final ChannelGroup channels = new DefaultChannelGroup();


    @Override
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

//        final ChatServerHandler chatServerHandler = ctx.getPipeline().get(ChatServerHandler.class);
//
//        // Get notified when SSL handshake is done.
//        ChannelFuture handshakeFuture = chatServerHandler.
//        handshakeFuture.addListener(new Greeter());

        e.getFuture().addListener(new Greeter());
    }

    @Override
    public void channelDisconnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

        channels.remove(e.getChannel());
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {

        // Convert to a String first.
        String request = (String) e.getMessage();

        // Send the received message to all channels but the current one.
        for (Channel c : channels) {
            if (c != e.getChannel()) {
                c.write("[" + e.getChannel().getRemoteAddress() + "] " +
                        request + '\n');
            } else {
                c.write("[you] " + request + '\n');
            }
        }

        // Close the connection if the client has sent 'bye'.
        if ("bye".equals(request.toLowerCase())) {
            e.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }


    private static final class Greeter implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                // Once session is secured, send a greeting.
                future.getChannel().write(
                        "Welcome to " + InetAddress.getLocalHost().getHostName() +
                                " secure chat service!\n");
                future.getChannel().write(
                        "Your session is protected !\n");

                // Register the channel to the global channel list
                // so the channel received the messages from others.
                channels.add(future.getChannel());
            } else {
                future.getChannel().close();
            }
        }
    }
}
