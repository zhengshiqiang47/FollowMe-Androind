package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;

import com.example.coderqiang.followme.Interface.BaiduImageInterface;
import com.example.coderqiang.followme.Interface.ImgJsonIterface;
import com.example.coderqiang.followme.Interface.WeatherInterface;
import com.example.coderqiang.followme.Model.BaiduMapImage;
import com.example.coderqiang.followme.Model.City;
import com.example.coderqiang.followme.Model.CityLab;
import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.ScenicImg;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;
import com.example.coderqiang.followme.Model.Weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class HttpParse {
    private static final String TAG = "HttpParse";
    private static final String APP_KEY="e6e8f3668fc7f4817d9738489cf1d738";

    public ArrayList<Scenicspot> getScenicspot(Context context,String cityname,String pageNum){
        ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(context).getScenicspots();

        HttpAnalyze httpAnalyze=new HttpAnalyze();
        City city=new City();
        if (cityname != null) {
            city=CityLab.get(context).isContain(cityname);
        }else {
            city.setCityName("杭州");
            city.setProvinceName("浙江");
            city.setCtripId("hangzhou14");
            cityname = "杭州";
        }
        ArrayList<Scenicspot> citySpots=city.getScenicspots();
        if(Integer.parseInt(pageNum)<city.getCountPage()){
            return citySpots;
        }
        String result=httpAnalyze.getHtml("http://you.ctrip.com/searchsite/Sight?query="+cityname);
        Document document;
        try {
            document= Jsoup.parse(result);
            Element allSightEle=document.select("a[class=fr]").get(0);
            String allUrl=allSightEle.attr("href");
            String pageUrl=allUrl.split(".html")[0]+"/s0-p"+pageNum+".html";
            String sightResult=httpAnalyze.getHtml("http://you.ctrip.com/"+pageUrl);
            Log.i(TAG, pageUrl + "");
            Document sightDoc=Jsoup.parse(sightResult);
            Elements sightElems=sightDoc.select("div[class=list_mod2]");
            for (int i=0;i<sightElems.size();i++) {
                Element element=sightElems.get(i);
                String imgUrl=element.select("div[class=leftimg]").select("a").select("img").attr("src");
//                Log.i(TAG, "imgUrl:" + imgUrl);
                String name=element.select("div[class=rdetailbox]").select("dl").select("dt").select("a[target=_blank]").text();
                String url="http://you.ctrip.com"+element.select("div[class=rdetailbox]").select("dl").select("dt").select("a[target=_blank]").attr("href");
                String rank=element.select("div[class=rdetailbox]").select("dl").select("dt").select("s").text();
                String addr=element.select("div[class=rdetailbox]").select("dl").select("dd").get(0).text();
                String manyA=element.select("div[class=rdetailbox]").select("dl").select("dd").get(1).text();
                if(manyA.contains("A级景区")){
                    manyA=manyA.split("景区")[0];
                }else manyA=" ";
                String mark=element.select("div[class=rdetailbox]").select("ul[class=r_comment]").select("li").select("a[class=score]").text();
                String commentCount = element.select("div[class=rdetailbox]").select("ul[class=r_comment]").select("li").select("a[class=recomment]").text();
//                Log.i(TAG,"name:"+name);
//                Log.i(TAG,"url:"+url);
//                Log.i(TAG,"rank:"+rank);
//                Log.i(TAG,"addr:"+addr);
//                Log.i(TAG,"manyA:"+manyA);
//                Log.i(TAG,"mark:"+mark);
//                Log.i(TAG, "commentCount" + commentCount);
//                Log.i(TAG,"---------");
                Scenicspot scenicspot=new Scenicspot();
                scenicspot.setMark(mark);
                scenicspot.setCommentCount(commentCount);
                scenicspot.setFirstImg(imgUrl);
                scenicspot.setScenicName(name);
                scenicspot.setCityName(cityname);
                scenicspot.setParse(true);
                scenicspot.setUrl(url);
                scenicspot.setRank(rank);
                scenicspot.setAddr(addr);
                scenicspot.setManyA(manyA);
                scenicspot.setParse(false);
                scenicspots.add(scenicspot);
                citySpots.add(scenicspot);
                city.setScenicspots(citySpots);
                city.setParse(true);
            }
            return citySpots;
        }catch (Exception e){
            Log.e(TAG,"解析景点地址出错",e);
            return citySpots;
        }
    }

    public  void getAllScenicDetails(final Context context, String cityname){
        if (cityname==null) cityname = "杭州";
        final ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(context).getScenicspots();
        final ArrayList<Scenicspot> willParse = new ArrayList<Scenicspot>();
        if (cityname.charAt(cityname.length()-1)=='市'){
            cityname=cityname.substring(0, cityname.length() - 1);
        }
        for (Scenicspot scenicspot : scenicspots) {
            if (scenicspot.getCityName().equals(cityname)) {
                willParse.add(scenicspot);
            }
        }
        final String cityName=cityname;
        CityLab.get(context).isContain(cityName);
        for(int i=0;i<willParse.size();i++){
            final int position=i;
            Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    if(!willParse.get(position).isParse())
                        scenicspots.get(position).setParse(getScenicDetail(context, willParse.get(position)));
                    if (position==0) {
                        ScenicspotLab.get(context.getApplicationContext()).getHotScenicspots().add(scenicspots.get(position));
                    }
                    subscriber.onNext(null);
                }
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {
                    if(!willParse.get(position).isParse()){
                        Log.i(TAG, willParse.get(position).getScenicName() + "未解析成功");
                        scenicspots.remove(willParse.get(position));
                        if(CityLab.get(context).isContain(cityName)!=null){
                            CityLab.get(context).isContain(cityName).deleteScenicspot(willParse.get(position));
                            Log.i(TAG,"移除"+CityLab.get(context).isContain(cityName).getScenicspots().contains(scenicspots.get(position)));
                        }
                    }
                }
            });

        }
        Log.i(TAG,"景点信息加载完成");
    }

    public boolean getScenicDetail(Context context, Scenicspot scenicspot) {
        if (scenicspot.isParse()) {
            Log.i(TAG, "已解析过");
            return true;
        }
        HttpAnalyze httpAnalyze = new HttpAnalyze();
        String result = httpAnalyze.getHtml(scenicspot.getUrl());
//        Log.i(TAG, scenicspot.getScenicName());
        try {
            Document document = Jsoup.parse(result);
            try {
                Element introEle = document.select("div[class=normalbox boxsight_v1]").get(0);
                String brightPoint = introEle.select("div[class=detailcon bright_spot]").select("ul").select("li").text();
//                Log.i(TAG, "亮点" + brightPoint);
                String[] poiIdstr = document.select("span[class=pl_num]").select("a[class=b_orange_m]").attr("href").split("/");
                String poiId = poiIdstr[poiIdstr.length - 1].replace(".html", "");
//                Log.i(TAG, "poiId:");
                scenicspot.setPoiId(poiId);
                scenicspot.setBrightPoint(brightPoint);
                Elements elements = introEle.select("div[class=detailcon detailbox_dashed]").select("div[class=toggle_l]").select("div[itemprop=description]").select("p");
                String intro = "";
                for (int i = 0; i < elements.size(); i++) {
                    intro += (elements.get(i).text() + "\n\n");
                }
//                Log.i(TAG, "Intro:" + intro);
                scenicspot.setIntroduction(intro);
                Element detailEle = document.select("div[class=s_sight_infor]").get(0);

                Elements detailElems = detailEle.select("ul[class=s_sight_in_list]").select("li");
                int size = 0;
                if (detailElems.toString().contains("类") && detailElems.toString().contains("型")) {
                    Elements typesEle = detailElems.get(size).select("span[class=s_sight_con]").select("a");
                    String type = "";
//                    Log.i(TAG, "typeEle.size" + typesEle.size());
                    for (int i = 0; i < typesEle.size(); i++) {
                        type += (typesEle.get(i).text() + " ");
                    }
                    size++;
//                    Log.i(TAG, "type" + type);
                    scenicspot.setType(type);
                }

                if (detailElems.toString().contains("等") && detailElems.get(size).toString().contains("级"))
                    size++;
                if (detailElems.toString().contains("游玩时间")) {
                    String countTime = detailElems.get(size).select("span[class=s_sight_con]").text();
//                    Log.i(TAG, "countTime:" + countTime);
                    scenicspot.setCountTime(countTime);
                    size++;
                }
                if (detailElems.toString().contains("电") && detailElems.get(size).toString().contains("话")) {
                    String phoneNum = detailElems.get(size).select("span[class=s_sight_con]").text();
//                    Log.i(TAG, "phoneNum" + phoneNum);
                    scenicspot.setPhoneNumber(phoneNum);
                    size++;
                }
                if (detailElems.toString().contains("官方网站")) {
                    String web = detailElems.get(size).select("span[class=s_sight_con]").text();
                    scenicspot.setScenicWeb(web);
//                    Log.i(TAG, "web" + web);
                }
                if(detailEle.select("dl[class=s_sight_in_list]").size()>=1){
                    String openTime = detailEle.select("dl[class=s_sight_in_list]").get(0).select("dd").get(0).text();
//                Log.i(TAG, "openTime" + openTime);
                    scenicspot.setOpenTime(openTime);
                }
                if(detailEle.select("dl[class=s_sight_in_list]").size()>=2){
                    String ticket = detailEle.select("dl[class=s_sight_in_list]").get(1).select("dd").get(0).text();
                    scenicspot.setTicket(ticket);
                }
//                Log.i(TAG, "ticket" + ticket);
            } catch (Exception e) {
                Log.e(TAG, "基本信息解析失败" + scenicspot.getScenicName() + " " + scenicspot.getUrl(), e);
            }
            try {
                String imgUrl = document.select("div[class=carousel-inner]").select("div[class=item active]").select("a").attr("href");
                getPhoto(imgUrl, httpAnalyze, scenicspot);
            } catch (Exception e) {
                Log.e(TAG, "相册解析失败" + scenicspot.getScenicName() + " " + scenicspot.getUrl(), e);
                return false;
            }
            try {
                ArrayList<Comment> comments = getComments(result, scenicspot);
                scenicspot.setComments(comments);
                getMoreComment(context, comments, scenicspot, "2");
            } catch (Exception e) {
                Log.e(TAG, "评论解析失败" + scenicspot.getScenicName() + " " + scenicspot.getUrl(), e);
            }
            scenicspot.setParse(true);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "解析失败" + scenicspot.getScenicName() + " url" + scenicspot.getUrl(), e);
        }

        return false;
    }

    private ArrayList<Comment> getComments(String result,Scenicspot scenicspot){
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Document document = Jsoup.parse(result);
        Elements commentElems=document.select("div[class=comment_single]");
        for (int i=0;i<commentElems.size();i++) {
            Comment comment=new Comment();
            Element commentEle=commentElems.get(i);
            String userImg=commentEle.select("div[class=userimg]").select("a").select("img").attr("src");
            String userName=commentEle.select("div[class=userimg]").select("span[class=ellipsis]").select("a").text();
            String mark=commentEle.select("ul").select("li[class=title cf]").select("span[class=sblockline]").text();
            String time=commentEle.select("ul").select("li[class=title cf]").select("span[class=youcate]").text();
            String descriotion=commentEle.select("ul").select("li[class=main_con]").select("span[class=heightbox]").text();
            Elements picElems=commentEle.select("ul").select("li[class=comment_piclist cf]").select("a");
            ArrayList<String> bigImages = new ArrayList<String>();
            ArrayList<String> smaImages = new ArrayList<String>();
            for (int j = 0; j < picElems.size(); j++) {
                Element picEle=picElems.get(j);
                String imgUrlBig=picEle.attr("href");
                String imgUrlSma=picEle.select("img").attr("src");
//                Log.i(TAG, "ImagBigUrl:" + imgUrlBig + " small:" + imgUrlSma);
                smaImages.add(imgUrlSma);
                bigImages.add(imgUrlBig);
            }
//            Log.i(TAG, "userImg" + userImg);
//            Log.i(TAG, "userName" + userName);
//            Log.i(TAG, "mark" + mark);
//            Log.i(TAG, "time" + time);
//            Log.i(TAG, "description" + descriotion);
            comment.setOwnerImag(userImg);
            comment.setTime(time);
            comment.setCommentName(userName);
            comment.setSightMark(mark);
            comment.setContent(descriotion);
            comment.setImages(bigImages);
            comment.setImgSmals(smaImages);
            comments.add(comment);
        }
        return comments;
    }

    private void getPhoto(String imgUrl, HttpAnalyze httpAnalyze, final Scenicspot scenicspot){
        String urls[]=imgUrl.split("/");
        String distictUrl=urls[urls.length-2];
        String distict = getDistinct(distictUrl);
        String res=urls[urls.length-1].split("-")[0].substring(1);
        String id = urls[urls.length - 1].split("-")[1].split(".html")[0];
//        Log.i(TAG, "distinct:" + distict);
//        Log.i(TAG, "distinct:" + getDistinctName(distictUrl));
        scenicspot.setDistrictId(distict);
        scenicspot.setDistrictName(getDistinctName(distictUrl));
        scenicspot.setRes(res);
//        Log.i(TAG,"res:"+res);
//        Log.i(TAG, "id:" + id);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://you.ctrip.com/Destinationsite/TTDSecond/Photo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImgJsonIterface jsonIterface = retrofit.create(ImgJsonIterface.class);
        Call<ArrayList<ScenicImg>> call=jsonIterface.getImg(distict,"2","1",id,res);
        try {
            ArrayList<ScenicImg> scenicImgs=call.execute().body();
            scenicspot.setImgUrls(scenicImgs);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
    }

    public ArrayList<Comment> getMoreComment(Context context,ArrayList<Comment> comments , Scenicspot scenicspot,String pageNum) {
        String url = "http://you.ctrip.com/destinationsite/TTDSecond/SharedView/AsynCommentView";
        String result = null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder form = new FormBody.Builder();
//        Log.i(TAG, "poiId:" + scenicspot.getPoiId() + "districteName:" + scenicspot.getDistrictName() + " diId:" +scenicspot.getDistrictId());
        form.add("poiID", scenicspot.getPoiId())
                .add("districtId", scenicspot.getDistrictId())
                .add("districtEName", scenicspot.getDistrictName())
                .add("pagenow",scenicspot.getPageNum()+"" )
                .add("order", "3")
                .add("resourceId", scenicspot.getRes())
                .add("resourcetype","2");
        RequestBody fromBody=form.build();
        Request request = new Request.Builder()
                .url(url)
                .post(fromBody)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36")
                .build();
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            result = new String(response.body().bytes());
            result="<!DOCTYPE html>\n<html>\n<body>"+result;
            result=result+"\n</body>\n</html>";
            comments.addAll(getComments(result, scenicspot));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return comments;
    }

    private String getDistinct(String str){
        String distinct="";
        for (int i=0;i<str.length();i++){
            if(str.charAt(i)>=48&&str.charAt(i)<=57){
                distinct+=str.charAt(i);
            }
        }
        return distinct;
    }

    private String getDistinctName(String str){
        String distinctName="";
        for (int i=0;i<str.length();i++){
            if(str.charAt(i)<48||str.charAt(i)>57){
                distinctName+=str.charAt(i);
            }
        }
        return distinctName;
    }

    public void getAllCityId(Context context){
        HttpAnalyze httpAnalyze=new HttpAnalyze();
        ArrayList<City> cities= CityLab.get(context).getCities();
        String result=httpAnalyze.getHtml("http://you.ctrip.com/sitemap/placedis/c110000");
        Document document=Jsoup.parse(result);
        Elements provinceElems=document.select("div[class=sitemap_block]");
        for (Element provinceEle : provinceElems) {
            String provinceName=provinceEle.select("div[class=map_title cf]").text().replace(" 更多","");
//            Log.i(TAG, provinceName);
            Elements cityEles = provinceEle.select("ul[class=map_linklist cf]").select("li");
            boolean first=true;
            for (Element cityEle : cityEles) {
                City city=new City();
                String url=cityEle.select("a").attr("href");
                String name=cityEle.select("a").text().replace("旅游攻略","");
                String strs[]=url.split("/");
                String ctripidAndName= strs[strs.length - 1].replace(".html", "");

                if (first) {
                    Log.i(TAG,"name:"+name);
                    first=false;
                }

//                Log.i(TAG,"citripIdAndName:"+ctripidAndName);
                city.setCityName(name);
                city.setProvinceName(provinceName);
                if(provinceName.contains("直辖市")){
                    city.setProvinceName(name);
                }
                city.setCtripId(ctripidAndName);
                cities.add(city);
            }
        }
    }

    public void getPlace(City city){
        ArrayList<String> imageUrl=city.getIamgeUrls();
        HttpAnalyze httpAnalyze=new HttpAnalyze();
        String result=httpAnalyze.getHtml("http://you.ctrip.com/place/"+city.getCtripId()+".html");
        Document document=Jsoup.parse(result);
        Elements elements=document.select("div[class=slide_show]").select("div[class=imgbackgbox]").select("img");
        Log.i(TAG,"Size"+elements.size());
        for (int i = 0; i < elements.size(); i ++) {
            imageUrl.add(elements.get(i).attr("src"));
            Log.i(TAG,"imageUrl:"+elements.get(i).attr("src"));
        }
    }

    public void getWeather(Context context,City city){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://op.juhe.cn/onebox/weather/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherInterface jsonIterface = retrofit.create(WeatherInterface.class);
        Call<Weather> call=jsonIterface.getWeather(city.getCityName(),APP_KEY,"json");
        Log.i(TAG,"开始查询天气1");
        try {
            Weather weather=call.execute().body();
            Log.i(TAG,"开始查询天气2"+weather.getReason());
            if(weather.getReason().equals("查询成功")||weather.getReason().equals("successed!")){
                city.setWeather(weather);
                Log.i(TAG,city.getCityName()+"天气查询成功"+city.getWeather().getResult().getData().getWeather().get(0).getInfo().getDay().get(2));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
    }

    public String getBaiduImage(Context context,String uid,String type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://map.baidu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BaiduImageInterface jsonIterface = retrofit.create(BaiduImageInterface.class);
        Call<BaiduMapImage> call = jsonIterface.getImage("ugcphotolist", type, "1", "mappc", uid, "photo_exterior,phone_other", "1", "20");
        Log.i(TAG, "开始抓取图片");
        /**http://map.baidu.com/detail?qt=ugcphotolist&type=scope&orderBy=1&from=mappc&uid=a9173a180c1324641df74dd3
         * &photoType=photo_exterior,phone_other&pageIndex=1&pageCount=20
         * Created by CoderQiang on 2017/2/18.
         */

        try {
            BaiduMapImage baiduMapImages = call.execute().body();
            if (baiduMapImages != null && baiduMapImages.getPhoto() != null && baiduMapImages.getPhoto().getPhoto_list() != null&& baiduMapImages.getPhoto().getPhoto_list().size()>0) {
                Log.i(TAG, "baiduMapIamge" + baiduMapImages.getPhoto().getPhoto_list().get(0).getImgUrl());
                return baiduMapImages.getPhoto().getPhoto_list().get(0).getImgUrl();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "获取失败", e);
        }
        return null;
    }
}