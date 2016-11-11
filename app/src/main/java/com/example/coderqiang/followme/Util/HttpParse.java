package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;
import android.util.TimeFormatException;

import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class HttpParse {
    private static final String TAG = "HttpParse";
    public  void getScenicspot(Context context, String cityname, String pageNum) {
        ArrayList<Scenicspot> scenicspots= ScenicspotLab.get(context).getScenicspots();
        HttpAnalyze httpAnalyze=new HttpAnalyze();

        String result=httpAnalyze.getHtml("http://www.mafengwo.cn/group/s.php?q="+cityname+"&p="+pageNum+"&t=poi&kt=1");
        Document document = Jsoup.parse(result);
        Elements elements = document.select("div[class=clearfix]");
        for (int i=0;i<elements.size();i++) {
//            Log.i(TAG, "" + elements.get(i));
            Element element = elements.get(i);

            Element urlElem=element.select("div[class=flt1]").get(0).select("a").get(0);
            String scenicspotUrl = urlElem.attr("href");
            String imgUrl=urlElem.select("img").get(0).attr("src");

            Element textElem = element.select("div[class=ct-text]").get(0);
            Element nameElem = textElem.select("h3").get(0).select("a").get(0);
            String nameStr=nameElem.text();
            if (!nameStr.contains("景点")) break;
            String name=nameStr.substring(5);
            String shortIntro = textElem.select("p[class=seg-desc]").get(0).text();
            Scenicspot scenicspot = new Scenicspot();
            scenicspot.setFirstImg(imgUrl);
            scenicspot.setScenicName(name);
            scenicspot.setUrl(scenicspotUrl);
            scenicspot.setShotIntro(shortIntro);

            scenicspots.add(scenicspot);
        }
        Log.i(TAG, "size:" + scenicspots.size());
    }

    public  void getAllScenicDetails(Context context){
        ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(context).getScenicspots();
        for(int i=0;i<scenicspots.size();i++){
            if(!getScenicDetail(context, scenicspots.get(i))){
                scenicspots.remove(i);
            }
        }
        Log.i(TAG,"景点信息加载完成");
    }

    public  boolean getScenicDetail(Context context,Scenicspot scenicspot){
        try {
            int size=0;
            HttpAnalyze httpAnalyze=new HttpAnalyze();
            String result = httpAnalyze.getHtml(scenicspot.getUrl());
            Document document = Jsoup.parse(result);
            Elements js=document.select("script[type=text/javascript]");
            ArrayList<String> imageUrls=new ArrayList<String>();
            for(int i=0;i<js.size();i++) {
                String imageJS=js.get(i).toString();
                if(imageJS.contains("var headpic =")){
                    imageJS = imageJS.substring(0, 1200);
                    String Jses[]=imageJS.split("\"");
//                    Log.i(TAG,""+Jses.length);
//                    Log.i(TAG, Jses[3]);
                    if(Jses[3].contains("http")){
                        imageUrls.add(Jses[3]);
                    }else Log.e(TAG,"图片解析失败");
                    String twoImag=Jses[7].replace("\\","");
//                    Log.i(TAG, Jses[7].replace("\\",""));
                    if(twoImag.contains("http")){
                        imageUrls.add(twoImag);
                    }else Log.e(TAG,"图片解析失败");
                    String threeImag=Jses[9].replace("\\","");
                    if(threeImag.contains("http")){
                        imageUrls.add(threeImag);
                    }else Log.e(TAG,"图片解析失败");
//                    Log.i(TAG, Jses[9].replace("\\",""));

                    scenicspot.setImgUrls(imageUrls);
                    break;
                }
            }
//            Log.i(TAG, "js数量" + js.size());
            Elements elements2 = document.select("div[class=sc]");
//            Log.i(TAG, elements2.toString());
            Element cityEle = document.select("div[class=drop]").select("span[class=hd]").get(0);
            String cityName = cityEle.text();
//            Log.i(TAG, "cityName" + cityName);
            scenicspot.setCityName(cityName);
            Element introAllEles = document.select("dl[class=intro]").get(0);

            Element introEle = introAllEles.select("dt").select("p").select("span").get(0);
            Elements elements = introEle.select("[br]");
            String str[] = introEle.getAllElements().toString().split("<br>");
            String intro = "";
            for (int j = 0; j < str.length; j++) {
                intro += str[j];
                intro += "\n";
            }
            intro = intro.replace("<span>", "");
            intro = intro.replace("</span>", "");
//            Log.i(TAG, intro);
            scenicspot.setIntroduction(intro);
            Elements informationEle = introAllEles.select("dd").select("p");
            if(introAllEles.toString().contains("电话")){
                String phoneNum = informationEle.get(size++).text();
//                Log.i(TAG, "phoneNum"+phoneNum);
                scenicspot.setPhoneNumber(phoneNum);
            }
            if(introAllEles.toString().contains("网址")){
                String url = informationEle.get(size++).text();
//                Log.i(TAG, "url"+url);
                scenicspot.setScenicWeb(url);
            }
            if(introAllEles.toString().contains("交通")){
                String traffic = informationEle.get(size++).select("span").toString();
                traffic = traffic.replace("<br>", "\n");
                traffic = traffic.replace("<span>", "");
                traffic = traffic.replace("</span>", "");
//                Log.i(TAG, "traffic"+traffic);
                scenicspot.setTraffic(traffic);
            }
            if(introAllEles.toString().contains("门票")){
                String ticket = informationEle.get(size++).select("span").get(0).toString();
                ticket = ticket.replace("<br>", "\n");
                ticket = ticket.replace("<span>", "");
                ticket = ticket.replace("</span>", "");
//                Log.i(TAG, "ticket"+ticket);
                scenicspot.setTicket(ticket);
            }
            if(introAllEles.toString().contains("开放时间")){
                String time = informationEle.get(size++).select("span").get(0).toString();
                time = time.replace("<br>", "\n");
                time = time.replace("<span>", "");
                time = time.replace("</span>", "");
//                Log.i(TAG, "time:" + time);
                scenicspot.setOpenTime(time);
            }
            if(introAllEles.toString().contains("用时参考")){
                String countTime = informationEle.get(size++).text();
//                Log.i(TAG, "countTime:" + countTime);
                scenicspot.setCountTime(countTime);
            }
            scenicspot.setParse(true);
            return true;
        } catch (Exception e) {
            Log.i(TAG, "解析出错：" + scenicspot.getScenicName());
            scenicspot.setParse(false);
            return false;
        }
    }

//    public static void getParseFalseHtml(String url){
//        String html = HttpAnalyze.getHtml(url);
//        Document document = Jsoup.parse(html);
//        document.select("div[id=header]").remove();
//        document.select("div[class=share]").remove();
//        document.select("div[class=FeedBackBtn]").remove();
//        document.select("div[row row-comment").remove();
//        boolean isDel=document.select("div[id=header]").size()==0;
//        Log.i(TAG,"是否删除"+isDel);
//    }

}