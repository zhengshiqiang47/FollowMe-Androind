package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Interface.CityInterface;
import com.example.coderqiang.followme.Interface.DynamicInterface;
import com.example.coderqiang.followme.Interface.ScenicInterface;
import com.example.coderqiang.followme.Interface.TravelPlanInterface;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Dynamic;
import com.example.coderqiang.followme.Model.DynamicComment;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.Model.DynamicLab;
import com.example.coderqiang.followme.Model.FMUser;
import com.example.coderqiang.followme.Model.Paging;
import com.example.coderqiang.followme.Model.ResultDTO;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.TravelDay;
import com.example.coderqiang.followme.Model.TravelPlan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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

/**
 * Created by CoderQiang on 2017/2/27.
 */

public class ServerUtil {
    private static final String TAG="ServerUtil";
    private static final String TYPE_GET="1";
    public static final String LOAD_SUCCESS = "loadSuccess";
    public static final String LOAD_OTHERCITY = "loadOtherCity";

    public final static String BASE_URL ="http://10.0.2.2:8080/";
    private static int index=0;

    public static void getDynamic(Context context,FMUser user){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DynamicInterface dynamicInterface =retrofit.create(DynamicInterface.class);
        Call<ResultDTO<Paging<Dynamic>>> call= dynamicInterface.queryDynamic(user.getId(),user.getDynamicIndex(),20);
        try {
            Paging<Dynamic> dynamicPaging = call.execute().body().getData();
            List<Dynamic> dynamicList = dynamicPaging.getList();
            DynamicLab.get(context.getApplicationContext()).getDynamics().addAll(dynamicPaging.getList());
            if (dynamicList == null || dynamicList.size() == 0) {
                return;
            }
            for (Dynamic dynamic : dynamicList) {
                ArrayList<DynamicImage> dynamicImages = new Gson().fromJson(dynamic.getImageName(), new TypeToken<ArrayList<DynamicImage>>() {}.getType());
                dynamic.setDynamicImages(dynamicImages);
            }
            user.addDynamicIndex();
        } catch (Exception e) {
            Log.e(TAG, "getDynamic_exp", e);
        }
    }

    public static ArrayList<Dynamic> getDynamicMine(Context context,FMUser user){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DynamicInterface dynamicInterface =retrofit.create(DynamicInterface.class);
            Call<ResultDTO<Paging<Dynamic>>> call= dynamicInterface.queryDynamic(user.getId(),user.getDynamicIndex(),20);
            try {
                List<Dynamic> dynamics = call.execute().body().getData().getList();
                for (Dynamic dynamic : dynamics) {
                    ArrayList<DynamicImage> dynamicImages = new Gson().fromJson(dynamic.getImageName(), new TypeToken<ArrayList<DynamicImage>>() {}.getType());
                    dynamic.setDynamicImages(dynamicImages);
                }
            return new ArrayList<>(dynamics);
        } catch (Exception e) {
            Log.e(TAG, "dynamicMine", e);
        }
        return new ArrayList<>();
    }

    public static int uploadDynamic(Context context,Dynamic dynamic){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DynamicInterface dynamicInterface =retrofit.create(DynamicInterface.class);
        Call<ResultDTO<Dynamic>> call= dynamicInterface.createDynamic(dynamic);
        try {
            return call.execute().body().getData().getDynamicID();
        } catch (Exception e) {
            Log.e(TAG,"uploadDynamic_exp", e);
        }
        return 0;
    }

    public static int uploadDynamicImageName(Context context,String imageName,int id){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
        form.add("type","3")
            .add("id",id+"")
            .add("imagename",imageName);
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(BASE_URL +"dynamicServlet")
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
                .url(BASE_URL +"dynamicServlet")
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CityInterface cityInterface = retrofit.create(CityInterface.class);
        try {
            List<City> cities = cityInterface.allCity().execute().body().getData();
            for (City city:cities){
                city.setIamgeUrls(MyStringUtil.CommentImageParse(city.getImageUrl()));
            }
            CityLab.get(context).getCities().clear();
            CityLab.get(context).getCities().addAll(cities);
            Log.i(TAG, "CityParse OK");
        } catch (Exception e) {
            Log.i(TAG, "CityParse ERROR",e);
        }
    }

