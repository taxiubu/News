package com.example.newspaper.Activity.Common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPData {
    static String stream= null;

    public HTTPData() {
    }

    public String getHTTPData(String urlString){
        try {
            URL url= new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();

            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStream in= new BufferedInputStream(connection.getInputStream());
                BufferedReader r= new BufferedReader(new InputStreamReader(in));
                StringBuilder strBuilder= new StringBuilder();
                String line;

                while ((line=r.readLine())!=null){
                    strBuilder.append(line);
                }
                stream= strBuilder.toString();
                connection.disconnect();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stream;
    }
}
