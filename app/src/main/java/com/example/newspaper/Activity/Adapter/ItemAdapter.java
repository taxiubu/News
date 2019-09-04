package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Interface.ItemClick;
import com.example.newspaper.Activity.Model.Item;
import com.example.newspaper.Activity.Model.RootObject;
import com.example.newspaper.Activity.SQL.SQLItemSave;
import com.example.newspaper.R;
import com.squareup.picasso.Picasso;
// adapter bài viết của từng mục
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHoder> {
    private RootObject rssObject;
    private Context mContext;
    private LayoutInflater inflater;
    ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public ItemAdapter(RootObject rssObject, Context mContext) {
        this.rssObject = rssObject;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_list, parent, false);
        ViewHoder viewHoder = new ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {

        final Item item = rssObject.getItems().get(position);
        if (position==0){
            holder.tvTitleItemLarge.setText(item.getTitle());
            holder.tvDesItemLarge.setText(splitString(item.getDescription()));
            Picasso.with(mContext)
                    .load(item.getThumbnail())
                    .placeholder(R.drawable.ic_menu_gallery)
                    .error(R.drawable.ic_menu_gallery)
                    .into(holder.imageItemLarge);
            holder.clickItemSmall.setVisibility(View.GONE);
            holder.clickItemLarge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onClick(item.title, item.link);
                }
            });
        }
        else {
            holder.tvTitleItemSmall.setText(item.getTitle());
            Picasso.with(mContext)
                    .load(item.getThumbnail())
                    .placeholder(R.drawable.ic_menu_gallery)
                    .error(R.drawable.ic_menu_gallery)
                    .into(holder.imageItemSmall);
            holder.clickItemLarge.setVisibility(View.GONE);
            holder.clickItemSmall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onClick(item.title, item.link);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rssObject.getItems().size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvTitleItemSmall, tvTitleItemLarge, tvDesItemLarge;
        RelativeLayout clickItemSmall;
        LinearLayout clickItemLarge;
        ImageView imageItemSmall, imageItemLarge;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvTitleItemSmall = itemView.findViewById(R.id.tvTitle);
            imageItemSmall = itemView.findViewById(R.id.imageItem);
            clickItemSmall = itemView.findViewById(R.id.layout_item_small);

            tvTitleItemLarge= itemView.findViewById(R.id.tvTitleLarge);
            tvDesItemLarge= itemView.findViewById(R.id.tvDescriptionLarge);
            imageItemLarge= itemView.findViewById(R.id.imageItemLarge);
            clickItemLarge= itemView.findViewById(R.id.layout_item_large);
        }
    }

    private String splitString(String s) {
        int index = s.lastIndexOf('>');
        return s.substring(index + 1);
    }

}
