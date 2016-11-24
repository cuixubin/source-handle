/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xcyg.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author xcyg
 */
public class ConvertVideo {
    private static String mencode = "G:\\mencode\\mencoder.exe";

    public static String process(String localFilePath, String toFilePath) throws IOException {
        int type = checkContentType(localFilePath);
        String msg = "";
        if (type == 0) {
            // 直接将文件转为flv文件  
            msg = processFLV(localFilePath);
        } else if (type == 1) {
            boolean status = processAVI(localFilePath);
            if (!status) {
                afterProcess(localFilePath);
                return "error";
            }
            deleteFile(localFilePath);
            String aviFilePath = toFilePath + ".avi";
            // 将avi转为flv  
            msg = processFLV(aviFilePath);
        } else if (type == 2) {
            List<String> imageCommand = processImage(localFilePath);
            new ProcessBuilder(imageCommand).start();
            msg = localFilePath;
        }
        return msg;
    }

    /**
     * asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv,wmv9，rm，rmvb
     *
     * @param path
     * @return
     */
    private static int checkContentType(String path) {
        String type = path.substring(path.lastIndexOf(".") + 1, path.length())
                .toLowerCase();
        // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
        if (type.equals("avi")) {
            return 0;
        } else if (type.equals("mpg")) {
            return 0;
        } else if (type.equals("wmv")) {
            return 0;
        } else if (type.equals("3gp")) {
            return 0;
        } else if (type.equals("mov")) {
            return 0;
        } else if (type.equals("mp4")) {
            return 0;
        } else if (type.equals("asf")) {
            return 0;
        } else if (type.equals("asx")) {
            return 0;
        } else if (type.equals("flv")) {
            return 2;
        } else if (type.equals("wmv9")) {
            return 1;
        } else if (type.equals("rm")) {
            return 1;
        } else if (type.equals("rmvb")) {
            return 1;
        }
        return 9;
    }

    private static boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        }
        return true;
    }

    // 对ffmpeg无法解析的文件格式(wmv9，rm，rmvb等), 可以先用别的工具（mencoder）转换为avi(ffmpeg能解析的)格式.  
    private static boolean processAVI(String localFilePath) {
        List<String> commend = new ArrayList<String>();
        commend.add(mencode);
        commend.add(localFilePath);
        commend.add("-oac");
        commend.add("lavc");
        commend.add("-lavcopts");
        commend.add("acodec=mp3:abitrate=64");
        commend.add("-ovc");
        commend.add("xvid");
        commend.add("-xvidencopts");
        commend.add("bitrate=600");
        commend.add("-of");
        commend.add("avi");
        commend.add("-o");
        commend.add(localFilePath.substring(0, localFilePath.indexOf(".")) + ".avi");
        try {
            Process builder = new ProcessBuilder(commend).start();
            processThread(builder);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）  
    private static String processFLV(String localFilePath) throws IOException {
        if (!checkfile(localFilePath)) {
            System.out.println("file is not found");
            return "error";
        }
        // 文件命名  
        List<String> commend = new ArrayList<String>();
        String ffmpegPath = SysConfig.getValue("videoTools", "D:/devApps/ffmpeg.exe");
        commend.add(ffmpegPath);
        commend.add("-i");
        commend.add(localFilePath);
        commend.add("-ab");
        commend.add("56");
        commend.add("-ar");
        commend.add("22050");
        commend.add("-qscale");
        commend.add("8");
        commend.add("-r");
        commend.add("15");
        commend.add("-s");
        commend.add("1366x768");
        String flvPath = localFilePath.substring(0, localFilePath.lastIndexOf(".")) + ".flv";
        commend.add(flvPath);
        Process builder = null;
        try {
            builder = new ProcessBuilder(commend).start();
            processThread(builder);
            List<String> imageCommand = processImage(flvPath);
            new ProcessBuilder(imageCommand).start();
            return flvPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (builder != null) {
                builder = null;
            }
            afterProcess(localFilePath);
        }
        return "error";
    }

    /**
     * 处理进程
     *
     * @param builder
     * @throws InterruptedException
     */
    private static void processThread(Process builder) throws InterruptedException {
        new PrintStream(builder.getErrorStream()).start();
        new PrintStream(builder.getInputStream()).start();
        builder.waitFor();
    }

    private static List<String> processImage(String localFilePath) throws IOException {
        List<String> commend = new ArrayList();
        String ffmpegPath = SysConfig.getValue("videoTools", "D:/devApps/ffmpeg.exe");
        commend.add(ffmpegPath);// 视频提取工具的位置
        commend.add("-ss");
        commend.add("08.010");
        commend.add("-i");
        commend.add(localFilePath);
        commend.add("-y");
        commend.add("-f");
        commend.add("image2");

        commend.add("-vframes");
        commend.add("100");
        commend.add("-s");
        commend.add("352x240");
        String imagePath1 = localFilePath.substring(0, localFilePath.lastIndexOf(".")) + ".jpg";
        commend.add(imagePath1);
        return commend;
    }

    private static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    private static void afterProcess(String localPath) {
        String aviPath = localPath.substring(0, localPath.indexOf(".")) + ".avi";
        if (isExist(aviPath)) {
            deleteFile(aviPath);
            return;
        }
        String mpgPath = localPath.substring(0, localPath.indexOf(".")) + ".mpg";
        if (isExist(mpgPath)) {
            deleteFile(mpgPath);
            return;
        }
        String mp4Path = localPath.substring(0, localPath.indexOf(".")) + ".mp4";
        if (isExist(mp4Path)) {
            deleteFile(mp4Path);
            return;
        }
        String movPath = localPath.substring(0, localPath.indexOf(".")) + ".mov";
        if (isExist(movPath)) {
            deleteFile(movPath);
            return;
        }
        if (isExist(localPath)) {
            deleteFile(localPath);
        }
    }

    /**
     * 获取视频时长
     *
     * @param movieFfmpegUrl
     * @return
     */
    public static String getMovieTime(String movieFfmpegUrl) {
        try {
            List<String> command = new java.util.ArrayList<String>();
            String ffmpegPath = SysConfig.getValue("videoTools", "D:/devApps/ffmpeg.exe");
            command.add(ffmpegPath);
            command.add("-i");
            command.add(movieFfmpegUrl);
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader buf = null;
            String line = null;
            buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String info = "";
            while ((line = buf.readLine()) != null) {
                if (line.indexOf("Duration:") != -1) {
                    info = line.substring(line.indexOf(":") + 1, line.indexOf(","));
                    break;
                }
            }
            p.waitFor();
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        String flag = ConvertVideo.process("F:\\test.mp4", "F:\\");
//        System.out.println(flag);
//        String info = ConvertVideo.getMovieTime("F:\\chimiantiao.rm");
//        System.out.println(info);
//        String test = "C:\\test\\test11.jpg";
//        test= test.substring(test.lastIndexOf("\\")+1);
//        System.out.println(test);
    }
}
