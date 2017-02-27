package com.example.coderqiang.followme.Fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.PlanNode;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.example.coderqiang.followme.Activity.ChinaActivity;
import com.example.coderqiang.followme.Activity.LoginActivity;
import com.example.coderqiang.followme.Activity.NewDynamicActivity;
import com.example.coderqiang.followme.Activity.PictureActivity;
import com.example.coderqiang.followme.Activity.ScenicActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Activity.SelectImageActivity;
import com.example.coderqiang.followme.Activity.TestActivity;
import com.example.coderqiang.followme.Activity.WebViewActivity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.Util.UserUtil;
import com.example.coderqiang.followme.View.AddScenicDialog;
import com.hyphenate.chat.EMClient;
import com.lcodecore.tkrefreshlayout.header.progresslayout.CircleImageView;
import com.nightonke.boommenu.BoomMenuButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by CoderQiang on 2016/11/1.
 */

public class UserinfoFragment extends android.support.v4.app.Fragment  {
    private static final String TAG="UserInfoFragment";

    @Bind(R.id.userinfo_my_message)
    LinearLayout myMessageLayout;
    @Bind(R.id.circleImagview)
    CircleImagview imageView;
    @Bind(R.id.userinfo_concern)
    TextView concernTV;
    @Bind(R.id.userinfo_follower)
    TextView followerTV;
    @Bind(R.id.userinfo_nick)
    TextView nickTv;
    @Bind(R.id.userinfo_signature)
    EditText signatureTV;
    @Bind(R.id.userinfo_travel)
    TextView travelTv;
    @Bind(R.id.userinfo_edit)
    ImageView editTv;
    @Bind(R.id.scenic_collection_recycler)
    RecyclerView collectRecycler;
    @Bind(R.id.scenic_collection)
    LinearLayout collectLayout;
    @Bind(R.id.scenic_collection_arrow)
    ImageView collectArrow;
    @Bind(R.id.logout)
    LinearLayout logoutLayout;

    private static final int PHOTO_REQUEST=1;
    private static final int PHOTO_REQUEST_CUT=2;
    private static final int PHOTO_REQUEST_CAREMA = 3;
    private ArrayList<Scenicspot> collectScenicspots;
    File tempFile;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    String path;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uerinfo_detail_2, container, false);
         ButterKnife.bind(this, v);
        loadImg();
        initView();
        new getUserInfo().execute();
        myMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewDynamicActivity.class);
