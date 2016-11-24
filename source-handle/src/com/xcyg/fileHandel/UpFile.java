/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcyg.fileHandel;

import com.xcyg.util.ConvertVideo;
import com.xcyg.util.SimpleThread;
import com.xcyg.util.SysConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author xcyg
 */
public class UpFile extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String IMGTYPE = "jpg_jpeg_bmp_gif_png_tiff_JPG_JPEG_PNG_BMP_GIF_TIFF";
        String VIDEOTYPE = "avi_wmv_mpeg_mp4_mov_mkv_flv_f4v_m4v_rmvb_rm_3gp_dat_ts_mts_vob";
        
        //文件名
        String fname = request.getParameter("filename");
        //文件后缀
        String fsuf = null;
        try {
            fsuf = fname.substring(fname.lastIndexOf(".")+1);
        } catch(Exception e) {
            return;
        }
        //资源保存的文件夹名称
        String uploadFolder = SysConfig.getValue("UPLOAD_FOLDER", "/upload");
        //实际路径
        String sourcePath = SysConfig.getValue("SOURCE_PATH", "C:/upload") + uploadFolder;
        //返回的访问路径名+文件名
        String filePath = "";
        //视频文件标识和封面路径
        boolean ifVideo = false;
        String coverPath = "";
        
        request.setCharacterEncoding("UTF-8");
        if(null != fsuf && !"".equals(fsuf)) {
            if(IMGTYPE.contains(fsuf)) {
                sourcePath += SysConfig.getValue("IMG_PATH", "/images");
                filePath += SysConfig.getValue("IMG_PATH", "/images");
            }else if(VIDEOTYPE.contains(fsuf)) {
                sourcePath += SysConfig.getValue("VIDEO_PATH", "/video");
                filePath += SysConfig.getValue("VIDEO_PATH", "/video");
                ifVideo = true;
            }else {
                sourcePath += SysConfig.getValue("FILE_PATH", "/file");
                filePath += SysConfig.getValue("FILE_PATH", "/file");
            }
        }else {
            sourcePath += SysConfig.getValue("FILE_PATH", "/file");
            filePath += SysConfig.getValue("FILE_PATH", "/file");
        }
        
        InputStream input = request.getInputStream(); 
        
        File file = new File(sourcePath);  
        if(!file.exists()){  
            file.mkdirs();  
        }  
        //以时间给文件重命名
        
        String newName = new Date().getTime() + ".";
        if(ifVideo) {
            fname = sourcePath + "/" +  newName + fsuf;
            coverPath = filePath + "/" + newName + "jpg";
            newName += "flv";
        }else {
            newName += fsuf;
            fname = sourcePath + "/" +  newName;
        }
        filePath += "/" +  newName;
        FileOutputStream fos = new FileOutputStream(fname);  
  
        int size = 0;  
        byte[] buffer = new byte[1024];  
        while ((size = input.read(buffer,0,1024)) != -1) {  
            fos.write(buffer, 0, size);  
        }  
        fos.close();  
        input.close();
        
        String json = ""; 
        json = "{ "
                + "\"state\": \"SUCCESS\","
                + "\"fpath\": \""+filePath+"\","
                + "\"fname\": \""+newName+"\"";
        if(null!=coverPath && !"".equals(coverPath)) {
            String videoTime = ConvertVideo.getMovieTime(fname);
            String toPath = fname.substring(0, fname.lastIndexOf("/"));
            Thread thread = new Thread(new SimpleThread(fname, toPath));
            thread.start();
            json += ",\"videoTime\": \""+videoTime+"\"";
            json += ",\"videoCoverPath\": \""+coverPath+"\"";
        }
        json += "}";
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
