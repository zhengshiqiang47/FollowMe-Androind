package com.example.coderqiang.followme.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.coderqiang.followme.R;
import com.example.coderqiang.followme.View.MyPhotoView;

import net.qiujuer.genius.graphics.Blur;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBulgeDistortionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageColorBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGlassSphereFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageRGBDilationFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/3/1.
 */

public class ImageFilterActivity extends Activity {

    @Bind(R.id.imagefilter_cancle)
    ImageView imagefilterCancle;
    @Bind(R.id.imagefilter_right)
    ImageView imagefilterRight;
    @Bind(R.id.imagefilter_title)
    LinearLayout imagefilterTitle;
    @Bind(R.id.imagefilter_image)
    MyPhotoView image;
    @Bind(R.id.imagefilter_normal)
    ImageView normalImage;
    @Bind(R.id.imagefilter_sepiafilter)
    ImageView sepiaImage;
    @Bind(R.id.imagefilter_filter2)
    ImageView imagefilterFilter2;
    @Bind(R.id.imagefilter_filter3)
    ImageView imagefilterFilter3;
    @Bind(R.id.imagefilter_filter4)
    ImageView imagefilterFilter4;
    @Bind(R.id.imagefilter_filter5)
    ImageView imagefilterFilter5;
    @Bind(R.id.imagefilter_filter6)
    ImageView imagefilterFilter6;
    @Bind(R.id.imagefilter_filter7)
    ImageView imagefilterFilter7;
    @Bind(R.id.imagefilter_bg)
    ImageView imageFilterBg;

