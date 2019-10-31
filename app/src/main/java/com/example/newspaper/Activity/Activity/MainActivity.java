package com.example.newspaper.Activity.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.newspaper.Activity.Adapter.SliderAdapter;
import com.example.newspaper.Activity.Define.Define;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Fragment.FragmentHistory;
import com.example.newspaper.Activity.Fragment.FragmentItems;
import com.example.newspaper.Activity.Fragment.FragmentSave;
import com.example.newspaper.Activity.Fragment.FragmentSearch;
import com.example.newspaper.Activity.Interface.IOnClickItem;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.SQL.SQLClickHistory;
import com.example.newspaper.R;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IOnClickItem {
    private static final String TAG = "MainActivity";
    SQLClickHistory sqlClickHistory;
    PublicMethod publicMethod;
    SliderView imageToolbar;
    Disposable internet;
    FrameLayout contain;
    Toolbar toolbar;
    EditText etSearch;
    ImageView btSearch;
    RelativeLayout layoutSearch;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getFragment(FragmentItems.newInstance(Define.RSS_to_Json_API +Define.RSS_thoisu));
        //startService(new Intent(getBaseContext(), ServiceNotification.class));
        findViewByID();
        sliderShow();

        //search
        etSearch.setInputType(InputType.TYPE_NULL);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
                etSearch.setText("");
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                String titleSearch= etSearch.getText().toString();
                getFragment(FragmentSearch.newInstance(titleSearch));
                etSearch.setInputType(InputType.TYPE_NULL);
                etSearch.setShowSoftInputOnFocus(false);
                Toast.makeText(getBaseContext(), R.string.search, Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.item1:{
                Toast.makeText(getBaseContext(), R.string.newsSave, Toast.LENGTH_LONG).show();
                getFragment(FragmentSave.newInstance());
                toolbar.setTitle(R.string.newsSave);
                layoutSearch.setVisibility(View.GONE);
                break;
            }
            case R.id.item2:{
                Toast.makeText(getBaseContext(), R.string.newsClick, Toast.LENGTH_LONG).show();
                getFragment(FragmentHistory.newInstance());
                toolbar.setTitle(R.string.newsClick);
                layoutSearch.setVisibility(View.GONE);
                break;
            }
            case R.id.Vietnamese:{
                changeLanguage("vi");
                break;
            }
            case R.id.English:{
                changeLanguage("nv-rUS");
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //final ActionBar toolbar= getSupportActionBar();
        int id = item.getItemId();
        String urlGetData;

        switch (id){
            case R.id.menu1:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_trangchu;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.menu2:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thoisu;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.menu3:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thegioi;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu4:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_kinhdoanh;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.menu5:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_startup;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu6:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_giaitri;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu7:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thethao;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu8:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_phapluat;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu9:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_giaoduc;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu10:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_suckhoe;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu11:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_doisong;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu12:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_dulich;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu13:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_khoahoc;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu14:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_sohoa;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu15:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_xe;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu16:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_ykien;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu17:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_tamsu;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }case R.id.menu18:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_cuoi;
                getFragment(FragmentItems.newInstance(urlGetData));
                layoutSearch.setVisibility(View.VISIBLE);
                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getFragment(Fragment fragment){
        try{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contain,fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "getFragment: "+e.getMessage());
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        internet= ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnected) throws Exception {
                        if(isConnected==false){
                            diaLogNoInternet();
                        }
                        else
                        {
                            contain.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }*/

    @Override
    public void onClick(String title, String link) {
        // insert to SQLClickHistory
        sqlClickHistory= new SQLClickHistory(getBaseContext());
        List<ItemRelated> list;
        list= sqlClickHistory.getAllItem();
        publicMethod= new PublicMethod();
        if(publicMethod.checkTitleItemClick(title, list)){
            sqlClickHistory.deleteItemClick(title);
            sqlClickHistory.insertItem(title, link);
        }
        else
            sqlClickHistory.insertItem(title, link);

        // showDetail
        Intent intent= new Intent(getBaseContext(), ShowDetail.class);
        intent.putExtra("link", link);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
    }
    //chọn ngôn ngữ
    public void changeLanguage(String language){
        Locale locale= new Locale(language);
        Configuration configuration= new Configuration();
        configuration.locale= locale;
        getBaseContext().getResources().updateConfiguration(
                configuration, getBaseContext().getResources().getDisplayMetrics()
        );
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
    //AlertDialong
    public void diaLogNoInternet(){
        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setTitle(R.string.NoInternet);
        dialog.setMessage(R.string.messDialog);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
    public void showKeyboard(){
        etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        etSearch.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(etSearch, InputMethodManager.SHOW_FORCED);
    }
    public void sliderShow(){
        SliderAdapter sliderAdapter= new SliderAdapter(getBaseContext());
        imageToolbar.setSliderAdapter(sliderAdapter);

        imageToolbar.setIndicatorAnimation(IndicatorAnimations.SLIDE);
        imageToolbar.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }
    private void findViewByID(){
        imageToolbar= findViewById(R.id.imageToolbar);
        layoutSearch= findViewById(R.id.layoutSearch);
        etSearch= findViewById(R.id.etSearch);
        contain= findViewById(R.id.contain);
        btSearch= findViewById(R.id.btSearch);
    }


}

