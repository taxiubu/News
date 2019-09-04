package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newspaper.Activity.Interface.ItemClick;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.R;
import java.util.ArrayList;
// adapter bài viết liên quan
public class RelatedItemAdapter extends RecyclerView.Adapter<RelatedItemAdapter.ViewHorder> {
    private Context mcontext;
    private ArrayList<ItemRelated> list;
    private LayoutInflater inflater;
    ItemClick itemClick;
    public void setItemClick(ItemClick itemClick){
        this.itemClick= itemClick;
    }

    public RelatedItemAdapter(Context mcontext, ArrayList<ItemRelated> list) {
        this.mcontext = mcontext;
        this.list = list;
        inflater= LayoutInflater.from(mcontext);
    }

    @NonNull
    @Override
    public ViewHorder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.related_item, parent, false);
        return new ViewHorder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHorder holder, int position) {
        final ItemRelated itemRelated= list.get(position);
        holder.tvTitleRelateItem.setText(itemRelated.getTitle());
        holder.tvTitleRelateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onClick(itemRelated.getTitle(), itemRelated.getLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHorder extends RecyclerView.ViewHolder {
        TextView tvTitleRelateItem;
        public ViewHorder(@NonNull View itemView) {
            super(itemView);
            tvTitleRelateItem= itemView.findViewById(R.id.tvTitleRelatedItem);
        }
    }
}
