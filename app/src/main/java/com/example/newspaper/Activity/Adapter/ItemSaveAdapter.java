package com.example.newspaper.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newspaper.Activity.Interface.ItemSaveClick;
import com.example.newspaper.Activity.Model.ItemSave;
import com.example.newspaper.R;
import java.util.ArrayList;
// adapter bài viết đã lưu
public class ItemSaveAdapter extends RecyclerView.Adapter<ItemSaveAdapter.ViewHoder> {
    private Context context;
    private ArrayList<ItemSave> list;
    private LayoutInflater inflater;
    ItemSaveClick itemSaveClick;

    public void setItemSaveClick(ItemSaveClick itemSaveClick) {
        this.itemSaveClick = itemSaveClick;
    }

    public ItemSaveAdapter(Context context, ArrayList<ItemSave> list) {
        this.context = context;
        this.list = list;
        inflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.item_save, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final ItemSave itemSave= list.get(position);
        holder.tvTitleItems.setText(itemSave.getTitle());
        holder.tvTitleItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSaveClick.onClick(itemSave.getTitle(), itemSave.getDocument());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvTitleItems;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvTitleItems= itemView.findViewById(R.id.tvTitleItems);
        }
    }
}
