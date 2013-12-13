package chat;

import org.jboss.netty.channel.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: PM7:51
 */
public class ChatClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(
            ChatClientHandler.class.getName());

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
        // Get the SslHandler from the pipeline
        // which were added in SecureChatPipelineFactory.

    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        System.err.println(e.getMessage());
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
}
