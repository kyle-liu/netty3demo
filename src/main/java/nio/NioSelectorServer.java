package nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * User: kyle
 * Date: 13-11-5
 * Time: PM2:39
 */
public class NioSelectorServer {

    public static void main(String[] args) throws Exception{

        //创建一个selector选择器
       Selector selector = Selector.open();

       //打开一个通道
        ServerSocketChannel socketChannel = ServerSocketChannel.open();

        //绑定到9000端口
        socketChannel.socket().bind(new InetSocketAddress(9000));

        //使设定non-blocking的方式
        socketChannel.configureBlocking(false);

        //向Selector注册Channel以及我们有兴趣的事件
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            //选择事件
           selector.select();
           Iterator<SelectionKey> keyIter =  selector.selectedKeys().iterator();
           while (keyIter.hasNext()){

               SelectionKey key= keyIter.next();
               keyIter.remove();
               System.out.println(key.readyOps());
               if(key.isAcceptable()){
                   System.out.println("Accept");
                   socketChannel.accept();
               }
           }

        }







    }
}
