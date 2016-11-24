/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcyg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

/**
 *
 * @author xcyg
 */
public class PrintStream  extends Thread {
    private InputStream is = null;
    
    public PrintStream(InputStream is) {
        this.is = is;
    }
    
    @Override
    public void run(){
        try{
            while(this != null){
                int ch = is.read();
                if(ch != -1){
                    System.out.println("system out"+(char)ch);
                }else{
                    break;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try {
                is.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(PrintStream.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
