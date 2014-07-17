/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ambimmort.minatutorial.part1.filter.firewall.throttle.enhancement;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class SocketClient {

    public static void main(String[] args) {
        for (int j = 0; j < 100; j++) {
            try {
                Socket socket = new Socket("localhost", 8888);
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                for (int i = 0; i < 10; i++) {
                    pw.println("hello world" + i);
                }
                pw.flush();
                pw.close();

            } catch (IOException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
