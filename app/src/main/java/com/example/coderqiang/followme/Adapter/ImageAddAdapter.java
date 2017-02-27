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
import com.example.coderqiang.followme.R;

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
    private ArrayList<Uri> uris = new ArrayList<>();
    private Uri uri;
    private int selectPosition;
    private int type;

    public ImageAddAdapter(int type,ArrayList<Uri> uris,Activity activity) {
        super();
        this.uris=uris;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(position==uris.size()){
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
            Glide.with(activity).load(uris.get(position)).into(imageHolder.image);
            final Uri tempUri=uris.get(position);
            final int tempPostion=position;
            imageHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPosition=position;
                    uri=tempUri;
                    delete();
//                    uris.remove(uri);
//                    Log.i("Adapter","itemCount:"+uris.size());
//                    notifyItemRemoved(tempPostion);
                }
            });
            if(type==VIEW_DYNAMIC){
                imageHolder.delete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == uris.size()) {
            return TYPE_ADDICON;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if(type==VIEW_DYNAMIC){
            return uris.size();
        }
        return uris.size() + 1;
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

        public ImageHolder(View itemView) {
            super(itemView);
            delete=(ImageView)itemView.findViewById(R.id.new_dynamic_item_delete);
            image=(ImageView)itemView.findViewById(R.id.new_dynamic_item_image);
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
