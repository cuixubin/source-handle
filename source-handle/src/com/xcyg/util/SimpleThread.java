/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcyg.util;


/**
 *
 * @author xcyg
 */
public class SimpleThread implements Runnable {
    public static String convertMsg = "";
    

    private String localFilePath = null;

    private String toFilePath = null;

    
    public SimpleThread(String localFilePath, String toFilePath) {
        this.localFilePath = localFilePath;
        this.toFilePath = toFilePath;
    }

    public void run() {
        synchronized (this) {
            try {
                String msg = ConvertVideo.process(localFilePath, toFilePath);
                if (msg.equals("error")) {
                    System.out.println(localFilePath + ":" + "have error");
                } else {
                    System.out.println("convert success");
//                    String imagePath = msg.substring(0, msg.indexOf(".")) + ".jpg";
//                    convertMsg += "\"videoPath\":\""+msg+"\",";
//                    convertMsg += "\"thumbnailspath\":\""+imagePath+"\"";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