    int position;
    Activity activity;
    Uri uri;
    Bitmap normal;
    Bitmap bitmap;
    Bitmap bg;
    Bitmap sepiaBitmp;
    Bitmap filter2;
    Bitmap filter3;
    Bitmap filter4;
    Bitmap filter5;
    Bitmap filter6;
    Bitmap filter7;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefilter);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.journey_green));
        position=getIntent().getIntExtra("position",0);
        //底部导航栏
        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        ButterKnife.bind(this);
        activity=this;
        uri = Uri.parse(getIntent().getStringExtra("uri"));
        getBitmap();
        initView();
        showFilter();
    }
    /**
     * GPUImageBrightnessFilter()   增加光亮度
     * GPUImageColorBlendFilter()   灰色
     * GPUImageBulgeDistortionFilter()  中间凸起
     * GPUImageContrastFilter() 对比度
     * GPUImageColorInvertFilter() 胶片
     * GPUImageSepiaFilter 褐色
     * GPUImageSketchFilter 漫画
     * GPUImageSobelEdgeDetection() 边缘检测
     * GPUImageGlassSphereFilter() 鱼眼效果
     * GPUImageEmbossFilter() 浮雕
     * GPUImageHazeFilter() 朦胧加暗
     * GPUImageRGBDilationFilter 水晶模糊效果
     * GPUImageSmoothToonFilter() 卡通效果
     * GPUImageKuwaharaFilter() 桑原效果，(油画)
     *
     *  float[] a={0,0,0};
     *  gpuImage.setFilter(new GPUImageVignetteFilter(new PointF(0.5f,0.5f),a,0.0f,1.0f)); 边缘黑效果
     *
     */

    private void showFilter() {
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    GPUImage gpuImage = new GPUImage(activity);
                    gpuImage.setFilter(new GPUImageKuwaharaFilter());
                    gpuImage.setImage(bitmap);
                    sepiaBitmp = gpuImage.getBitmapWithFilterApplied();
                    subscriber.onNext(1);

                    gpuImage.setFilter(new GPUImageHazeFilter());
                    gpuImage.setImage(bitmap);
                    filter2=gpuImage.getBitmapWithFilterApplied();
                    subscriber.onNext(2);

                    try {
                        gpuImage.setFilter(new GPUImageSketchFilter());
                        gpuImage.setImage(bitmap);
                        filter3=gpuImage.getBitmapWithFilterApplied();
                        subscriber.onNext(3);
                    }catch (Exception e){
                        filter3=normal;
                    }


                    try {
                        gpuImage.setFilter(new GPUImageColorBlendFilter());
                        gpuImage.setImage(bitmap);
                        filter4=gpuImage.getBitmapWithFilterApplied();
                        subscriber.onNext(4);
                    }catch (Exception e){
                        filter4 = normal;
                    }

                    gpuImage.setFilter(new GPUImageSepiaFilter());
                    gpuImage.setImage(bitmap);
                    filter5=gpuImage.getBitmapWithFilterApplied();
                    subscriber.onNext(5);

                    gpuImage.setFilter(new GPUImageRGBDilationFilter());
                    gpuImage.setImage(bitmap);
                    filter6=gpuImage.getBitmapWithFilterApplied();
                    subscriber.onNext(6);


                    gpuImage.setFilter(new GPUImageBrightnessFilter());
                    gpuImage.setImage(bitmap);
                    filter7=Bitmap.createBitmap(bitmap);
                    filter7=compressImage(filter7,100);
                    filter7=Blur.onStackBlur(filter7,10);
                    subscriber.onNext(7);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer i) {
                switch (i){
                    case 1:
                        bg=Bitmap.createBitmap(bitmap);
                        bg=compressImage(bg,100);
                        Bitmap filterBg=Blur.onStackBlur(bg,150);
                        imageFilterBg.setImageBitmap(filterBg);
                        sepiaImage.setImageBitmap(sepiaBitmp);break;
                    case 2:
                        imagefilterFilter2.setImageBitmap(filter2);break;
                    case 3:
                        imagefilterFilter3.setImageBitmap(filter3);break;
                    case 4:
                        imagefilterFilter4.setImageBitmap(filter4);break;
                    case 5:
                        imagefilterFilter5.setImageBitmap(filter5);break;
                    case 6:
                        imagefilterFilter6.setImageBitmap(filter6);break;
                    case 7:
                        imagefilterFilter7.setImageBitmap(filter7);break;
                }
            }
        });

    }

    private void initView() {
        try {
            getBitmap();
            image.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @OnClick({R.id.imagefilter_normal,R.id.imagefilter_cancle,R.id.imagefilter_right,R.id.imagefilter_sepiafilter, R.id.imagefilter_filter2, R.id.imagefilter_filter3, R.id.imagefilter_filter4, R.id.imagefilter_filter5, R.id.imagefilter_filter6, R.id.imagefilter_filter7})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagefilter_cancle:
                onBackPressed();
                break;
            case R.id.imagefilter_right:
                Uri uri=saveBitmap();
                Intent intent=new Intent();
                intent.putExtra("uri",uri.toString());
                intent.putExtra(NewDynamicActivity.EXTRA_POSITION,position);
                setResult(NewDynamicActivity.RESULT_OK,intent);
                finish();
                break;
            case R.id.imagefilter_normal:
                bitmap=normal;
                image.setImageBitmap(normal);
                break;
            case R.id.imagefilter_sepiafilter:
                bitmap=sepiaBitmp;
                image.setImageBitmap(sepiaBitmp);
                break;
            case R.id.imagefilter_filter2:
                bitmap=filter2;
                image.setImageBitmap(filter2);
                break;
            case R.id.imagefilter_filter3:
                bitmap=filter3;
                image.setImageBitmap(filter3);
                break;
            case R.id.imagefilter_filter4:
                bitmap=filter4;
                image.setImageBitmap(filter4);
                break;
            case R.id.imagefilter_filter5:
                bitmap=filter5;
                image.setImageBitmap(filter5);
                break;
            case R.id.imagefilter_filter6:
                bitmap = filter6;
                image.setImageBitmap(filter6);
                break;
            case R.id.imagefilter_filter7:
                bitmap=filter7;
                image.setImageBitmap(filter7);
                break;
        }
    }

    private Uri saveBitmap() {
        String imageFilePath=null;
        imageFilePath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null);
        Log.i("", "生成的照片输出路径：" + imageFilePath.toString());
        return Uri.parse(imageFilePath);
    }


    private Bitmap compressImage(Bitmap image,int size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 30;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        BitmapFactory.Options options1 = new BitmapFactory.Options();
        options1.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, options1);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public void getBitmap() {
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int height=options.outHeight*1080/options.outWidth;
        options.inJustDecodeBounds=false;
        Log.i("filter","outwidth"+options.outWidth+" size:"+options.outHeight/height);
        options.inSampleSize=options.outHeight/height;
        options.inDither=false;    /*不进行图片抖动处理*/
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri),null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        normal=bitmap;
        normalImage.setImageBitmap(bitmap);
        bitmap=compressImage(bitmap,500);
        image.setImageBitmap(bitmap);
    }
}
