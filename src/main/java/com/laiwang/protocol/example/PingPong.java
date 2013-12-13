package com.laiwang.protocol.example;

import com.laiwang.protocol.*;
import com.laiwang.protocol.transport.ResponseStatus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.laiwang.protocol.Content.content;
import static com.laiwang.protocol.LWP.*;


import static com.laiwang.protocol.transport.ResponseStatus.OK;

/**
 * User: kyle
 * Date: 13-11-26
 * Time: AM11:10
 */
public class PingPong {

    public static void main(String[] args) throws Exception {
           final CountDownLatch latch = new CountDownLatch(1);

           bind("tcp://localhost:12306", Pong.class);


           ask("tcp://localhost:12306", request("/ping"), new ReplyOn(1000L) {

               @Status(ResponseStatus.Code.OK)
               void countDown(Response response) {
                   latch.countDown();
               }

               @Override
               public void unhandled(Response response) throws Exception {
                   System.err.println("Received unhandled response: " + response);
               }

               @Override
               public void caught(Throwable t) {
                   t.printStackTrace();
               }
           });

           System.out.println("result is " + latch.await(100L, TimeUnit.SECONDS));

           close();
       }

       static class Pong implements Listen {

           @Listen.Path("/ping")
           void replyOK(Request request, Context context) {
               reply(context, response(OK, content("pong")));
           }

           @Override
           public void unhandled(Request request, Context context) throws Exception {
               System.err.println("Received unhandled request: " + request);
           }

           @Override
           public void caught(Throwable t, Context context) {
               t.printStackTrace();
           }
       }

}
