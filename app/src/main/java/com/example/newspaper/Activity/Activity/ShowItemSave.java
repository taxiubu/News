package com.example.newspaper.Activity.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Adapter.ItemsDetailAdapter;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Model.Detail;
import com.example.newspaper.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ShowItemSave extends AppCompatActivity {
    TextView tvTitleItemSave, tvDesItemSave, tvPubDateItemSave;
    RecyclerView rcvItemSaveDetail;
    ImageView btBack;
    String title, des, pubDate;
    List<Detail> details;
    ImageView btZoomOutOff, btZoomInOff;
    PublicMethod publicMethod= new PublicMethod();
    Float sizeText= 15F;
    ItemsDetailAdapter detailAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_save);
        getSupportActionBar().hide();
        tvTitleItemSave= findViewById(R.id.tvTitleItemSaveShow);
        tvDesItemSave= findViewById(R.id.tvDesItemSaveShow);
        tvPubDateItemSave= findViewById(R.id.tvPubDateItemSaveShow);
        btZoomInOff= findViewById(R.id.btZoomInOffline);
        btZoomOutOff= findViewById(R.id.btZoomOutOffline);

        //btBack
        btBack= findViewById(R.id.btBackOffline);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //setLayoutManager
        rcvItemSaveDetail= findViewById(R.id.rcvItemSaveDetail);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false);
        rcvItemSaveDetail.setLayoutManager(layoutManager);

        //lấy chuỗi rồi thao tác ))
        details= new ArrayList<>();
        String html= getIntent().getStringExtra("document");
        Document document= Jsoup.parse(html);
        if(document!=null){
            Element article= document.select("article").first();
            Elements lineDetail= article.select("p, table, div#article_content");
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

            //setText tiêu đề
            title= document.selectFirst("h1").text();
            des= document.selectFirst("p").text();
            pubDate= document.selectFirst("span").text();
            tvTitleItemSave.setText(title);
            tvDesItemSave.setText(des);
            tvPubDateItemSave.setText(pubDate);
        }

        //setAdapter
        detailAdapter= new ItemsDetailAdapter(getBaseContext(), (ArrayList<Detail>) details, sizeText);
        rcvItemSaveDetail.setAdapter(detailAdapter);

        //btZoom
        btZoomOutOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sizeText>9){
                    sizeText-=2;
                    Toast.makeText(getBaseContext(), R.string.zoomOut, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Min", Toast.LENGTH_LONG).show();
                detailAdapter= new ItemsDetailAdapter(getBaseContext(), (ArrayList<Detail>) details, sizeText);
                rcvItemSaveDetail.setAdapter(detailAdapter);
            }
        });
        btZoomInOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sizeText<=25){
                    sizeText+=2;
                    Toast.makeText(getBaseContext(), R.string.zoomIn, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Max", Toast.LENGTH_LONG).show();
                detailAdapter= new ItemsDetailAdapter(getBaseContext(), (ArrayList<Detail>) details, sizeText);
                rcvItemSaveDetail.setAdapter(detailAdapter);
            }
        });
    }
}
