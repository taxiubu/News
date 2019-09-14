package com.example.newspaper.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newspaper.Activity.Adapter.ItemAdapter;
import com.example.newspaper.Activity.Common.HTTPData;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Interface.IOnClickItem;
import com.example.newspaper.Activity.Model.RootObject;
import com.example.newspaper.R;
import com.google.gson.Gson;

public class FragmentItems extends Fragment {
    private IOnClickItem listener;
    RootObject rssObject;
    RecyclerView recyclerView;
    PublicMethod publicMethod;
    public static FragmentItems newInstance(String urlGetData) {

        Bundle args = new Bundle();
        args.putSerializable("UrlGetData", urlGetData);
        FragmentItems fragment = new FragmentItems();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView = view.findViewById(R.id.rcv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //check Internet
        publicMethod= new PublicMethod();
        if(publicMethod.checkConnectInternet(getContext()))
            loadRSS();
        else
            Toast.makeText(getContext(), R.string.NoInternet, Toast.LENGTH_LONG).show();
        return view;
    }

    private void loadRSS() {
        AsyncTask<String, String, String> loadRSSAsync= new AsyncTask<String, String, String>() {
            ProgressDialog mDialong= new ProgressDialog(getContext());
            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPData httpData= new HTTPData();
                result= httpData.getHTTPData(strings[0]);
                return result;
            }

            @Override
            protected void onPreExecute() {
                mDialong.setMessage("Mạng lag chờ xíu ...");
                mDialong.show();
            }

            @Override
            protected void onPostExecute(String s) {
                mDialong.show();
                rssObject= new Gson().fromJson(s, RootObject.class);
                ItemAdapter itemAdapter= new ItemAdapter(rssObject, getContext());
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.setIOnClickItem(new IOnClickItem() {
                    @Override
                    public void onClick(String title, String link) {
                        listener.onClick(title, link);
                    }
                });
                itemAdapter.notifyDataSetChanged();
            }
        };

        String urlLink = (String) getArguments().getSerializable("UrlGetData");
        loadRSSAsync.execute(urlLink);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IOnClickItem) {
            listener = (IOnClickItem) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement");
        }
    }

}
