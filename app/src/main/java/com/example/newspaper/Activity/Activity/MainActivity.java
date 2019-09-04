package com.example.newspaper.Activity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.example.newspaper.Activity.Define.Define;
import com.example.newspaper.Activity.Define.PublicMethod;
import com.example.newspaper.Activity.Fragment.FragmentHistory;
import com.example.newspaper.Activity.Fragment.FragmentHotVideos;
import com.example.newspaper.Activity.Fragment.FragmentItems;
import com.example.newspaper.Activity.Fragment.FragmentSave;
import com.example.newspaper.Activity.Interface.ItemClick;
import com.example.newspaper.Activity.Model.ItemRelated;
import com.example.newspaper.Activity.SQL.SQLClickHistory;
import com.example.newspaper.R;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ItemClick {
    private static final String TAG = "MainActivity";
    SQLClickHistory sqlClickHistory;
    PublicMethod publicMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getFragment(FragmentItems.newInstance(Define.RSS_to_Json_API +Define.RSS_cuoi));
        //startService(new Intent(getBaseContext(), ServiceNotification.class));
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
        if (id == R.id.item1) {
            Toast.makeText(getBaseContext(), R.string.newsSave, Toast.LENGTH_LONG).show();
            getFragment(FragmentSave.newInstance());
            getSupportActionBar().setTitle(R.string.newsSave);
        }
        else if(id==R.id.item2){
            Toast.makeText(getBaseContext(), R.string.newsClick, Toast.LENGTH_LONG).show();
            getFragment(FragmentHistory.newInstance());
            getSupportActionBar().setTitle(R.string.newsClick);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final ActionBar toolbar= getSupportActionBar();
        int id = item.getItemId();
        String urlGetData;

        switch (id){
            case R.id.menu1:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_trangchu;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu1);
                break;
            }
            case R.id.menu2:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thoisu;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu2);
                break;
            }
            case R.id.menu3:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thegioi;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu3);
                break;
            }case R.id.menu4:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_kinhdoanh;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu4);
                break;
            }
            case R.id.menu5:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_startup;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu5);
                break;
            }case R.id.menu6:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_giaitri;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu6);
                break;
            }case R.id.menu7:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_thethao;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu7);
                break;
            }case R.id.menu8:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_phapluat;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu8);
                break;
            }case R.id.menu9:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_giaoduc;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu9);
                break;
            }case R.id.menu10:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_suckhoe;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu10);
                break;
            }case R.id.menu11:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_doisong;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu11);
                break;
            }case R.id.menu12:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_dulich;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu12);
                break;
            }case R.id.menu13:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_khoahoc;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu13);
                break;
            }case R.id.menu14:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_sohoa;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu14);
                break;
            }case R.id.menu15:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_xe;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu15);
                break;
            }case R.id.menu16:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_ykien;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu16);
                break;
            }case R.id.menu17:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_tamsu;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu17);
                break;
            }case R.id.menu18:{
                urlGetData= Define.RSS_to_Json_API+ Define.RSS_cuoi;
                getFragment(FragmentItems.newInstance(urlGetData));
                toolbar.setTitle(R.string.menu18);
                break;
            }case R.id.menu19:{

                getFragment(FragmentHotVideos.newInstance());
                toolbar.setTitle(R.string.menu19);
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
    @Override
    public void onClick(String title, String link) {
        // insert to SQLClickHistory
        sqlClickHistory= new SQLClickHistory(getBaseContext());
        List<ItemRelated> list;
        list= sqlClickHistory.getAllItem();
        publicMethod= new PublicMethod();
        if(publicMethod.checkTitleItemClick(title, list)==false){
            sqlClickHistory.insertItem(title, link);
        }

        // showDetail
        Intent intent= new Intent(getBaseContext(), ShowDetail.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
}
