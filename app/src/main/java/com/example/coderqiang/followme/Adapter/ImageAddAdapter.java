package com.example.coderqiang.followme.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coderqiang.followme.Activity.ImageFilterActivity;
import com.example.coderqiang.followme.Activity.NewDynamicActivity;
import com.example.coderqiang.followme.Activity.PictureActivity;
import com.example.coderqiang.followme.Model.DynamicImage;
import com.example.coderqiang.followme.R;

import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by CoderQiang on 2017/2/16.
 */

public class ImageAddAdapter extends RecyclerView.Adapter {
    private static final int PHOTO_REQUEST=0;
    private static final int TYPE_ADDICON = 1;
    private static final int TYPE_NORMAL = 2;
    public static final int VIEW_DYNAMIC = 3;
    public static final int VIEW_NEW_DYNAMIC = 4;

    private Activity activity;
    private ArrayList<Uri> uris ;
    private Uri uri;
    private int selectPosition;
    private int type;
    private ArrayList<DynamicImage> urls ;
    private ArrayList<String> urlStr=new ArrayList<>();

    public ImageAddAdapter(int type,ArrayList<Uri> uris,Activity activity,ArrayList<DynamicImage> urls) {
        super();
        this.uris=uris;
        this.urls=urls;
        if (urls != null) {
            for (DynamicImage dynamicImage:urls){
                String url = "http://123.206.195.52:8080/day_30/dynamicImg/" + dynamicImage.getName();
                urlStr.add(url);
            }
        }
        this.activity=activity;
        this.type=type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return new ImageHolder(LayoutInflater.from(activity).inflate(R.layout.item_new_dynamic_image, parent, false));
        } else if (viewType == TYPE_ADDICON) {
            return new FootHolder(LayoutInflater.from(activity).inflate(R.layout.item_new_dynamic_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        if(type==VIEW_DYNAMIC){
            ImageHolder imageHolder=(ImageHolder)holder;
            showDynamic(position, imageHolder);
        }else if(position==uris.size()){
            FootHolder footHolder=(FootHolder)holder;
            footHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    activity.startActivityForResult(intent,PHOTO_REQUEST);
                }
            });
        }else{
            ImageHolder imageHolder=(ImageHolder)holder;
            if(type==VIEW_NEW_DYNAMIC){
                showNewDynamic(position, imageHolder);
            }
        }
    }

    private void showDynamic(int position, ImageHolder imageHolder) {
        String baseUrl="http://123.206.195.52:8080/day_30/dynamicImg";
        imageHolder.delete.setVisibility(View.GONE);
        imageHolder.filter.setVisibility(View.GONE);
        final int tempPosition=position;
        imageHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PictureActivity.class);
                intent.putExtra(PictureActivity.EXTRA_IMGURLS,urlStr);
                intent.putExtra(PictureActivity.EXTRA_POSITION,tempPosition);
                intent.putExtra(PictureActivity.EXTRA_DESCRIPTION," ");
                activity.startActivity(intent);
            }
        });
        Glide.with(activity).load(baseUrl+"/"+urls.get(position).getName()).override(324,576).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageHolder.image);
    }

    private void showNewDynamic(int position, ImageHolder imageHolder) {
        Glide.with(activity).load(uris.get(position)).into(imageHolder.image);
        final Uri tempUri=uris.get(position);
        final int tempPostion=position;
        imageHolder.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ImageFilterActivity.class);
                intent.putExtra("uri",tempUri.toString());
                intent.putExtra("position",tempPostion);
                activity.startActivityForResult(intent, NewDynamicActivity.FILTER_REQUEST);
            }
        });
        imageHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ImageFilterActivity.class);
                intent.putExtra("uri",tempUri.toString());
                intent.putExtra("position",tempPostion);
                activity.startActivityForResult(intent,NewDynamicActivity.FILTER_REQUEST);
            }
        });
        imageHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition=tempPostion;
                uri=tempUri;
                delete();
//                    uris.remove(uri);
//                    Log.i("Adapter","itemCount:"+uris.size());
//                    notifyItemRemoved(tempPostion);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (type==VIEW_NEW_DYNAMIC&&position == uris.size()) {
            return TYPE_ADDICON;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if(type==VIEW_DYNAMIC){
            return urls==null?0:urls.size();
        }
        return uris.size() + 1;
    }

    public ArrayList<Uri> getUris(){
        return uris;
    }

    private void delete(){
        uris.remove(uri);
        Log.i("Adapter","itemCount:"+uris.size());
        notifyItemRemoved(selectPosition);
//        notifyDataSetChanged();
    }


    private class ImageHolder extends RecyclerView.ViewHolder {

        ImageView delete;
        ImageView image;
        ImageView filter;

        public ImageHolder(View itemView) {
            super(itemView);
            delete=(ImageView)itemView.findViewById(R.id.new_dynamic_item_delete);
            image=(ImageView)itemView.findViewById(R.id.new_dynamic_item_image);
            filter = (ImageView) itemView.findViewById(R.id.new_dynamic_item_filter);
        }
    }

    private class FootHolder extends RecyclerView.ViewHolder {
        ImageView add;
        public FootHolder(View itemView) {
            super(itemView);
            add=(ImageView)itemView.findViewById(R.id.item_new_dynamic_footer_add);
        }
    }
}
