package time;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.util.Date;

/**
 * User: kyle
 * Date: 13-11-9
 * Time: AM11:42
 */
public class TimeClientHandler extends SimpleChannelHandler {


    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
//        long curentTimeMillis = buffer.readInt() * 1000L;
//        System.out.println(new Date(curentTimeMillis));
//        e.getChannel().close();
        UnixTime m = (UnixTime)e.getMessage();
        System.out.println(m);
        e.getChannel().close();
    }

    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {

        e.getCause().printStackTrace();
        e.getChannel().close();
    }


}
