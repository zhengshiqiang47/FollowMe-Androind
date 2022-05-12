package com.example.coderqiang.followme.Fragment;

import static com.example.coderqiang.followme.Util.ServerUtil.BASE_URL;

import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.coderqiang.followme.Activity.EditUserInfoActivity;
import com.example.coderqiang.followme.Activity.LoginActivity;
import com.example.coderqiang.followme.Activity.NearbyActivity;
import com.example.coderqiang.followme.Activity.ScenicDetailActivity;
import com.example.coderqiang.followme.Activity.ShowTravlePlanAcvitity;
import com.example.coderqiang.followme.CircleImagview;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.FriendsLab;
import com.example.coderqiang.followme.Model.MyLocation;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.SettingLab;
import com.example.coderqiang.followme.Model.User;
import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.Util.UploadImage;
import com.example.coderqiang.followme.Util.UserUtil;
import com.hyphenate.chat.EMClient;

import net.qiujuer.genius.graphics.Blur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    TextView signatureTV;
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
    @Bind(R.id.userinfo_title_bg)
    ImageView userInfoTitlebg;
    @Bind(R.id.userinfo_address)
    TextView address;
    @Bind(R.id.userinfo_my_travel)
    TextView myTravel;
    @Bind(R.id.userinfo_sex)
    ImageView sex;
    @Bind(R.id.userinfo_set)
    TextView setTextview;
    @Bind(R.id.userinfo_travelplan)
    LinearLayout myTravlePlan;
    @Bind(R.id.userinfo_nearby)
    LinearLayout nearby;

    private static final int PHOTO_REQUEST = 1;
    private static final int PHOTO_REQUEST_CUT=2;
    private static final int PHOTO_REQUEST_CAREMA = 3;
    private ArrayList<Scenicspot> collectScenicspots;
    private String friendSize="";
    File tempFile;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    String path;
    Uri saveUri;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uerinfo_detail_2, container, false);
        ButterKnife.bind(this, v);
        initView();
        if(User.get(getActivity()).getFmUser()==null) {
            new getUserInfo().execute();
        } else {
            updateUserInfo(User.get(getActivity()).getFmUser());
        }
        myMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
            }
        });

        getFriend();
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
        myTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), HistoryTrackActivity.class);
//                startActivity(intent);
                Toast.makeText(getActivity(),"敬请期待",Toast.LENGTH_SHORT).show();
            }
        });
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
        setTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
        myTravlePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowTravlePlanAcvitity.class);
                startActivity(intent);
            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearbyActivity.class);
                startActivity(intent);
            }
        });
        address.setText(MyLocation.getMyLocation(getActivity()).getCityName());
        concernTV.setText(FriendsLab.get(getActivity().getApplicationContext()).getFMUsers().size()+"");
        showTitleBg();
    }

    private void showTitleBg() {
        Glide.with(getActivity()).load(BASE_URL + "upload/"+ User.get(getActivity()).getName()+".png").asBitmap().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Bitmap bitmap=compressImage(resource);
                bitmap= Blur.onStackBlur(bitmap,60);
                userInfoTitlebg.setImageBitmap(bitmap);
            }
        });
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options1);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    private void showCollect(){
        try {
            collectScenicspots= ScenicspotLab.get(getActivity().getApplicationContext()).getCollectionspots();
            if (collectScenicspots.size() == 0) {
                collectScenicspots.add(CityLab.get(getActivity()).getCurrentCity().getScenicspots().get(5));
            }
            Log.i(TAG, "收藏个数" + collectScenicspots.size());
            collectRecycler.setVisibility(View.VISIBLE);
            collectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            collectRecycler.setAdapter(new scenicAdapter());
            TranslateAnimation translate=new TranslateAnimation(collectRecycler.getWidth(),0,0,0);
            translate.setDuration(800);
            translate.setFillAfter(true);
            collectRecycler.startAnimation(translate);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity(),"暂无收藏",Toast.LENGTH_SHORT).show();
        }


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
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection",true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,saveUri);
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
                Uri selectedImage = data.getData();
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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

    public void getFriend() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                ArrayList<FMUser> fmUsers=UserUtil.getFriend(User.get(getActivity().getApplicationContext()).getFmUser().getId());
                friendSize=fmUsers.size()+"";
                FriendsLab.get(getActivity().getApplicationContext()).setFMUsers(fmUsers);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

            @Override
            public void onCompleted() {
                concernTV.setText(friendSize);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    private class Upload extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... params) {
            return UploadImage.formUpload(BASE_URL + "uploadServlet",path, User.get(getActivity().getApplicationContext()).getName(),getActivity());
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

    private class getUserInfo extends AsyncTask<Void,Void,Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return UserUtil.getUser(getActivity(),User.get(getActivity().getApplication()).getName());
        }

        @Override
        protected void onPostExecute(Boolean info) {

            FMUser fmUser=User.get(getActivity().getApplicationContext()).getFmUser();
            Log.i("UserinfoFragment","info:"+info+fmUser.getSignature());
            updateUserInfo(fmUser);
//            if (info != null&&info.split("\\|d\\|").length>1) {
//                String result[]=info.split("\\|d\\|");
//                String signature=result[2];
//                Log.i("UserInfoFragment",signature);
//                String nick=result[1];
//                String concern=result[3];
//                String travel=result[4];
//                String follower=result[5];
//                signatureTV.setText(signature);
//                nickTv.setText(nick);
//                concernTV.setText(concern);
//                travelTv.setText(travel);
//                followerTV.setText(follower);
//            }else {
//                Toast.makeText(getActivity(),"连接服务器失败，稍后自动获取",Toast.LENGTH_SHORT).show();
//            }
        }


    }
    private void updateUserInfo(FMUser fmUser) {
        signatureTV.setText(fmUser.getSignature());
        nickTv.setText(fmUser.getNickName());
        concernTV.setText(fmUser.getConcern()+"");
        travelTv.setText(fmUser.getTravle()+"");
        followerTV.setText(fmUser.getFollower()+"");
        if(fmUser.getSex()==0)
        sex.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_man_white));
        else sex.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_woman_white));
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
