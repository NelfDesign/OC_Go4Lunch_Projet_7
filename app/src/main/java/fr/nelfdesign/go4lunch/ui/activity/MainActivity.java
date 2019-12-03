package fr.nelfdesign.go4lunch.ui.activity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.ui.fragments.MapFragment;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.ui.fragments.RestaurantListFragment;
import fr.nelfdesign.go4lunch.ui.fragments.WorkersFragment;

public class MainActivity extends BaseActivity {

    private Fragment mFragment;

    @BindView(R.id.nav_view) BottomNavigationView navView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    // base activity method
    @Override
    public int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mFragment == null){
            mFragment = new MapFragment();
            configureFragment(mFragment);
        }

        this.configureToolBar("I'm hungry");

        navView.setOnNavigationItemSelectedListener(item ->
                updateMainFragment(item.getItemId()));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    private Boolean updateMainFragment(Integer integer){
        switch (integer) {
            case R.id.navigation_map:
                this.mFragment = new MapFragment();
                configureFragment(mFragment);
                break;
            case R.id.navigation_list:
                this.mFragment = new RestaurantListFragment();
                configureFragment(mFragment);
                break;
            case R.id.navigation_workers:
                this.mFragment = new WorkersFragment();
                configureFragment(mFragment);
                break;
        }
        return true;
    }

    private void configureFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

}
