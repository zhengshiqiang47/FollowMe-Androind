package com.example.coderqiang.followme.Util;

import android.content.Context;
import android.util.Log;
import android.util.TimeFormatException;

import com.example.coderqiang.followme.Model.Comment;
import com.example.coderqiang.followme.Model.Scenicspot;
import com.example.coderqiang.followme.Model.ScenicspotLab;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by CoderQiang on 2016/11/6.
 */

public class HttpParse {
    private static final String TAG = "HttpParse";

    public void getScenicspot(Context context,String cityname,String pageNum){
        ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(context).getScenicspots();
        HttpAnalyze httpAnalyze=new HttpAnalyze();
        String result=httpAnalyze.getHtml("http://you.ctrip.com/searchsite/Sight?query="+cityname);
        Document document = Jsoup.parse(result);
        Element allSightEle=document.select("a[class=fr]").get(0);
        String allUrl=allSightEle.attr("href");
        String pageUrl=allUrl.split(".html")[0]+"/s0-p"+pageNum+".html";
        String sightResult=httpAnalyze.getHtml("http://you.ctrip.com/"+pageUrl);
        Document sightDoc=Jsoup.parse(sightResult);
        Elements sightElems=sightDoc.select("div[class=list_mod2]");
        Log.w(TAG, "景点个数" + sightElems.size());
        for (int i=0;i<sightElems.size();i++) {
            Element element=sightElems.get(i);
            String imgUrl=element.select("div[class=leftimg]").select("a").select("img").attr("src");
            Log.i(TAG, "imgUrl:" + imgUrl);

            String name=element.select("div[class=rdetailbox]").select("dl").select("dt").select("a[target=_blank]").text();
            String url="http://you.ctrip.com"+element.select("div[class=rdetailbox]").select("dl").select("dt").select("a[target=_blank]").attr("href");
            String rank=element.select("div[class=rdetailbox]").select("dl").select("dt").select("s").text();
            String addr=element.select("div[class=rdetailbox]").select("dl").select("dd").get(0).text();
            String manyA=element.select("div[class=rdetailbox]").select("dl").select("dd").get(1).text();
            if(manyA.contains("A级景区")){
                manyA=manyA.split("景区")[0];
            }else manyA=" ";
            String mark=element.select("div[class=rdetailbox]").select("ul[class=r_comment]").select("li").select("a[class=score]").text();
            String commentCount=element.select("div[class=rdetailbox]").select("ul[class=r_comment]").select("li").select("a[class=recomment]").text();
            Log.i(TAG,"name:"+name);
            Log.i(TAG,"url:"+url);
            Log.i(TAG,"rank:"+rank);
            Log.i(TAG,"addr:"+addr);
            Log.i(TAG,"manyA:"+manyA);
            Log.i(TAG,"mark:"+mark);
            Log.i(TAG, "commentCount" + commentCount);
            Log.i(TAG,"---------");
            Scenicspot scenicspot=new Scenicspot();
            scenicspot.setFirstImg(imgUrl);
            scenicspot.setScenicName(name);
            scenicspot.setCityName(cityname);
            scenicspot.setParse(true);
            scenicspot.setUrl(url);
            scenicspot.setRank(rank);
            scenicspot.setAddr(addr);
            scenicspot.setManyA(manyA);
            scenicspots.add(scenicspot);
        }
    }

//    public  void getScenicspot(Context context, String cityname, String pageNum) {
//        ArrayList<Scenicspot> scenicspots= ScenicspotLab.get(context).getScenicspots();
//        HttpAnalyze httpAnalyze=new HttpAnalyze();
//        String temp=httpAnalyze.getHtml("http://www.mafengwo.cn/ajax/ajax_fetch_pagelet.php?callback=jQuery18107823185430330342_1480340723223&params=%7B%22poi_id%22%3A6822%7D&api=%3Amdd%3Apagelet%3ApoiCommentListApi&_=1480340723306");
//        Log.e(TAG, "temp" + temp);
//        temp = temp.substring(3000);
//        Log.e(TAG, "temp2" + temp);
//        String result=httpAnalyze.getHtml("http://www.mafengwo.cn/group/s.php?q="+cityname+"&p="+pageNum+"&t=poi&kt=1");
//        Document document = Jsoup.parse(result);
//        Elements elements = document.select("div[class=clearfix]");
//        for (int i=0;i<elements.size();i++) {
////            Log.i(TAG, "" + elements.get(i));
//            Element element = elements.get(i);
//
//            Element urlElem=element.select("div[class=flt1]").get(0).select("a").get(0);
//            String scenicspotUrl = urlElem.attr("href");
//            String imgUrl=urlElem.select("img").get(0).attr("src");
//
//            Element textElem = element.select("div[class=ct-text]").get(0);
//            Element nameElem = textElem.select("h3").get(0).select("a").get(0);
//            String nameStr=nameElem.text();
//            if (!nameStr.contains("景点")) break;
//            String name=nameStr.substring(5);
//            String shortIntro = textElem.select("p[class=seg-desc]").get(0).text();
//            Scenicspot scenicspot = new Scenicspot();
//            scenicspot.setFirstImg(imgUrl);
//            scenicspot.setScenicName(name);
//            scenicspot.setUrl(scenicspotUrl);
//            scenicspot.setShotIntro(shortIntro);
//
//            scenicspots.add(scenicspot);
//            scenicspot.setScenicspotID(Scenicspot.ID++);
//        }
//        Log.i(TAG, "size:" + scenicspots.size());
//    }

