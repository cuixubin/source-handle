/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcyg.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author xcyg
 */
public class SysConfig {
    
    /**
     * 获取配置文件中指定路径值，如果不存在则返回设定的值
     * @param key
     * @param setLocation
     * @return
     * @throws IOException 
     */
    public static  String getValue(String key, String setLocation) throws IOException {
        Properties props = new Properties();  
        InputStream in = SysConfig.class.getClassLoader().getResourceAsStream("sysconf.properties");
        String value = setLocation;
        try {  
            if(null != in) {
                props.load(in);  
                value = props.getProperty(key);  
            }
            // 有乱码时要进行重新编码  
            // new String(props.getProperty("name").getBytes("ISO-8859-1"), "GBK");
            return value;  
        } catch (IOException e) {  
            e.printStackTrace();  
            return value;  
        } finally {  
            if (null != in) {
                in.close();  
            }
        }  
    }
}
