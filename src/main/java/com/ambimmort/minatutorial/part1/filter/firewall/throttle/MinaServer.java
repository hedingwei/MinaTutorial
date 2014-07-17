/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.minatutorial.part1.filter.firewall.throttle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.firewall.ConnectionThrottleFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 瀹氬穽
 */
public class MinaServer {
    
    public static void main(String[] args){
        IoAcceptor acceptor = new NioSocketAcceptor();
        //连接调节器
        ConnectionThrottleFilter throttle = new ConnectionThrottleFilter(1000);
        
        acceptor.getFilterChain().addLast("log", new LoggingFilter());
        acceptor.getFilterChain().addLast("throttle",throttle);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
        acceptor.setHandler(new MinaServerHandler());
        try {
            acceptor.bind(new InetSocketAddress(8888));
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }
    
}
