package com.example.newspaper.Activity.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.newspaper.Activity.Adapter.ItemsDetailAdapter;
import com.example.newspaper.Activity.Adapter.RelatedItemAdapter;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Interface.ItemClick;
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
    ImageView btBack;
    TextView btSave;
    TextView tvShowTitle, tvPubDateShow, tvDesShow;
    RecyclerView recyclerViewDetail, recyclerViewRelatedItem;
    ItemsDetailAdapter adapter;
    RelatedItemAdapter relatedItemAdapter;
    String titleDetail, descriptionDetail, pubDateDetail;
    SQLItemSave itemSave;
    String documentItemSave;
    SQLClickHistory sqlClickHistory;
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
        btSave= findViewById(R.id.btSave);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                    Element last= article.selectFirst("p");
                    String className= last.className();
                    if(className.equals("Normal")){
                        last= article.selectFirst("p").lastElementSibling();
                    }
                    else {
                        last= article.select("p.author").first();
                    }
                    Elements lineDetail= last.siblingElements();

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
                    details.add(new Detail(last.text(), "", true));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return details;
        }

        @Override
        protected void onPostExecute(ArrayList<Detail> strings) {
            super.onPostExecute(strings);
            // set tiêu đề
            tvShowTitle.setText(titleDetail);
            tvPubDateShow.setText(pubDateDetail);
            tvDesShow.setText(descriptionDetail);

            // click save
            btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getBaseContext(), "Save", Toast.LENGTH_LONG).show();
                    itemSave= new SQLItemSave(getBaseContext());
                    List<ItemSave> itemSaveList;
                    itemSaveList= itemSave.getAllItemSave();
                    if(publicMethod.checkTitleItemSave(titleDetail, itemSaveList)==false){
                        itemSave.insertItemSave(titleDetail, documentItemSave);
                    }
                }
            });

            //setAdapter cho rcvDetail
            adapter= new ItemsDetailAdapter(getBaseContext(), strings);
            recyclerViewDetail.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private class LoadRelatedItem extends AsyncTask<String, Void, ArrayList<ItemRelated>> {
        private static final String TAG = "LoadRelatedItem";

        @Override
        protected ArrayList<ItemRelated> doInBackground(String... strings) {
            Document document = null;
            ArrayList<ItemRelated> arrayList = new ArrayList<>();
            try {
                document = Jsoup.connect(strings[0]).get();
                if (document != null) {
                    Element container = document.select("section.container").first();
                    Elements li = container.getElementsByTag("li");

                    for (Element element : li) {
                        Element tag_a = element.selectFirst("a");
                        String titleLi = tag_a.attr("title");
                        String linkLi = tag_a.attr("href");
                        if (titleLi != null && linkLi != null) {
                            arrayList.add(new ItemRelated(titleLi, linkLi));
                        }

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<ItemRelated> strings) {
            super.onPostExecute(strings);
            relatedItemAdapter = new RelatedItemAdapter(getBaseContext(), strings);
            recyclerViewRelatedItem.setAdapter(relatedItemAdapter);

            //click vào bài viết
            relatedItemAdapter.setItemClick(new ItemClick() {
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