//                intent.putExtra(WebViewActivity.TYPE,WebViewActivity.TYPE_URL);
//                intent.putExtra(WebViewActivity.WEB_URL,"http://you.ctrip.com/travels");
                startActivity(intent);
            }
        });
        return v;
    }

    private void initView(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,PHOTO_REQUEST);
            }
        });
        signatureTV.addTextChangedListener(new TextWatcher() {
            String before = "";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(before)){

                }
            }
        });
        UploadImage.setTouxiang(imageView,getActivity().getApplicationContext());
        collectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectRecycler.getVisibility()==View.GONE){
                    showCollect();
                }else if(collectRecycler.getVisibility()==View.VISIBLE){
                    hideCollect();
                }
            }
        });
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                EMClient.getInstance().logout(true);
                getActivity().finish();
                ScenicspotLab.get(getActivity()).clear();
            }
        });
    }


    private void showCollect(){
        collectScenicspots= ScenicspotLab.get(getActivity().getApplicationContext()).getCollectionspots();
        if (collectScenicspots.size() == 0) {
            ScenicspotLab.get(getActivity().getApplicationContext()).getCollectionspots().add(ScenicspotLab.get(getActivity()).getScenicspots().get(1));
        }
        Log.i(TAG, "收藏个数" + collectScenicspots.size());
        collectRecycler.setVisibility(View.VISIBLE);
        collectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        collectRecycler.setAdapter(new scenicAdapter());
        TranslateAnimation translate=new TranslateAnimation(collectRecycler.getWidth(),0,0,0);
        translate.setDuration(800);
        translate.setFillAfter(true);
        collectRecycler.startAnimation(translate);

    }

    private void hideCollect(){
        TranslateAnimation translate=new TranslateAnimation(0,collectRecycler.getWidth(),0,0);
        translate.setDuration(800);
        translate.setFillAfter(true);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                collectRecycler.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        collectRecycler.startAnimation(translate);
    }

    private void loadImg() {
//        Glide.with(this)
//                .load("http://dl.bizhi.sogou.com/images/2012/02/21/92347.jpg")
//                .centerCrop()
//                .crossFade()
//                .placeholder(R.drawable.geometry)
//                .into(userImg);
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
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==getActivity().RESULT_CANCELED){
            Toast.makeText(getActivity().getApplicationContext(),"取消操作",Toast.LENGTH_SHORT).show();
            return;
        }
        ContentResolver resolver =getActivity(). getContentResolver();
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
                Toast.makeText(getActivity().getApplicationContext(), "未找到存储卡，无法存储照片！",Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode==PHOTO_REQUEST_CUT){
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
//                Log.i("Select","裁切之后:"+data.getData().getPath());
                imageView.setImageBitmap(bitmap);
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
            out = getActivity().openFileOutput("temp.png", Context.MODE_PRIVATE);
            Log.i("tempPNG","无内存卡"+"path"+"temp.png");
            path=Environment.getExternalStorageDirectory().getAbsolutePath()+ "/temp.png";
        }
        out.write(baos.toByteArray());
        out.flush();
        out.close();
    }

    private class Upload extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            return UploadImage.formUpload("http://123.206.195.52:8080/day_30/uploadServlet",path, User.get(getActivity().getApplicationContext()).getName(),getActivity());
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.contains("上传成功")) Toast.makeText(getActivity(),"头像上传成功",Toast.LENGTH_SHORT).show();
            if(tempFile.exists()){
                tempFile.delete();
            }
            Glide.get(getActivity()).clearMemory();
            SettingLab.getSettingLab(getActivity().getApplicationContext()).setTouxiangUpdate(true);
        }
    }

    private class getUserInfo extends AsyncTask<Void,Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            return UserUtil.getUserInfo(6,User.get(getActivity().getApplicationContext()).getName());
        }

        @Override
        protected void onPostExecute(String info) {
            Log.i("UserinfoFragment","info:"+info.split("\\|d\\|").length);
            if (info != null&&info.split("\\|d\\|").length>1) {
                String result[]=info.split("\\|d\\|");
                String signature=result[2];
                Log.i("UserInfoFragment",signature);
                String nick=result[1];
                String concern=result[3];
                String travel=result[4];
                String follower=result[5];
                signatureTV.setText(signature);
                nickTv.setText(nick);
                concernTV.setText(concern);
                travelTv.setText(travel);
                followerTV.setText(follower);
            }else {
                Toast.makeText(getActivity(),"连接服务器失败，稍后自动获取",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class scenicAdapter extends RecyclerView.Adapter{
        ArrayList<Scenicspot> scenicspots;

        public scenicAdapter() {
            super();
            scenicspots=collectScenicspots;
            Log.i(TAG, "景点数:" + scenicspots.size());
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new scenicAdapter.scenicHolder(LayoutInflater.from(getActivity()).inflate(R.layout.journey_day_travel_item,parent,false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            scenicAdapter.scenicHolder scenicHolder=(scenicAdapter.scenicHolder)holder;
            final Scenicspot scenicspot=scenicspots.get(position);
            scenicHolder.scenicName.setText(scenicspots.get(position).getScenicName());
            scenicHolder.scenicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ScenicDetailActivity.class);
                    intent.putExtra(ScenicDetailActivity.EXTRA_SCENIC_SER,scenicspot);
                    ActivityOptions activityOptions=null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        android.util.Pair<View, String> pair[] = new android.util.Pair[1];
                        pair[0] = new android.util.Pair<View, String>(((scenicAdapter.scenicHolder) holder).scenicName, "scenic_name");
                        activityOptions= ActivityOptions.makeSceneTransitionAnimation(getActivity(),pair);
                    }
                    startActivity(intent,activityOptions.toBundle());
                }
            });
            scenicHolder.scenicName.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i(TAG,"长按");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示");
                    builder.setMessage("是否从收藏删除"+scenicspot.getScenicName());
                    builder.setIcon(R.drawable.circle);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScenicspotLab.get(getActivity()).getCollectionspots().remove(scenicspot);
                            notifyItemRemoved(position);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
            scenicHolder.addIcon.setVisibility(View.GONE);
            scenicHolder.goIcon.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return scenicspots.size();
        }

        private class scenicHolder extends RecyclerView.ViewHolder{
            private TextView scenicName;
            ImageView addIcon;
            ImageView goIcon;
            RelativeLayout travelItemLayout;
            public scenicHolder(View itemView) {
                super(itemView);
                scenicName=(TextView)itemView.findViewById(R.id.journey_day_travel_item_name);
                addIcon=(ImageView)itemView.findViewById(R.id.journey_day_travel_item_add);
                goIcon = (ImageView) itemView.findViewById(R.id.journey_day_travel_item_go);
                travelItemLayout=(RelativeLayout)itemView.findViewById(R.id.travel_item_layout);
            }
        }
    }
}
