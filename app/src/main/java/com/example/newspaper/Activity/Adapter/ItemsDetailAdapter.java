package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newspaper.Activity.Model.Detail;
import com.example.newspaper.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
// adapter phần thông tin của bài viết
public class ItemsDetailAdapter extends RecyclerView.Adapter<ItemsDetailAdapter.ViewDetailHoder> {
    private Context context;
    private ArrayList<Detail> list;
    private LayoutInflater inflater;
    ScaleGestureDetector scaleGestureDetector;
    public ItemsDetailAdapter(Context context, ArrayList<Detail> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewDetailHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_show_detail, parent, false);
        return new ViewDetailHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDetailHoder holder, int position) {
        final Detail detail= list.get(position);
        if (detail.getBl()==true){
            holder.layoutBottom.setVisibility(View.GONE);
            holder.tvDetail.setText(detail.getText());
        }
        else {
            holder.layoutTop.setVisibility(View.GONE);
            holder.tvImageDetail.setText(detail.getText());
            Picasso.with(context)
                    .load(detail.getImageLink())
                    .placeholder(R.drawable.ic_menu_gallery)
                    .error(R.drawable.ic_menu_gallery)
                    .into(holder.imageDetail);
        }
        scaleGestureDetector= new ScaleGestureDetector(context, new Zoom(holder));
        holder.tvDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    class Zoom extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        ViewDetailHoder hoder;
        Float scale=1.0F;
        public Zoom(ViewDetailHoder hoder) {
            this.hoder = hoder;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale*=detector.getScaleFactor();
            hoder.tvDetail.setScaleX(scale);
            hoder.tvDetail.setScaleY(scale);
            return super.onScale(detector);
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

            //scaleGestureDetector= new ScaleGestureDetector(context, new Zoom());
        }

        /*class Zoom extends ScaleGestureDetector.SimpleOnScaleGestureListener{
            Float scale=1.0F;
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale*=detector.getScaleFactor();
                tvDetail.setScaleX(scale);
                tvDetail.setScaleY(scale);
                layoutTop.setScaleX(scale);
                layoutTop.setScaleY(scale);

                return super.onScale(detector);
            }
        }*/
    }

}
