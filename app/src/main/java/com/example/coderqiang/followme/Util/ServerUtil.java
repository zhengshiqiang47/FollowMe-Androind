package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Interface.GetDynamicInterface;
import com.example.coderqiang.followme.Interface.ImgJsonIterface;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicComment;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.TravleDay;
import com.example.coderqiang.followme.Model.TravlePlan;
import com.example.coderqiang.followme.Model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2017/2/27.
 */

public class ServerUtil {
    private static final String TAG="ServerUtil";
    private static final String TYPE_GET="1";
    public static final String LOAD_SUCCESS = "loadSuccess";
    public static final String LOAD_OTHERCITY = "loadOtherCity";

    private static String baseUrl="http://123.206.195.52:8080/day_30/";
    private static String local="http://localhost:8080/day_30/";
    private static int index=0;

    public static void getDynamic(Context context,FMUser user){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetDynamicInterface getDynamicInterface=retrofit.create(GetDynamicInterface.class);
        Call<ArrayList<Dynamic>> call=getDynamicInterface.getDyanmic(TYPE_GET,user.getId()+"",user.getDynamicIndex());
        try {
            ArrayList<Dynamic> dynamics=call.execute().body();
            DynamicLab.get(context.getApplicationContext()).getDynamics().addAll(dynamics);
            for (Dynamic dynamic:dynamics){
                ArrayList<DynamicImage> dynamicImages=new Gson().fromJson(dynamic.getImageName(),new TypeToken<ArrayList<DynamicImage>>() {}.getType());
//                ArrayList<String> urls=new ArrayList<>();
//                for (DynamicImage dynamicImage:dynamicImages){
//                    String url="http://123.206.195.52:8080/day_30/dynamicImg/"+dynamicImage.getName();
//                    urls.add(url);
//                }
//                dynamic.setImageUrls(urls);
                dynamic.setDynamicImages(dynamicImages);
            }
            if(dynamics==null||dynamics.size()==0) return;
            user.addDynamicIndex();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
    }

    public static ArrayList<Dynamic> getDynamicMine(Context context,FMUser user){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "6")
                .add("userid",user.getId()+"");
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String json=new String(response.body().bytes(),"utf-8");
            ArrayList<Dynamic> dynamics = new Gson().fromJson(json,new TypeToken<ArrayList<Dynamic>>() {}.getType());
            for (Dynamic dynamic:dynamics){
                ArrayList<DynamicImage> dynamicImages=new Gson().fromJson(dynamic.getImageName(),new TypeToken<ArrayList<DynamicImage>>() {}.getType());
//                ArrayList<String> urls=new ArrayList<>();
//                for (DynamicImage dynamicImage:dynamicImages){
//                    String url="http://123.206.195.52:8080/day_30/dynamicImg/"+dynamicImage.getName();
//                    urls.add(url);
//                }
//                dynamic.setImageUrls(urls);
                dynamic.setDynamicImages(dynamicImages);
            }
            return dynamics;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static int uploadDynamic(Context context,Dynamic dynamic){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        Log.i(TAG,"latitude"+dynamic.getLatitude());
        form.add("type","2")
            .add("userid", dynamic.getUserID()+"")
            .add("username",dynamic.getUserName())
            .add("content",dynamic.getContent())
            .add("timestamp",dynamic.getTimeStamp()+"")
            .add("imageCount",dynamic.getImageCount()+"")
            .add("address",dynamic.getAddress())
            .add("latitude",dynamic.getLatitude()+"")
            .add("longtitude",dynamic.getLongtitude()+"");
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i(TAG,"上传成功"+result);
            int id=Integer.parseInt(result);
            if(id!=0)
                dynamic.setDynamicID(id);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int uploadDynamicImageName(Context context,String imageName,int id){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("type","3")
            .add("id",id+"")
            .add("imagename",imageName);
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i(TAG,"上传成功"+result);
            int resid=Integer.parseInt(result);
            return resid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int uploadDynamicComment(Context context,DynamicComment dynamicComment){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("type","4")
                .add("dynamicid",dynamicComment.getDynamicId()+"")
                .add("commenterid",dynamicComment.getCommenterId()+"")
                .add("userid",dynamicComment.getUserId()+"")
                .add("commentername",dynamicComment.getCommenterName()+"")
                .add("content",dynamicComment.getContent())
                .add("timestamp",dynamicComment.getTimestamp()+"");
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i(TAG,"评论成功"+result);
            int resid=Integer.parseInt(result);
            dynamicComment.setCommentId(resid);
            return resid;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void getAllCity(Context context){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "4");
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"scenicSpotServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            ArrayList<City> cities=new Gson().fromJson(res,new TypeToken<ArrayList<City>>() {}.getType());
            for (City city:cities){
                city.setIamgeUrls(MyStringUtil.CommentImageParse(city.getImageUrl()));
            }
            CityLab.get(context).getCities().clear();
            CityLab.get(context).getCities().addAll(cities);
            Log.i(TAG, "CityParse OK");
//            String images=comment.getImgStr();
//            images=images.replaceAll("\\|","");
//            String[] imageList=images.split("https://");
//            System.out.println("size"+imageList.length);
//            for (int i = 1; i < imageList.length; i++) {
//                System.out.println("https://"+imageList[i]);
//            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static ArrayList<Scenicspot> getScenicSpot(String cityname, int page, Context context, final String msg){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("page",""+page)
                .add("type", "1")
                .add("cityname", cityname);
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"scenicSpotServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            ArrayList<Scenicspot> scenicspots=new Gson().fromJson(res,new TypeToken<ArrayList<Scenicspot>>() {}.getType());
            for (Scenicspot scenicspot : scenicspots) {
                final Scenicspot tempSce=scenicspot;
                Log.i(TAG,"scenicspotId"+scenicspot.getId()+ "scenicspot" + scenicspot.getScenicName() + "-" + scenicspot.getBrightPoint() + "-");
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        getScenicComment(tempSce);
                        getScenicImage(tempSce);
                        Log.i(TAG,"-----");
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {

                    @Override
                    public void onCompleted() {
                        index++;
                        if(index==10){
                            EventBus.getDefault().post(msg);
                            index=0;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });

            }
            City city=CityLab.get(context).isContain(cityname);
            city.getScenicspots().addAll(scenicspots);
            city.addscenicPage();
//            String images=comment.getImgStr();
//            images=images.replaceAll("\\|","");
//            String[] imageList=images.split("https://");
//            System.out.println("size"+imageList.length);
//            for (int i = 1; i < imageList.length; i++) {
//                System.out.println("https://"+imageList[i]);
//            }
            return scenicspots;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    public static boolean getScenicComment(Scenicspot scenicspot){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("page",scenicspot.getPageNum()+"")
                .add("type", "2")
                .add("scenicid", ""+scenicspot.getId());
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"scenicSpotServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            List<Comment> comments=new Gson().fromJson(res,new TypeToken<ArrayList<Comment>>() {}.getType());
            for (Comment comment:comments){
                comment.setImagelist(MyStringUtil.CommentImageParse(comment.getImages()));
                comment.setImgSmalslist(MyStringUtil.CommentImageParse(comment.getImgSmall()));
            }
            scenicspot.getComments().clear();
            scenicspot.getComments().addAll(comments);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getScenicImage(Scenicspot scenicspot){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        Log.i(TAG, "scenicId:" + scenicspot.getId());
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "3")
                .add("scenicid", ""+scenicspot.getId());
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"scenicSpotServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String res=new String(response.body().bytes(),"utf-8");
            List<ScenicImg> scenicImgs=new Gson().fromJson(res,new TypeToken<ArrayList<ScenicImg>>() {}.getType());
            if(scenicImgs==null||scenicImgs.size()==0){
                ScenicImg img=new ScenicImg();
                img.setBigImgUrl(baseUrl+"dynamicImg/loadingbg.jpg");
                img.setSmallImgUrl(baseUrl+"dynamicImg/loadingbg.jpg");
                img.setId(0);
                scenicspot.getImgUrls().add(img);
            }
//            ScenicImg scenicImg=scenicImgs.get(0);
//            Log.i(TAG,"imageSize:"+scenicImgs.size()+"-"+scenicImg.getBigImgUrl()+"-"+scenicImg.getSmallImgUrl()+"-"+scenicImg.getWidth()+"-"+scenicImg.getScenicId());
            scenicspot.getImgUrls().addAll(scenicImgs);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static int uploadTravelPlan(Context context, TravlePlan plan){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("type","8").add("userid",plan.getUserId()+"")
            .add("name",plan.getTravleName())
            .add("city",plan.getCityName())
            .add("time",plan.getTime()+"")
            .add("daycount",plan.getDayCount()+"")
            .add("memo",plan.getBeginMemo())
            .add("travleday",new Gson().toJson(plan.getTravleDays()));
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i(TAG,"上传成功"+result);
            int id=Integer.parseInt(result);
            if(id!=0)
                plan.setId(id);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int updateTravelPlan(Context context, TravlePlan plan){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("type","9").add("planid",plan.getId()+"")
                .add("daycount",plan.getDayCount()+"")
                .add("travleday",new Gson().toJson(plan.getTravleDays()));
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            String result = new String(response.body().bytes());
            Log.i(TAG,"上传成功"+result);
            int id=Integer.parseInt(result);
            if(id!=0)
                plan.setId(id);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<TravlePlan> getTravlePlan(Context context,int userid){
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        FormBody.Builder form=new FormBody.Builder()
                .add("type", "7")
                .add("userid",userid+"");
        RequestBody requestBody=form.build();
        Request request=new Request.Builder()
                .url(baseUrl+"dynamicServlet")
                .post(requestBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            Response response=okHttpClient.newCall(request).execute();
            String json=new String(response.body().bytes(),"utf-8");
            ArrayList<TravlePlan> TravlePlans = new Gson().fromJson(json,new TypeToken<ArrayList<TravlePlan>>() {}.getType());

            for (TravlePlan travlePlan:TravlePlans){
                Log.i(TAG,"travleplan"+travlePlan.getTravleDaysStr());
                ArrayList<TravleDay> travleDays=new Gson().fromJson(travlePlan.getTravleDaysStr(),new TypeToken<ArrayList<TravleDay>>() {}.getType());
//                ArrayList<String> urls=new ArrayList<>();
//                for (TravlePlanImage TravlePlanImage:TravlePlanImages){
//                    String url="http://123.206.195.52:8080/day_30/TravlePlanImg/"+TravlePlanImage.getName();
//                    urls.add(url);
//                }
//                TravlePlan.setImageUrls(urls);
                travlePlan.setTravleDays(travleDays);
                if(travleDays!=null)
                    Log.i(TAG,"travleDays:"+travleDays.size());
            }

            return TravlePlans;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
