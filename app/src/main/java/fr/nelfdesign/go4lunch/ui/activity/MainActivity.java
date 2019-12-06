package fr.nelfdesign.go4lunch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
import fr.nelfdesign.go4lunch.utils.Utils;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    //FIELDS
    private Fragment mFragment;
    private ImageView mImageViewNav;
    private TextView mTextViewNavName;
    private TextView mTextViewNavMail;
    private FirebaseUser user;

    @BindView(R.id.nav_view) BottomNavigationView navView;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.navigation_view) NavigationView mNavigationView;

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
        }

        user = this.getCurrentUser();
        configureFragment(mFragment);
        this.configureToolBar("I'm hungry");

        navView.setOnNavigationItemSelectedListener(this::updateMainFragment);

        drawerLayoutConfiguration();
        configureNavigationHeader();

    }

    private void configureNavigationHeader() {
        mNavigationView.setNavigationItemSelectedListener(this::updateMainFragment);
        this.updateNavigationHeader();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_search:
                Timber.i( "Search");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean updateMainFragment(MenuItem menuItem){
        switch (menuItem.getItemId()) {
            case R.id.navigation_map:
                this.mFragment = new MapFragment();
                configureFragment(mFragment);
                break;
            case R.id.navigation_list | R.id.nav_lunch:
                this.mFragment = new RestaurantListFragment();
                configureFragment(mFragment);
                break;
            case R.id.navigation_workers:
                this.mFragment = new WorkersFragment();
                configureFragment(mFragment);
                break;
            case R.id.logout:
                this.deleteCurrentUserAccount();
                break;
        }
        // Closes the NavigationView at the end of the user action
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
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

    private void drawerLayoutConfiguration() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        this.mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateNavigationHeader() {
        final View headerNav = mNavigationView.getHeaderView(0);

        //XML id for update data
        mImageViewNav = headerNav.findViewById(R.id.image_navDrawer);
        mTextViewNavName = headerNav.findViewById(R.id.name_text);
        mTextViewNavMail = headerNav.findViewById(R.id.mail_text);

        if (user != null) {
            // ImageView: User image
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(this.mImageViewNav);
            }

            // TextView: Username and email
            final String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.no_name_found) :
                    user.getDisplayName();

            final String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_mail_found) :
                    user.getEmail();

            this.mTextViewNavName.setText(username);
            this.mTextViewNavMail.setText(email);
        }
    }

    /**
     * Signs out the current user of Firebase
     */
    private void signOutCurrentUser() {
        Utils.showSnackBar(this.mDrawerLayout, "sign out");

        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Deletes the current user account
     */
    private void deleteCurrentUserAccount() {
        if (user != null){
            user.delete()
                    .addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            Utils.showSnackBar(this.mDrawerLayout,"Deleted account");
                            startAuthActivity();
                        }
                    });
        }
    }

    private void startAuthActivity(){
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

}
