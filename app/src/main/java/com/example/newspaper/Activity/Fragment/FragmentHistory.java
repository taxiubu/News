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
import com.example.newspaper.Activity.Activity.ShowDetail;
import com.example.newspaper.Activity.Adapter.ClickHistoryAdapter;
import com.example.newspaper.Activity.Interface.ItemClick;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.SQL.SQLClickHistory;
import com.example.newspaper.R;
import java.util.ArrayList;
import java.util.List;

public class FragmentHistory extends Fragment {
    RecyclerView rcvItemHistory;
    SQLClickHistory sqlClickHistory;
    TextView btRemoveAll;
    List<ItemRelated> list, listRemove, listRemoveAll;
    public static FragmentHistory newInstance() {

        Bundle args = new Bundle();

        FragmentHistory fragment = new FragmentHistory();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_history, container, false);
        rcvItemHistory= view.findViewById(R.id.rcvItemHistory);
        btRemoveAll= view.findViewById(R.id.btRemoveAllHistory);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvItemHistory.setLayoutManager(layoutManager);

        // lấy list từ bảng SQLClickHistory
        sqlClickHistory= new SQLClickHistory(getContext());
        list= sqlClickHistory.getAllItem();

        //Dùng Adapter
        ClickHistoryAdapter adapter= new ClickHistoryAdapter(getContext(), (ArrayList<ItemRelated>) list);
        rcvItemHistory.setAdapter(adapter);
        adapter.setItemClick(new ItemClick() {
            @Override
            public void onClick(final String title, final String link) {
                PopupMenu popupMenu= new PopupMenu(getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_click_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id= menuItem.getItemId();
                        if (id==R.id.itemView){
                            Toast.makeText(getContext(), R.string.view, Toast.LENGTH_LONG).show();
                            Intent intentClick= new Intent(getContext(), ShowDetail.class);
                            intentClick.putExtra("link", link);
                            startActivity(intentClick);
                        }
                        else {
                            sqlClickHistory.deleteItemClick(title);
                            listRemove= sqlClickHistory.getAllItem();
                            ClickHistoryAdapter adapterRemove= new ClickHistoryAdapter(getContext(), (ArrayList<ItemRelated>) listRemove);
                            rcvItemHistory.setAdapter(adapterRemove);
                            Toast.makeText(getContext(), R.string.remove, Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        // Xóa tất cả
        btRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlClickHistory.deleteAll();
                listRemoveAll= sqlClickHistory.getAllItem();
                ClickHistoryAdapter adapterRemoveAll= new ClickHistoryAdapter(getContext(), (ArrayList<ItemRelated>) listRemoveAll);
                rcvItemHistory.setAdapter(adapterRemoveAll);
                Toast.makeText(getContext(), R.string.removeAll, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
