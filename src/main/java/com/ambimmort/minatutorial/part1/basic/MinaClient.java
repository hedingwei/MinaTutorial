/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.minatutorial.part1.basic;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author 定巍
 */
public class MinaClient {
    public static void main(String[] args){
        IoConnector connector = new NioSocketConnector();
        
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
        
        connector.setHandler(new MinaClientHandler());
        
        ConnectFuture f = connector.connect(new InetSocketAddress("localhost",8888));
        
        boolean b = f.awaitUninterruptibly(3, TimeUnit.SECONDS);
        if (b && !f.isConnected()) {
            System.out.println("try connected but can not connect.");
        }else{
            
            while(true){
                try {
                    Thread.sleep(1000);
                    f.getSession().write("mmm+"+System.currentTimeMillis());
                } catch (InterruptedException ex) {
                    Logger.getLogger(MinaClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
       
    }
}
