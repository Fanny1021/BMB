package com.fanny.bmb.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fanny.bmb.R;
import com.fanny.bmb.fragment.CenterFragment;
import com.fanny.bmb.fragment.DeviceFragment;
import com.fanny.bmb.fragment.MarketFragment;
import com.fanny.bmb.fragment.MessageFragment;
import com.fanny.bmb.fragment.UserFragment;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.main_fragment_container)
//    FrameLayout mainFragmentContainer;
//    @BindView(R.id.space)
//    SpaceNavigationView space;

    private SpaceNavigationView spaceNavigationView;

    private ArrayList<Fragment> fragmentLists;
    final List<Fragment> fragments=new ArrayList<>();

    private boolean iscenter=false;
    private RelativeLayout mbc;
    private SpaceTabLayout tabLayout;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);
        tabLayout.initialize(viewPager,getSupportFragmentManager(),fragmentLists,savedInstanceState);

        initActionBar();

        UserFragment userFragment=new UserFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_menu,userFragment).commit();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        int openDrawerContentDescRes=R.string.open;
        int closeDrawerContentDescRes=R.string.close;
        ActionBarDrawerToggle arrowBtn=new ActionBarDrawerToggle(this, drawerLayout,openDrawerContentDescRes,closeDrawerContentDescRes);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        arrowBtn.syncState();
        drawerLayout.addDrawerListener(arrowBtn);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                View menu=findViewById(R.id.fl_menu);
                if(drawerLayout.isDrawerOpen(menu)){
                    drawerLayout.closeDrawer(menu);
                }else {
                    drawerLayout.openDrawer(menu);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActionBar() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Fan");
        actionBar.setLogo(R.drawable.ic_alert);

        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void initFragment() {
        fragmentLists=new ArrayList<>();
        fragmentLists.add(new DeviceFragment());
        fragmentLists.add(new MessageFragment());
        fragmentLists.add(new MarketFragment());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);


    }




}
