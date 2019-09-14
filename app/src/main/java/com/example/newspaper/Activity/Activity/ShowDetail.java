package com.example.newspaper.Activity.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Adapter.ItemsDetailAdapter;
import com.example.newspaper.Activity.Adapter.RelatedItemAdapter;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Interface.IOnClickItem;
import com.example.newspaper.Activity.Model.Detail;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.Model.ItemSave;
import com.example.newspaper.Activity.SQL.SQLClickHistory;
import com.example.newspaper.Activity.SQL.SQLItemSave;
import com.example.newspaper.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ShowDetail extends AppCompatActivity {
    Float textSize=15F;
    ImageView btBack;
    ScrollView layoutDetail;
    TextView btSave;
    TextView tvShowTitle, tvPubDateShow, tvDesShow;
    RecyclerView recyclerViewDetail, recyclerViewRelatedItem;
    ItemsDetailAdapter adapter;
    RelatedItemAdapter relatedItemAdapter;
    String titleDetail, descriptionDetail, pubDateDetail;
    SQLItemSave itemSave;
    String documentItemSave;
    SQLClickHistory sqlClickHistory;
    TextView tvXemNhieu;
    ImageView btZoomOut, btZoomIn;
    PublicMethod publicMethod= new PublicMethod();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        getSupportActionBar().hide();
        btBack= findViewById(R.id.btBack);
        tvShowTitle= findViewById(R.id.tvTitleShow);
        tvPubDateShow= findViewById(R.id.tvPubDateShow);
        tvDesShow= findViewById(R.id.tvDescriptionShow);
        tvXemNhieu= findViewById(R.id.tvBaiVietLQ);
        layoutDetail= findViewById(R.id.layoutDetail);
        btSave= findViewById(R.id.btSave);
        btZoomIn= findViewById(R.id.btZoomIn);
        btZoomOut= findViewById(R.id.btZoomOut);

        //setBack
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
            }
        });

        //checkInternet
        if(publicMethod.checkConnectInternet(getBaseContext())==false)
            Toast.makeText(getBaseContext(), R.string.NoInternet, Toast.LENGTH_LONG).show();

        //rcv phần thông tin
        String link= getIntent().getStringExtra("link");
        recyclerViewDetail= findViewById(R.id.rcvDetail);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        recyclerViewDetail.setLayoutManager(layoutManager1);
        new LoadDetail().execute(link);

        //rcv phần bài viết liên quan
        recyclerViewRelatedItem= findViewById(R.id.rcvRelatedItem);
        RecyclerView.LayoutManager layoutManager2= new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        recyclerViewRelatedItem.setLayoutManager(layoutManager2);
        new LoadRelatedItem().execute(link);

    }

    private class LoadDetail extends AsyncTask<String, Void, ArrayList<Detail>>{
        private static final String TAG = "LoadDetail";
        @Override
        protected ArrayList<Detail> doInBackground(String... strings) {
            Document document= null;
            ArrayList<Detail> details= new ArrayList<>();
            try{
                document= Jsoup.connect(strings[0]).get();
                if(document!=null){
                    Element container= document.select("section.container").first();
                    Element sidebar1= container.select("section.sidebar_1").first();
                    documentItemSave= sidebar1.html();
                    titleDetail= sidebar1.selectFirst("h1").text();
                    descriptionDetail= sidebar1.selectFirst("p").text();
                    pubDateDetail= sidebar1.selectFirst("span").text();

                    Element article= sidebar1.select("article").first();
                    Elements lineDetail= article.select("p.Normal, table, div#article_content, p.MsNormal");
                    for (Element element: lineDetail){

                        String text= element.html();
                        Boolean bl= true;
                        if(text.indexOf(".jpg")!=-1){
                            String imageLink;
                            String textTitle;
                            bl= false;
                            imageLink= publicMethod.dataLink(text);
                            textTitle= element.select("p.Image").text();
                            details.add(new Detail(textTitle, imageLink, bl));
                        }
                        else{
                            details.add(new Detail(element.text(), "", bl));
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return details;
        }

        @Override
        protected void onPostExecute(final ArrayList<Detail> strings) {
            super.onPostExecute(strings);
            // set tiêu đề
            tvShowTitle.setText(titleDetail);
            tvPubDateShow.setText(pubDateDetail);
            tvDesShow.setText(descriptionDetail);

            // click save
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), R.string.save, Toast.LENGTH_LONG).show();
                    itemSave= new SQLItemSave(getBaseContext());
                    List<ItemSave> itemSaveList;
                    itemSaveList= itemSave.getAllItemSave();
                    if(publicMethod.checkTitleItemSave(titleDetail, itemSaveList)==false){
                        itemSave.insertItemSave(titleDetail, documentItemSave);
                    }
                }
            });
            tvXemNhieu.setText("Xem Nhiều"); //setText
            //btZoom
            btZoomIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (textSize<=25F){
                        textSize+=2;
                        Toast.makeText(getBaseContext(), R.string.zoomIn, Toast.LENGTH_LONG).show();
                    }

                    else
                        Toast.makeText(getBaseContext(), "Max", Toast.LENGTH_LONG).show();
                    adapter= new ItemsDetailAdapter(getBaseContext(), strings, textSize);
                    recyclerViewDetail.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            btZoomOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(textSize>9){
                        textSize-=2;
                        Toast.makeText(getBaseContext(), R.string.zoomOut, Toast.LENGTH_LONG).show();
                    }

                    else
                        Toast.makeText(getBaseContext(), "Min", Toast.LENGTH_LONG).show();
                    adapter= new ItemsDetailAdapter(getBaseContext(), strings, textSize);
                    recyclerViewDetail.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
            //setAdapter cho rcvDetail
            adapter= new ItemsDetailAdapter(getBaseContext(), strings, textSize);
            recyclerViewDetail.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private class LoadRelatedItem extends AsyncTask<String, Void, ArrayList<ItemRelated>> {
        private static final String TAG = "LoadRelatedItem";

        @Override
        protected ArrayList<ItemRelated> doInBackground(String... strings) {
            Document document = null;
            ArrayList<ItemRelated> arrayListRelated = new ArrayList<>();
            try {
                document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Element container = document.select("section.container").first();
                    Elements li = container.getElementsByTag("li");

                    for (Element element : li) {
                        Element h4= element.selectFirst("h4");
                        Element tag_a = element.selectFirst("a");
                        String titleLi = publicMethod.dataTitle(h4.html());
                        String linkLi = tag_a.attr("href");
                        arrayListRelated.add(new ItemRelated(titleLi, linkLi));

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayListRelated;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemRelated> strings) {
            super.onPostExecute(strings);
            relatedItemAdapter = new RelatedItemAdapter(getBaseContext(), strings);
            recyclerViewRelatedItem.setAdapter(relatedItemAdapter);

            //click vào bài viết
            relatedItemAdapter.setIOnClickItem(new IOnClickItem() {
                @Override
                public void onClick(String title, String link) {
                    // insert to SQLClickHistory
                    sqlClickHistory = new SQLClickHistory(getBaseContext());
                    List<ItemRelated> list;
                    list = sqlClickHistory.getAllItem();
                    if (publicMethod.checkTitleItemClick(title, list) == false)
                        sqlClickHistory.insertItem(title, link);

                    // showDetail
                    Intent intentClick = new Intent(getBaseContext(), ShowDetail.class);
                    intentClick.putExtra("link", link);
                    startActivity(intentClick);
                    finish();
                }
            });
            relatedItemAdapter.notifyDataSetChanged();
        }
    }

}
