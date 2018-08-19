package tanvir.lostandfound.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import tanvir.lostandfound.Fragment.FoundFragment;
import tanvir.lostandfound.Fragment.LostFragment;
import tanvir.lostandfound.Adapter.ViewPagerAdapter;
import tanvir.lostandfound.HelperClass.EnterOrBackFromActivity;
import tanvir.lostandfound.HelperClass.ProgressDialog;
import tanvir.lostandfound.R;

public class HomePage extends AppCompatActivity {


    private Toolbar foundFragmentToolBar;
    private Toolbar lostFragmentToolBar;
    private Toolbar placeInfoToolBarWithPlaceName;
    private FloatingActionButton homePageFAB;
    String activityOnWhichFragment = "Lost";

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    EnterOrBackFromActivity enterOrBackFromActivity;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        context = HomePage.this;
        enterOrBackFromActivity = new EnterOrBackFromActivity();
        homePageFAB = findViewById(R.id.homePageFAB);
        foundFragmentToolBar = findViewById(R.id.toolbarLayoutForFoundFragment);
        lostFragmentToolBar = findViewById(R.id.toolbarLayoutForLostFrahment);
        setSupportActionBar(foundFragmentToolBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        changeNavigationDrawer("Lost");
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new LostFragment(), "Lost");
        viewPagerAdapter.addFragments(new FoundFragment(), "Found");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        lostFragmentToolBar.setVisibility(View.VISIBLE);
        foundFragmentToolBar.setVisibility(View.GONE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    changeNavigationDrawer("Found");
                    activityOnWhichFragment = "Found";
                    lostFragmentToolBar.setVisibility(View.GONE);
                    foundFragmentToolBar.setVisibility(View.VISIBLE);
                } else {
                    activityOnWhichFragment = "Lost";
                    changeNavigationDrawer("Lost");
                    lostFragmentToolBar.setVisibility(View.VISIBLE);
                    foundFragmentToolBar.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });

    }

    public void changeNavigationDrawer(String fragmentName) {
        actionBarDrawerToggle = null;
        Log.d("fragmentName",fragmentName);
        if (fragmentName.contains("Found"))
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, foundFragmentToolBar, R.string.drawer_open
                    , R.string.drawer_close);
        else if (fragmentName.contains("Lost"))
        {
            Log.d("enterFragmentName",fragmentName);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, lostFragmentToolBar, R.string.drawer_open
                    , R.string.drawer_close);
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.HOmeId).setChecked(true);
        nav_Menu.findItem(R.id.UserProfile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                EnterOrBackFromActivity enterOrBackFromActivity = new EnterOrBackFromActivity();
                enterOrBackFromActivity.startUserProfileActivity(HomePage.this);
                return true;
            }
        });
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    public void startLostOrFoundPostCreationActivity(View view) {
        if (activityOnWhichFragment.contains("Lost"))
        {
            Intent myIntent = new Intent(this, UserPostCreationActivity.class);
            myIntent.putExtra("cameFromWhere","LostFragment");
            startActivity(myIntent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);

        }

        else
        {
            Intent myIntent = new Intent(this, UserPostCreationActivity.class);
            myIntent.putExtra("cameFromWhere","FoundFragment");
            startActivity(myIntent);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
        }

    }
}
