package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.newspaper.Activity.Define.Define;
import com.example.newspaper.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {
    private Context context;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.background_toolbar, null);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        switch (position){
            case 0:{
                Glide.with(context)
                        .load(Define.BACKGROUND_01)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }
            case 1:{
                Glide.with(context)
                        .load(Define.BACKGROUND_02)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }
            case 2:{
                Glide.with(context)
                        .load(Define.BACKGROUND_03)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }
            case 3:{
                Glide.with(context)
                        .load(Define.BACKGROUND_04)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }
            case 4:{
                Glide.with(context)
                        .load(Define.BACKGROUND_05)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }
            default:{
                Glide.with(context)
                        .load(Define.BACKGROUND_06)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .error(R.drawable.ic_menu_gallery)
                        .into(viewHolder.imageBackGroundToolbar);
                break;
            }


        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        ImageView imageBackGroundToolbar;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageBackGroundToolbar= itemView.findViewById(R.id.imageBackgourdToolbar);
        }
    }
}
