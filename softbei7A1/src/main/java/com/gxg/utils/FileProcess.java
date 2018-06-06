package com.gxg.utils;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by 郭欣光 on 2018/5/31.
 */


public class FileProcess {

    public static String deleteFile(String filePath) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        File file = new File(filePath);
        if(file.exists() && file.isFile()) {
            if(file.delete()) {
                message = "删除成功！";
            } else {
                status = "error";
                message = "删除失败！";
            }
        } else {
            status = "error";
            message = "删除文件失败：文件不存在！";
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public static String writeFile(String path, String name, String content) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        try {
            File fileDir = new File(path);
            if(!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(path + name);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte by[] = content.getBytes();
            fileOutputStream.write(by);
            fileOutputStream.close();
            message = "写入文件成功！";
        } catch (FileNotFoundException e) {
            System.out.println(e);
            status = "error";
            message = "写入文件出错：" + e.getMessage();
        } catch (IOException e) {
            System.out.println(e);
            status = "error";
            message = "写入文件出错：" + e.getMessage();
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }

    public static String uploadFile(MultipartFile file, String name, String path) {
        JSONObject jsonObject = new JSONObject();
        String status = "success";
        String message;
        if(file.isEmpty()) {
            status = "error";
            message = "文件为空！";
        } else {
            try {
                File uploadFilePath = new File(path);
                if (!uploadFilePath.exists()) {
                    uploadFilePath.mkdirs();
                }
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(path, name)));
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
                message = "上传成功！";
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
                status = "error";
                message = "上传文档失败：" + e.getMessage();
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(e.getMessage());
                status = "error";
                message = "上传文档失败：" + e.getMessage();
            }
        }
        jsonObject.accumulate("status", status);
        jsonObject.accumulate("message", message);
        return jsonObject.toString();
    }
}
