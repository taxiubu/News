package com.example.newspaper.Activity.Define;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.Model.ItemSave;

import java.util.List;

public class PublicMethod {
    public boolean checkTitleItemClick(String title, List<ItemRelated> list){

        for (ItemRelated itemRelated:list){
            if(title.equals(itemRelated.getTitle()))
                return true;

        }
        return false;
    }

    public boolean checkTitleItemSave(String title, List<ItemSave> list){

        for (ItemSave itemSave:list){
            if(title.equals(itemSave.getTitle()))
                return true;

        }
        return false;
    }

    public String dataLinkJPG(String s){
        int indexFirst= s.indexOf("https");
        int indexLast= s.indexOf(".jpg");
        return s.substring(indexFirst, indexLast+4);
    }
    public String dataLinkPNG(String s){
        int indexFirst= s.indexOf("https");
        int indexLast= s.indexOf(".png");
        return s.substring(indexFirst, indexLast+4);
    }
    public boolean checkConnectInternet(Context context){
        boolean connected= false;
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED ||
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.CONNECTED)
            connected= true;
        return connected;
    }
    public String dataTitle(String s){
        int indexFirst= s.indexOf("title=");
        int indexLast= s.indexOf("\"", indexFirst+10);
        return s.substring(indexFirst+7, indexLast);
    }

}
