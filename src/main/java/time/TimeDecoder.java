package time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: PM1:31
 */
public class TimeDecoder extends FrameDecoder {
    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {

        if(buffer.readableBytes() <  4) {
            return  null;
        }


        return  new UnixTime(buffer.readInt());

    }

}
