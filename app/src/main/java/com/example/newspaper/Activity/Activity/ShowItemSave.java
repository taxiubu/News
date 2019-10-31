package com.example.newspaper.Activity.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Adapter.AdapterItemsDetail;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.DialogCustom.DialogZoomImage;
import com.example.newspaper.Activity.Interface.IOnClickZoomImage;
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
    ImageView btZoomOutOff, btZoomInOff, btBrightness;
    PublicMethod publicMethod= new PublicMethod();
    Float sizeText= 15F;
    AdapterItemsDetail detailAdapter;
    RelativeLayout layoutBrightness;
    int check= 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_save);
        Toolbar toolbarActItemSave= findViewById(R.id.toolbarActItemSave);
        setSupportActionBar(toolbarActItemSave);
        tvTitleItemSave= findViewById(R.id.tvTitleItemSaveShow);
        tvDesItemSave= findViewById(R.id.tvDesItemSaveShow);
        tvPubDateItemSave= findViewById(R.id.tvPubDateItemSaveShow);
        btZoomInOff= findViewById(R.id.btZoomInOffline);
        btZoomOutOff= findViewById(R.id.btZoomOutOffline);
        layoutBrightness= findViewById(R.id.layoutBrightnessO);
        btBrightness= findViewById(R.id.btBrightnessO);

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
            if(article!=null){
                Elements lineDetail= article.select("p, table, div#article_content, div.width_common");
                for (Element element: lineDetail){
                    String imageLink=null;
                    String textTitle=null;
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

                //tiêu đề
                title= document.selectFirst("h1").text();
                des= document.selectFirst("p").text();
                pubDate= document.selectFirst("span").text();

            }
            //setText tiêu đề
            tvTitleItemSave.setText(title);
            tvDesItemSave.setText(des);
            tvPubDateItemSave.setText(pubDate);
        }

        //setAdapter
        detailAdapter= new AdapterItemsDetail(getBaseContext(), (ArrayList<Detail>) details, sizeText);
        rcvItemSaveDetail.setAdapter(detailAdapter);
        detailAdapter.setZoomImage(new IOnClickZoomImage() {
            @Override
            public void onClick(String linkImage) {
                showDialog(linkImage);
            }
        });

        //btZoom
        btZoomOutOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sizeText>13){
                    sizeText-=2;
                    Toast.makeText(getBaseContext(), R.string.zoomOut, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(), "Min", Toast.LENGTH_LONG).show();
                detailAdapter= new AdapterItemsDetail(getBaseContext(), (ArrayList<Detail>) details, sizeText);
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
                detailAdapter= new AdapterItemsDetail(getBaseContext(), (ArrayList<Detail>) details, sizeText);
                rcvItemSaveDetail.setAdapter(detailAdapter);
            }
        });

        //độ sáng
        layoutBrightness.setVisibility(View.GONE);
        btBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check== 1) {
                    btBrightness.setBackgroundResource(R.drawable.light_white_24dp);
                    check = 0;
                    layoutBrightness.setVisibility(View.GONE);
                }
                else {
                    btBrightness.setBackgroundResource(R.drawable.dark_white_24dp);
                    check= 1;
                    layoutBrightness.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getBaseContext(), R.string.complete, Toast.LENGTH_LONG).show();
            }
        });

    }
    public void showDialog(String linkImage){
        FragmentManager fragmentManager= getSupportFragmentManager();
        DialogZoomImage dialogZoomImage= DialogZoomImage.newInstance(linkImage);
        dialogZoomImage.show(fragmentManager, null);
    }
}
