package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newspaper.Activity.Interface.IOnClickItem;
import com.example.newspaper.Activity.Model.ItemSearch;
import com.example.newspaper.R;

import java.util.List;

public class ItemSearchAdapter extends RecyclerView.Adapter<ItemSearchAdapter.ViewHorder> {
    private List<ItemSearch> searchList;
    private Context mcontext;
    private LayoutInflater inflater;
    IOnClickItem onClickItem;

    public void setOnClickItem(IOnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    public ItemSearchAdapter(List<ItemSearch> searchList, Context mcontext) {
        this.searchList = searchList;
        this.mcontext = mcontext;
        inflater= LayoutInflater.from(mcontext);
    }

    @NonNull
    @Override
    public ItemSearchAdapter.ViewHorder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_search, parent, false);
        return new ViewHorder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemSearchAdapter.ViewHorder holder, int position) {
        final ItemSearch itemSearch= searchList.get(position);
        holder.tvTitleItemSearch.setText(itemSearch.getTitle());
        Glide.with(mcontext)
                .load(itemSearch.getImage())
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_gallery)
                .into(holder.imageItemSearch);
        holder.tvTitleItemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClick(itemSearch.getTitle(), itemSearch.getLink());
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHorder extends RecyclerView.ViewHolder {
        ImageView imageItemSearch;
        TextView tvTitleItemSearch;
        public ViewHorder(@NonNull View itemView) {
            super(itemView);
            imageItemSearch= itemView.findViewById(R.id.imageItemSearch);
            tvTitleItemSearch= itemView.findViewById(R.id.tvTitleSearch);
        }
    }
}