    public  void getAllScenicDetails(Context context){
        ArrayList<Scenicspot> scenicspots=ScenicspotLab.get(context).getScenicspots();
        for(int i=0;i<scenicspots.size();i++){
            if(!getScenicDetail(context, scenicspots.get(i))){
                scenicspots.remove(i);
            }
        }
        Log.i(TAG,"景点信息加载完成");
    }

    public boolean getScenicDetail(Context context,Scenicspot scenicspot){
        HttpAnalyze httpAnalyze=new HttpAnalyze();
        String result = httpAnalyze.getHtml(scenicspot.getUrl());
        Log.i(TAG, scenicspot.getScenicName());
        Document document = Jsoup.parse(result);
        String imgUrl=document.select("div[class=carousel-inner]").select("div[class=item active]").select("a").attr("href");

        return true;
    }

    private void getPhoto(String imgUrl){
        String urls[]=imgUrl.split("/");
        String distictUrl=urls[urls.length-2];
        String distict = getDistinct(distictUrl);
        String res=urls[urls.length-1].split("-")[0].substring(1);
        String id = urls[urls.length - 1].split("-")[1].split(".html")[0];
        Log.i(TAG, "distinct:" + distict);
        Log.i(TAG,"res:"+res);
        Log.i(TAG, "id:" + id);
        String ajaxUrl = "http://you.ctrip.com/Destinationsite/TTDSecond/Photo/AjaxPhotoDetailList?districtId="
                + distict + "&" + "type=2&phsid=" + id + "&resource" + res;

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
//    public  boolean getScenicDetail(Context context,Scenicspot scenicspot){
//        try {
//            int size=0;
//            HttpAnalyze httpAnalyze=new HttpAnalyze();
//            String result = httpAnalyze.getHtml(scenicspot.getUrl());
//            Document document = Jsoup.parse(result);
////            scenicspot.setComments(getComments(result));
//            Elements photo=document.select("div[class=row row-picture row-bg]").select("a[class=photo]").select("img");
//            ArrayList<String> imageUrls=new ArrayList<String>();
//            Log.i(TAG, "photo.size"+photo.size());
//            for(int i=0;i<photo.size();i++) {
//                String imageJS=photo.get(i).attr("src");
//                Log.i(TAG, imageJS);
//                imageUrls.add(imageJS);
//            }
//            scenicspot.setImgUrls(imageUrls);
////            Log.i(TAG, "js数量" + js.size());
//            Elements elements2 = document.select("div[class=sc]");
////            Log.i(TAG, elements2.toString());
//            Element cityEle = document.select("div[class=drop]").select("span[class=hd]").get(0);
//            String cityName = cityEle.text();
////            Log.i(TAG, "cityName" + cityName);
//            scenicspot.setCityName(cityName);
//            Element introAllEles = document.select("div[class=mod mod-detail]").get(0);
//
//            Element introEle = introAllEles.select("[class=summary]").get(0);
//            String str[] = introEle.getAllElements().toString().split("<br>");
//            String intro = "";
//            for (int j = 0; j < str.length; j++) {
//                intro += str[j];
//                intro += "\n";
//            }
//            intro = intro.replace("<div class=\"summary\">", "");
//            intro = intro.replace("</div>", "");
//            Log.i(TAG, intro);
//            scenicspot.setIntroduction(intro);
//            Elements informationEle = introAllEles.select("ul[class=baseinfo clearfix]").select("div[class=content]");
//            if(introAllEles.toString().contains("电话")){
//                String phoneNum = informationEle.get(size++).text();
//                Log.i(TAG, "phoneNum"+phoneNum);
//                scenicspot.setPhoneNumber(phoneNum);
//            }
//            if(introAllEles.toString().contains("网址")){
//                String url = informationEle.get(size++).text();
//                Log.i(TAG, "url"+url);
//                scenicspot.setScenicWeb(url);
//            }
//            if(introAllEles.toString().contains("用时参考")){
//                String countTime = informationEle.get(size++).text();
//                Log.i(TAG, "countTime:" + countTime);
//                scenicspot.setCountTime(countTime);
//            }
//            int secondSize=0;
//
//            if(introAllEles.toString().contains("交通")){
//                String traffic = introAllEles.select("dd").get(secondSize++).toString();
//                traffic = traffic.replace("<br>", "\n");
//                traffic = traffic.replace("<dd>", "");
//                traffic = traffic.replace("</dd>", "");
//                Log.i(TAG, "traffic"+traffic);
//                scenicspot.setTraffic(traffic);
//            }
//
//            if(introAllEles.toString().contains("门票")){
//                String ticket = introAllEles.select("dd").get(secondSize++).toString();
//                ticket = ticket.replace("<br>", "\n");
//                ticket = ticket.replace("<dd>", "");
//                ticket = ticket.replace("</dd>", "");
//                ticket = ticket.replace("<div>", "");
//                ticket = ticket.replace("</div>", "");
//                Log.i(TAG, "ticket"+ticket);
//                scenicspot.setTicket(ticket);
//            }
//            if(introAllEles.toString().contains("开放时间")){
//                String time = introAllEles.select("dd").get(secondSize++).toString();
//                time = time.replace("<br>", "\n");
//                time = time.replace("<dd>", "");
//                time = time.replace("</dd>", "");
//                Log.i(TAG, "time:" + time);
//                scenicspot.setOpenTime(time);
//            }
//
//            scenicspot.setParse(true);
//            return true;
//        } catch (Exception e) {
//            Log.i(TAG, "解析出错：" + scenicspot.getScenicName()+scenicspot.getUrl(),e);
//            scenicspot.setParse(false);
//            return false;
//        }
//    }

//    public  ArrayList<Comment> getComments(String result) {
//        ArrayList<Comment> comments = new ArrayList<Comment>();
//        try {
//
//            Document document = Jsoup.parse(result);
//            Element commnetEle=document.select("ul[class=rev-lists]").get(0);
//            Elements commentEles=document.select("li[class=rev-item clearfix]");
//            Log.i(TAG, "commentSize" + commentEles.size());
//            for(int i=0;i<commentEles.size();i++) {
//                Comment comment=new Comment();
//                Element element = commentEles.get(i);
//                ArrayList<String> imgUrls = new ArrayList<String>();
//                String id = element.attr("id");
////                Log.i(TAG, "id" + id);
//                String ownerImg=element.select("img[class=lazyavatar]").get(0).attr("data-original");
////                Log.i(TAG, "ownerImg:" + ownerImg);
//                comment.setOwnerImag(ownerImg);
//                String name = element.select("a[class=name]").get(0).text();
////                Log.i(TAG, "name:" + name);
//                comment.setCommentName(name);
//                String content=element.select("p[class=rev-txt]").text();
////                Log.i(TAG, "content:" + content);
//                comment.setContent(content);
//                Elements imgEles=element.select("img[class=lazy]");
//                Element dateEle=element.select("div[class=info clearfix]").select("span").get(0);
////                Log.i(TAG,"date:"+dateEle.text().substring(0,10));
//                comment.setTime(dateEle.text().substring(0,10));
//                ArrayList<String> images = new ArrayList<String>();
//                for(int j=0;j<imgEles.size();j++) {
//                    String imgUrl=imgEles.get(j).attr("data-original");
//                    imgUrls.add(imgUrl);
//                    images.add(imgUrl);
////                    Log.i(TAG, "imgUrl:" + imgUrl);
//                }
//                comment.setImages(images);
//                comments.add(comment);
//            }
//            return comments;
//        } catch (Exception e) {
//            Log.e(TAG,"评论解析失败：",e);
//            return comments;
//        }
//    }
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