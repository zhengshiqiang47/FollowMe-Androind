package com.example.coderqiang.followme.Activity;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UploadImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/12/23.
 */

public class SelectImageActivity extends Activity {
    private static final int PHOTO_REQUEST=1;
    private static final int PHOTO_REQUEST_CUT=2;
    private static final int PHOTO_REQUEST_CAREMA = 3;

    @Bind(R.id.select_image_button)
    Button select;
    @Bind(R.id.select_image_view)
    ImageView imageView;

    File tempFile;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectimage);
        ButterKnife.bind(this);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PHOTO_REQUEST);
            }
        });
    }

    private void crop(Uri uri){
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void camera(View view) {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"取消操作",Toast.LENGTH_SHORT).show();
            return;
        }

        ContentResolver resolver = getContentResolver();
        if(requestCode==PHOTO_REQUEST){
            try {
                if (data != null) {
                    Uri originalUri = data.getData();        //获得图片的uri
//                    Log.i("Select",originalUri.getPath());
//                    path=originalUri.getPath();

                    crop(originalUri);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(this.getApplicationContext(), "未找到存储卡，无法存储照片！",Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode==PHOTO_REQUEST_CUT){
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
//                Log.i("Select","裁切之后:"+data.getData().getPath());
                this.imageView.setImageBitmap(bitmap);
                try {
                    write(bitmap);
                    new Upload().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 写到sdcard中
    public void write(Bitmap bm) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);//png类型
        tempFile=new File(Environment.getExternalStorageDirectory(),"temp.png");
        path=tempFile.getAbsolutePath();
        Uri uri=Uri.fromFile(tempFile);
        Log.i("Select","裁切之后"+tempFile.getPath());
        FileOutputStream out=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            out = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "temp.png"));
            Log.i("tempPNG","有内存卡"+"path"+Environment.getExternalStorageDirectory()+"/temp.png");
            path=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/temp.png";
        }else{
            out = this.openFileOutput("temp.png", Context.MODE_PRIVATE);
            Log.i("tempPNG","无内存卡"+"path"+"temp.png");
            path=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/temp.png";
        }
        out.write(baos.toByteArray());
        out.flush();
        out.close();
    }

    private class Upload extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            UploadImage.formUpload(BASE_URL+"uploadServlet",path, User.get(getApplicationContext()).getName(),getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(tempFile.exists()){
                tempFile.delete();
            }
        }
    }
}
