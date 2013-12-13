package time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * User: kyle
 * Date: 13-11-8
 * Time: PM8:49
 */
public class TimeServerHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(
            ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {

//        Channel ch = e.getChannel();
//
//        ChannelBuffer time = ChannelBuffers.buffer(4);
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        ChannelFuture f = ch.write(time);
//        f.addListener(new ChannelFutureListener() {
//            public void operationComplete(ChannelFuture future) throws Exception {
//                Channel ch = future.getChannel();
//                ch.close();
//            }
//        });
        UnixTime time =  new UnixTime((int)(System.currentTimeMillis()/1000));

        ChannelFuture f = e.getChannel().write(time);

        f.addListener(ChannelFutureListener.CLOSE);



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
