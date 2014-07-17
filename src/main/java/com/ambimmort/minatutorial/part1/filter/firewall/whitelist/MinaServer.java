/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.minatutorial.part1.filter.firewall.whitelist;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.firewall.Subnet;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 *
 * @author 瀹氬穽
 */
public class MinaServer {

    public static void main(String[] args) {
        IoAcceptor acceptor = new NioSocketAcceptor();
        
        acceptor.getFilterChain().addLast("log", new LoggingFilter());
        //tag1:设置白名单过滤器
        WhitelistFilter filter = new WhitelistFilter();
        
        try {
            Subnet subnet = new Subnet(InetAddress.getByName("127.0.0.0"), 24);
            filter.allow(subnet);
        } catch (UnknownHostException ex) {
            Logger.getLogger(MinaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        acceptor.getFilterChain().addLast("whitelist", filter);
        
        //tag1:finished
        
        
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
