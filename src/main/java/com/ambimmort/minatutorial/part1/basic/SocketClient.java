/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ambimmort.minatutorial.part1.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 定巍
 */
public class SocketClient {
    public static void main(String[] args){
        try {
            Socket socket = new Socket("localhost", 8888);
            socket.setSoTimeout(1000);
            PrintWriter pw = new PrintWriter(socket.getOutputStream()) ;
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for(int i=0;i<10;i++){
                pw.println("hello world"+i);
                pw.flush();
            }

            String b  =null;
            
            while((b=br.readLine())!=null){
                System.out.println(b);
            }
            
//            socket.shutdownInput();
            
            System.out.println("-------------");
              for(int i=10;i<20;i++){
                pw.println("hello world"+i);
                pw.flush();
            }
            
            pw.flush();
            System.out.println("end-------------");
            
            pw.close();
  
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
