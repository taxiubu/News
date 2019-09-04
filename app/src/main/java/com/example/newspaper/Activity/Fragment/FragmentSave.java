package com.example.newspaper.Activity.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newspaper.Activity.Activity.ShowItemSave;
import com.example.newspaper.Activity.Adapter.ItemSaveAdapter;
import com.example.newspaper.Activity.Interface.ItemSaveClick;
import com.example.newspaper.Activity.Model.ItemSave;
import com.example.newspaper.Activity.SQL.SQLItemSave;
import com.example.newspaper.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentSave extends Fragment {
    RecyclerView rcvItemSave;
    SQLItemSave sqlItemSave;
    TextView btRemoveAll;
    public static FragmentSave newInstance() {

        Bundle args = new Bundle();

        FragmentSave fragment = new FragmentSave();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_save, container, false);
        rcvItemSave= view.findViewById(R.id.rcvItemSave);
        btRemoveAll= view.findViewById(R.id.btRemoveAllSave);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvItemSave.setLayoutManager(layoutManager);

        // lấy list từ bảng SQLItemSave
        sqlItemSave= new SQLItemSave(getContext());
        List<ItemSave> saveList;
        saveList= sqlItemSave.getAllItemSave();
        ItemSaveAdapter itemSaveAdapter= new ItemSaveAdapter(getContext(), (ArrayList<ItemSave>) saveList);
        rcvItemSave.setAdapter(itemSaveAdapter);
        itemSaveAdapter.setItemSaveClick(new ItemSaveClick() {
            @Override
            public void onClick(final String title, final String document) {
                PopupMenu popupMenu= new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id= menuItem.getItemId();
                        if (id==R.id.itemView){
                            Toast.makeText(getContext(), R.string.view, Toast.LENGTH_LONG).show();
                            Intent itemClick= new Intent(getContext(), ShowItemSave.class);
                            itemClick.putExtra("document", document);
                            startActivity(itemClick);
                        }
                        else {
                            sqlItemSave.deleteItemSave(title);
                            List<ItemSave> removeList;
                            removeList= sqlItemSave.getAllItemSave();
                            ItemSaveAdapter removeItem= new ItemSaveAdapter(getContext(), (ArrayList<ItemSave>) removeList);
                            rcvItemSave.setAdapter(removeItem);
                            Toast.makeText(getContext(), R.string.remove, Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });

        //xóa tất cả
        btRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlItemSave.deleteAll();
                List<ItemSave> listRemoveALl;
                listRemoveALl= sqlItemSave.getAllItemSave();
                ItemSaveAdapter RemoveAll= new ItemSaveAdapter(getContext(), (ArrayList<ItemSave>) listRemoveALl);
                rcvItemSave.setAdapter(RemoveAll);
                Toast.makeText(getContext(), R.string.removeAll, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
