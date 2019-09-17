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
import androidx.appcompat.widget.Toolbar;
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
import com.github.ybq.android.spinkit.SpinKitView;

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
    SpinKitView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        Toolbar toolbarActDetail = findViewById(R.id.toolbarActDetail);
        setSupportActionBar(toolbarActDetail);
        //getSupportActionBar().hide();
        // loading
        loading= findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

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


        //rcv phần thông tin
        String link= getIntent().getStringExtra("link");
        recyclerViewDetail= findViewById(R.id.rcvDetail);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        recyclerViewDetail.setLayoutManager(layoutManager1);

        //rcv phần bài viết liên quan
        recyclerViewRelatedItem= findViewById(R.id.rcvRelatedItem);
        RecyclerView.LayoutManager layoutManager2= new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        recyclerViewRelatedItem.setLayoutManager(layoutManager2);

        //checkInternet
        if(publicMethod.checkConnectInternet(getBaseContext())==false){
            Toast.makeText(getBaseContext(), R.string.NoInternet, Toast.LENGTH_LONG).show();
            tvXemNhieu.setText("");
        }
        else {
            new LoadDetail().execute(link);
            new LoadRelatedItem().execute(link);
        }

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
                    if(container!=null){
                        Element sidebar1= container.select("section.sidebar_1").first();
                        documentItemSave= sidebar1.html();
                        titleDetail= sidebar1.selectFirst("h1").text();
                        descriptionDetail= sidebar1.selectFirst("p").text();
                        pubDateDetail= sidebar1.selectFirst("span").text();

                        Element article= sidebar1.select("article").first();
                        Elements lineDetail= article.select("p.Normal, table, div#article_content, p.MsNormal");
                        for (Element element: lineDetail){
                            String imageLink= null;
                            String textTitle= null;
                            String text= element.html();
                            Boolean bl= true;
                            if(text.indexOf(".jpg")!=-1){
                                bl= false;
                                imageLink= publicMethod.dataLinkJPG(text);
                                textTitle= element.select("p.Image").text();

                            }
                            else if(text.indexOf(".png")!=-1){
                                bl= false;
                                imageLink= publicMethod.dataLinkPNG(text);
                                textTitle= element.select("p.Image").text();
                            }
                            else{
                                textTitle= element.text();
                            }
                            details.add(new Detail(textTitle, imageLink, bl));
                        }
                    }
                    // fix null container
                    else {
                        container= document.select("section#wrapper_container").first();
                        if(container!=null){
                            titleDetail= container.select("div.block_title_detail").first().text();
                            pubDateDetail= container.select("div.author").first().text().replace("&nbsp", "");
                            descriptionDetail= container.selectFirst("h2").text();
                            Elements lineDetail= container.select("p.Normal, p.MsNormal, div.thumb_detail_top, table");
                            for(Element element:lineDetail){
                                String text= element.html();
                                Boolean bl= true;
                                String imageLink=null;
                                String textTitle=null;
                                if(text.indexOf(".jpg")!=-1){
                                    bl= false;
                                    imageLink= publicMethod.dataLinkJPG(text);
                                    textTitle= element.select("div.caption_thumb_detail_top").text();

                                }
                                else if(text.indexOf(".png")!=-1){
                                    bl= false;
                                    imageLink= publicMethod.dataLinkPNG(text);
                                    textTitle= element.select("div.caption_thumb_detail_top").text();
                                }
                                else{
                                    textTitle= element.text();
                                }
                                details.add(new Detail(textTitle, imageLink, bl));
                            }
                            documentItemSave= container.html();
                        }
                        else {
                                //Element containFix= document.select("section.top_detail clearfix").first();
                            titleDetail= document.select("h1").first().text();
                            descriptionDetail= document.select("div.lead_detail").first().text();
                            pubDateDetail= document.select("span.time").first().text();

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
            if(strings!=null)
                loading.setVisibility(View.GONE);
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
            tvXemNhieu.setText(R.string.more); //setText
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
                    String titleLi= null;
                    String linkLi = null;
                    if(container!=null){
                        Elements li = container.getElementsByTag("li");
                        for (Element element : li) {
                            Element h4= element.selectFirst("h4");
                            Element tag_a = element.selectFirst("a");
                            titleLi = publicMethod.dataTitle(h4.html());
                            linkLi = tag_a.attr("href");
                            //String linkLi = publicMethod.dataLinkJPG(h4.html());
                            arrayListRelated.add(new ItemRelated(titleLi, linkLi));

                        }
                    }
                    // fix null container
                    else {
                        container= document.select("section#wrapper_container").first();
                        if(container!=null){
                            Element xemthem = container.select("div.list_xemthem_newver").first();
                            Elements li= xemthem.getElementsByTag("li");
                            for(Element element:li){
                                Element tag_a = element.selectFirst("a");
                                linkLi= tag_a.attr("href");
                                titleLi= tag_a.text();
                                arrayListRelated.add(new ItemRelated(titleLi, linkLi));
                            }
                        }
                        else {
                            Element xemthem= document.select("div.container_section clearfix").first();
                            Elements li= xemthem.getElementsByTag("article");
                            for(Element element:li){
                                titleLi= element.selectFirst("a").attr("href");
                                linkLi= element.selectFirst("a").attr("alt");
                                arrayListRelated.add(new ItemRelated(titleLi, linkLi));
                            }
                        }

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
                    if (publicMethod.checkTitleItemClick(title, list)){
                        sqlClickHistory.deleteItemClick(title);
                        sqlClickHistory.insertItem(title, link);
                    }
                    else
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
