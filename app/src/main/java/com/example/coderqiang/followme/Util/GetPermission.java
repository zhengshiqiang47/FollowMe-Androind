package com.example.coderqiang.followme.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by CoderQiang on 2017/2/12.
 */

public class GetPermission {
    private static final String TAG = "GetPermission";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    private static final int PERMISSIONS_LOCATION=2;
    private static final int PERMISSIONS_CALL=3;
    private static final int PERMISSIONS_ALL=0;
    private static final int PERMISSIONS_MICROPHONE=4;
    private static final int PERMISSIONS_STORAGY=5;
    private static final String PERMISSIONS_LOCATION_STR=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String PERMISSIONS_FINE_LOCATION_STR=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSIONS_CALL_STR=Manifest.permission.CALL_PHONE;
    private static final String PERMISSIONS_MICROPHONE_STR=Manifest.permission.RECORD_AUDIO;
    private static final String PERMISSIONS_READ_STORAGY_STR=Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSIONS_WRITE_STORAGY_STR=Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSIONS_CAMERA_STR=Manifest.permission.CAMERA;

    private int permissionSize=8;

    public static boolean getAllPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (
                    ContextCompat.checkSelfPermission(activity,PERMISSIONS_FINE_LOCATION_STR) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(activity,PERMISSIONS_LOCATION_STR) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(activity,PERMISSIONS_WRITE_STORAGY_STR) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(activity,PERMISSIONS_MICROPHONE_STR) != PackageManager.PERMISSION_GRANTED||
                    ContextCompat.checkSelfPermission(activity,PERMISSIONS_CAMERA_STR) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "开始获取权限");
                Toast.makeText(activity,"为保证程序使用正常,请务必允许相应权限",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[]{PERMISSIONS_LOCATION_STR,PERMISSIONS_FINE_LOCATION_STR,PERMISSIONS_MICROPHONE_STR
                        ,PERMISSIONS_READ_STORAGY_STR,PERMISSIONS_CAMERA_STR},PERMISSIONS_ALL);
                return true;
            } else{
                return true;
            }
        }
        return false;
    }

    public static boolean getPermission(Activity activity,String permission,int permissionRequest){
        if (ContextCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED) {

            Log.i(TAG, "开始获取权限"+permission);
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {
                Toast.makeText(activity,"为保证程序使用正常,请务必允许相应权限",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity, new String[]{permission}, permissionRequest);

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission},permissionRequest);
            }
        }else{
            return true;
        }
        return false;
    }
}