    public static List<Scenicspot> getScenicSpot(String cityname, int page, Context context, final String msg){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ScenicInterface scenicInterface = retrofit.create(ScenicInterface.class);
        try {
            Paging<Scenicspot> paging = scenicInterface.queryScenicSpot(cityname,page,20).execute().body().getData();
            List<Scenicspot> scenicspots = paging.getList();
            for (Scenicspot scenicspot : scenicspots) {
                getScenicComment(scenicspot);
                getScenicImage(scenicspot);
            }
            City city = CityLab.get(context).isContain(cityname);
            city.getScenicspots().addAll(scenicspots);
            city.addscenicPage();
            return scenicspots;
        } catch (Exception e) {
            Log.e(TAG, "getScenicSpot_exp", e);
        }
        return null;
    }

    public static boolean getScenicComment(Scenicspot scenicspot){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ScenicInterface scenicInterface = retrofit.create(ScenicInterface.class);
        try {
            Paging<Comment> paging = scenicInterface.queryScenicComment(scenicspot.getId(), 1, 200).execute().body().getData();
            List<Comment> comments = paging.getList();
            for (Comment comment:comments){
                comment.setImagelist(MyStringUtil.CommentImageParse(comment.getImages()));
                comment.setImgSmalslist(MyStringUtil.CommentImageParse(comment.getImgSmall()));
            }
            scenicspot.getComments().clear();
            scenicspot.getComments().addAll(comments);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "getScenicComment_exp", e);
        }
        return false;
    }

    public static boolean getScenicImage(Scenicspot scenicspot){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ScenicInterface scenicInterface = retrofit.create(ScenicInterface.class);
        try {
            Paging<ScenicImg> paging = scenicInterface.queryScenicImg(scenicspot.getId(), 1, 200).execute().body().getData();
            List<ScenicImg> scenicImgs = paging.getList();
            if (scenicImgs == null || scenicImgs.size() == 0) {
                ScenicImg img = new ScenicImg();
                img.setBigImgUrl(BASE_URL + "dynamicImg/loadingbg.jpg");
                img.setSmallImgUrl(BASE_URL + "dynamicImg/loadingbg.jpg");
                img.setId(0);
                scenicspot.getImgUrls().add(img);
            }
            scenicspot.getImgUrls().addAll(scenicImgs);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "getScenicImage_exp", e);
        }
        return false;
    }

    public static int uploadTravelPlan(Context context, TravelPlan plan){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TravelPlanInterface travelPlanInterface =retrofit.create(TravelPlanInterface.class);
        Call<ResultDTO<TravelPlan>> call= travelPlanInterface.createTravelPlan(plan);
        try {
            return call.execute().body().getData().getId();
        } catch (Exception e) {
            Log.e(TAG,"uploadDynamic_exp", e);
        }
        return 0;
    }

    public static int updateTravelPlan(Context context, TravelPlan plan){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("type","9").add("planid",plan.getId()+"")
                .add("daycount",plan.getDayCount()+"")
                .add("travleday",new Gson().toJson(plan.getTravelDays()));
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(BASE_URL +"dynamicServlet")
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

    public static List<TravelPlan> getTravelPlan(Context context, int userid){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TravelPlanInterface travelPlanInterface =retrofit.create(TravelPlanInterface.class);
        Call<ResultDTO<Paging<TravelPlan>>> call= travelPlanInterface.queryTravelPlan(userid,1,20);
        try {
            Paging<TravelPlan> travelPlanPaging = call.execute().body().getData();
            List<TravelPlan> travelPlans = travelPlanPaging.getList();
            for (TravelPlan travelPlan : travelPlans){
                Log.i(TAG,"travleplan"+ travelPlan.getTravleDaysStr());
                ArrayList<TravelDay> travelDays =new Gson().fromJson(travelPlan.getTravleDaysStr(),new TypeToken<ArrayList<TravelDay>>() {}.getType());
                travelPlan.setTravelDays(travelDays);
                if(travelDays !=null)
                    Log.i(TAG,"travleDays:"+ travelDays.size());
            }

            return travelPlans;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
