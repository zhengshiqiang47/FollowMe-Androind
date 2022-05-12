package com.example.coderqiang.followme.Util;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.User;
import com.squareup.haha.trove.THash;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class UploadImage {
    private static final String TAG="UpLoadImage";

    public static String formUpload(String urlStr, String filePath,String name,Context context) {
        String rsp = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "|"; // request头和上传文件内容分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            File file = new File(filePath);
            String filename = file.getName();
            String contentType = "";
            String touxiangName=name;
            if (filename.endsWith(".png")) {
                contentType = "image/png";
                touxiangName+=".png";
            }else
            if (filename.endsWith(".jpg")) {
                contentType = "image/jpg";
                touxiangName+=".jpg";
            }else
            if (filename.endsWith(".gif")) {
                contentType = "image/gif";
                touxiangName+=".gif";
            }else
            if (filename.endsWith(".bmp")) {
                contentType = "image/bmp";
                touxiangName+=".bmp";
            }
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"" + filePath
                    + "\"; filename=\"" + touxiangName+ "\"\r\n");
            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            rsp = buffer.toString();
            reader.close();
            Log.i(TAG,rsp);
            if(rsp.contains("上传成功"));
            reader = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return rsp;
    }

    public static String  DynamicImgUpload( Uri uri,String id,Activity context) {
        String rsp = "";
        String urlStr= "api/dynamicImg/upload";
        HttpURLConnection conn = null;
        String BOUNDARY = "|"; // request头和上传文件内容分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            File file = uri2File(uri,context);
            String filename = file.getName();
            String contentType = "";
            String name=id;
            if (filename.endsWith(".png")) {
                contentType = "image/png";
                name+=".png";
            }else
            if (filename.endsWith(".jpg")) {
                contentType = "image/jpg";
                name+=".jpg";
            }else
            if (filename.endsWith(".gif")) {
                contentType = "image/gif";
                name+=".gif";
            }else
            if (filename.endsWith(".bmp")) {
                contentType = "image/bmp";
                name+=".bmp";
            }
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
            strBuf.append("Content-Disposition: form-data; name=\"" + id
                    + "\"; filename=\"" + name+ "\"\r\n");
            strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
            out.write(strBuf.toString().getBytes());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            rsp = buffer.toString();
            reader.close();
            Log.i(TAG,rsp);
            reader = null;
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return "";
    }

    private static File uri2File(Uri uri,Activity context) {
        File file = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor actualimagecursor = context.managedQuery(uri, proj, null,
                null, null);
        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor
                .getString(actual_image_column_index);
        file = new File(img_path);
        return file;
    }

    public static void setTouxiang(CircleImagview circleImagview,Context context){
//        if (false) {
//            circleImagview.setImageBitmap(User.get(context).getTouxiang());
//        }else{
//            Log.i("UpLoadImage","username"+User.get(context).getName());
            Glide.with(context).load(BASE_URL + "upload/"+ User.get(context).getName()+".png").asBitmap().skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE).override(300,300).centerCrop().into(circleImagview);
//        }
    }
}
