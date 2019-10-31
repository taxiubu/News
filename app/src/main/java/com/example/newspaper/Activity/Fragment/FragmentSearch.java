package com.example.newspaper.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Activity.ShowDetail;
import com.example.newspaper.Activity.Adapter.AdapterItemSearch;
import com.example.newspaper.Activity.Define.Define;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Interface.IOnClickItem;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.Model.ItemSearch;
import com.example.newspaper.Activity.SQL.SQLClickHistory;
import com.example.newspaper.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FragmentSearch extends Fragment {
    SQLClickHistory sqlClickHistory;
    RecyclerView rcvItemSearch;
    AdapterItemSearch adapter;
    PublicMethod publicMethod= new PublicMethod();
    public static FragmentSearch newInstance(String titleSearch) {

        Bundle args = new Bundle();
        args.putString("titleSearch", titleSearch);
        FragmentSearch fragment = new FragmentSearch();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        String titleSearch= getArguments().getString("titleSearch");
        String urlSearch= Define.URL_SEARCH+ fixString(titleSearch)+ Define.END_URL_SEARCH;
        rcvItemSearch= view.findViewById(R.id.rcvItemSearch);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvItemSearch.setLayoutManager(layoutManager);
        if(publicMethod.checkConnectInternet(getContext()))
            new loadItemSearch().execute(urlSearch);
        return view;
    }
    private String fixString(String s){
        return s.replace(" ", "+");
    }

    private class loadItemSearch extends AsyncTask <String, Void, ArrayList<ItemSearch>>{
        private static final String TAG = "loadItemSearch";
        ProgressDialog dialog= new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading ...");
            dialog.show();
        }

        @Override
        protected ArrayList<ItemSearch> doInBackground(String... strings) {
            Document document;
            ArrayList<ItemSearch> list= new ArrayList<>();
            try {
                document= Jsoup.connect(strings[0]).get();
                if(document!=null){
                    Elements article= document.select("article.list_news");
                    for(Element element:article){
                        Element thumb_art= element.selectFirst("div");
                        Element tag_a= thumb_art.tagName("a");
                        Element img= thumb_art.selectFirst("img");
                        String text_tag_a= tag_a.html();
                        String title= publicMethod.dataTitle(text_tag_a);
                        String image= img.attr("src");
                        String link= publicMethod.dataURL(text_tag_a);
                        list.add(new ItemSearch(title, image, link));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemSearch> itemSearches) {
            super.onPostExecute(itemSearches);
            dialog.dismiss();
            adapter= new AdapterItemSearch(itemSearches, getContext());
            rcvItemSearch.setAdapter(adapter);
            adapter.setOnClickItem(new IOnClickItem() {
                @Override
                public void onClick(String title, String link) {
                    // insert to SQLClickHistory
                    sqlClickHistory= new SQLClickHistory(getContext());
                    List<ItemRelated> list;
                    list= sqlClickHistory.getAllItem();
                    publicMethod= new PublicMethod();
                    if(publicMethod.checkTitleItemClick(title, list)){
                        sqlClickHistory.deleteItemClick(title);
                        sqlClickHistory.insertItem(title, link);
                    }
                    else
                        sqlClickHistory.insertItem(title, link);

                    // intent showDetail
                    Intent intent= new Intent(getContext(), ShowDetail.class);
                    intent.putExtra("link", link);
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
        }
    }

}
