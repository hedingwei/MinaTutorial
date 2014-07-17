/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.minatutorial.part1.filter.firewall.throttle.enhancement;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 *
 * @author 定巍
 */
public class MinaServerHandler extends IoHandlerAdapter {

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println(session.getRemoteAddress()+" connected");
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        System.out.println("Message Received["+session.getRemoteAddress()+"]: "+message);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);
    }
    
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println(session.getRemoteAddress()+" closed");
    }
    
}
