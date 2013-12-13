package discard;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

/**
 * User: kyle
 * Date: 13-11-8
 * Time: PM8:09
 */
public class DiscardServerHandler extends SimpleChannelHandler {

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        ChannelBuffer buffer = (ChannelBuffer) e.getMessage();

//        while (buffer.readable()) {
//
//            System.out.println((char) buffer.readByte());
//            System.out.flush();
//        }

        Channel ch = e.getChannel();
        ch.write(e.getMessage());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {

        e.getCause().printStackTrace();

        Channel ch = e.getChannel();
        ch.close();
    }

}
