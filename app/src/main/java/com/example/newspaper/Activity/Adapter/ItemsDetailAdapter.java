package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.Activity.Interface.IOnClickZoomImage;
import com.example.newspaper.Activity.Model.Detail;
import com.example.newspaper.R;

import java.util.ArrayList;
// adapter phần thông tin của bài viết
public class ItemsDetailAdapter extends RecyclerView.Adapter<ItemsDetailAdapter.ViewDetailHoder> {
    private Context context;
    private ArrayList<Detail> list;
    private LayoutInflater inflater;
    private float sizeText;
    IOnClickZoomImage zoomImage;

    public void setZoomImage(IOnClickZoomImage zoomImage) {
        this.zoomImage = zoomImage;
    }

    public ItemsDetailAdapter(Context context, ArrayList<Detail> list, float sizeText) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.sizeText= sizeText;
    }

    @NonNull
    @Override
    public ViewDetailHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_show_detail, parent, false);
        return new ViewDetailHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewDetailHoder holder, int position) {
        final Detail detail= list.get(position);
        if (detail.getBl()==true){
            holder.layoutBottom.setVisibility(View.GONE);
            holder.tvDetail.setText(detail.getText());
            holder.tvDetail.setTextSize(sizeText);
        }
        else {
            holder.layoutTop.setVisibility(View.GONE);
            holder.tvImageDetail.setText(detail.getText());
            Glide.with(context)
                    .load(detail.getImageLink())
                    .into(holder.imageDetail);
            holder.imageDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomImage.onClick(detail.getImageLink());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewDetailHoder extends RecyclerView.ViewHolder {
        TextView tvDetail;
        TextView tvImageDetail;
        ImageView imageDetail;
        RelativeLayout layoutTop, layoutBottom;
        public ViewDetailHoder(@NonNull View itemView) {
            super(itemView);
            tvDetail= itemView.findViewById(R.id.tvDetail);
            tvImageDetail= itemView.findViewById(R.id.tvImageDetail);
            imageDetail= itemView.findViewById(R.id.imageDetail);
            layoutTop= itemView.findViewById(R.id.layout_top);
            layoutBottom= itemView.findViewById(R.id.layout_bottom);
        }
    }

}
