package time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: PM2:00
 */
public class TimeEncoder extends SimpleChannelHandler {

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
        UnixTime time = (UnixTime) e.getMessage();
        ChannelBuffer buf = ChannelBuffers.buffer(4);
        buf.writeInt(time.getValue());

        Channels.write(ctx, e.getFuture(), buf);

    }
}
