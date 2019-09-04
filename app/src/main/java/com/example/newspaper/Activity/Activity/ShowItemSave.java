package com.example.newspaper.Activity.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    PublicMethod publicMethod= new PublicMethod();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_save);
        getSupportActionBar().hide();
        tvTitleItemSave= findViewById(R.id.tvTitleItemSaveShow);
        tvDesItemSave= findViewById(R.id.tvDesItemSaveShow);
        tvPubDateItemSave= findViewById(R.id.tvPubDateItemSaveShow);
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

            //setText tiêu đề
            title= document.selectFirst("h1").text();
            des= document.selectFirst("p").text();
            pubDate= document.selectFirst("span").text();
            tvTitleItemSave.setText(title);
            tvDesItemSave.setText(des);
            tvPubDateItemSave.setText(pubDate);
        }

        //setAdapter
        ItemsDetailAdapter detailAdapter= new ItemsDetailAdapter(getBaseContext(), (ArrayList<Detail>) details);
        rcvItemSaveDetail.setAdapter(detailAdapter);
    }
}
