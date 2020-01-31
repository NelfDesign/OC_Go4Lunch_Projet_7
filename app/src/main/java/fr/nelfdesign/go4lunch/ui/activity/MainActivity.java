package fr.nelfdesign.go4lunch.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import fr.nelfdesign.go4lunch.BuildConfig;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.apiFirebase.WorkersHelper;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.settings.activity.SettingsActivity;
import fr.nelfdesign.go4lunch.ui.fragments.MapFragment;
import fr.nelfdesign.go4lunch.ui.fragments.RestaurantListFragment;
import fr.nelfdesign.go4lunch.ui.fragments.WorkersFragment;
import fr.nelfdesign.go4lunch.utils.Utils;

import static androidx.core.view.GravityCompat.START;

public class MainActivity extends BaseActivity {

    //FIELDS
    private Fragment mFragment;
    private FirebaseUser user;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_navigation)
    NavigationView mNavigationView;

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

        if (mFragment == null) {
            mFragment = new MapFragment();
        }

        user = this.getCurrentUser();
        configureFragment(mFragment);
        this.configureToolBar(getString(R.string.I_m_hungry));

        navView.setOnNavigationItemSelectedListener(this::updateMainFragment);

        drawerLayoutConfiguration();
        configureNavigationHeader();

        // Initialize the SDK for autocomplete
        Places.initialize(getApplicationContext(), BuildConfig.google_maps_key);
    }

    /**
     * configure navigation drawer header
     */
    private void configureNavigationHeader() {
        mNavigationView.setNavigationItemSelectedListener(this::updateMainFragment);
        this.updateNavigationHeader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.map_Fragment);
        Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Intent intent = new Intent(this, RestaurantDetail.class);
                intent.putExtra("placeId", place.getId());
                startActivity(intent);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_query), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_query), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creates a MenuInflater to add the menu xml file into the Toolbar
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_search) {
            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

            // Define the region
            RectangularBounds bounds = RectangularBounds.newInstance(
                    new LatLng(47.2184, -1.5536),
                    new LatLng(47.2205, -1.5435));

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY, fields)
                    .setLocationBias(bounds)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .build(this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * update main fragment when item is clicked
     *
     * @param menuItem item to click on
     * @return new fragment
     */
    private Boolean updateMainFragment(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_map:
                this.mFragment = new MapFragment();
                configureFragment(mFragment);
                mToolbar.setTitle(getResources().getString(R.string.I_m_hungry));
                break;
            case R.id.navigation_list:
                this.mFragment = new RestaurantListFragment();
                configureFragment(mFragment);
                mToolbar.setTitle(getResources().getString(R.string.I_m_hungry));
                break;
            case R.id.navigation_workers:
                this.mFragment = new WorkersFragment();
                configureFragment(mFragment);
                mToolbar.setTitle(getString(R.string.workers_toolbar));
                break;
            case R.id.logout:
                this.signOutCurrentUser();
                break;
            case R.id.nav_lunch:
                this.showMyRestaurantChoice();
                break;
            case R.id.nav_favorite:
                this.showMyFavoriteRestaurant();
                break;
            case R.id.nav_settings:
                this.startActivitySettings();
        }
        // Closes the DrawerNavigationView when the user click on an item
        if (this.mDrawerLayout.isDrawerOpen(START)) {
            this.mDrawerLayout.closeDrawer(START);
        }
        return true;
    }

    /**
     * create new fragment
     *
     * @param fragment to configure
     */
    private void configureFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(START)) {
            mDrawerLayout.closeDrawer(START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * configuration of the drawer layout
     */
    private void drawerLayoutConfiguration() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        this.mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * Update header with user information
     */
    private void updateNavigationHeader() {
        final View headerNav = mNavigationView.getHeaderView(0);

        //XML id for update data
        ImageView imageViewNav = headerNav.findViewById(R.id.image_navDrawer);
        TextView textViewNavName = headerNav.findViewById(R.id.name_text);
        TextView textViewNavMail = headerNav.findViewById(R.id.mail_text);

        if (user != null) {
            // ImageView: User image
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .circleCrop()
                        .into(imageViewNav);
            }

            // TextView: Username and email
            final String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.no_name_found) :
                    user.getDisplayName();

            final String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_mail_found) :
                    user.getEmail();

            textViewNavName.setText(username);
            textViewNavMail.setText(email);
        }
    }

    /**
     * Signs out the current user of Firebase
     */
    private void signOutCurrentUser() {
        Utils.showSnackBar(this.mDrawerLayout, "sign out");
        if (user != null) {
            FirebaseAuth.getInstance().signOut();
            startAuthActivity();
            finishAffinity();
        }
    }

    // Intent used for navigation item
    private void startAuthActivity() {
        Intent intent = new Intent(this, ConnexionActivity.class);
        startActivity(intent);
    }

    private void showMyFavoriteRestaurant() {
        Intent intent = new Intent(this, FavoritesRestaurant.class);
        startActivity(intent);
    }

    private void startActivitySettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * get a user query to show if the user has chosen a restaurant and redirect if able
     */
    private void showMyRestaurantChoice() {
        Query query = WorkersHelper.getAllWorkers().whereEqualTo("name",
                Objects.requireNonNull(getCurrentUser()).getDisplayName());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    if (!Objects.equals(document.get("placeId"), "")) {
                        Intent intent = new Intent(this.getBaseContext(), RestaurantDetail.class);
                        intent.putExtra("placeId", Objects.requireNonNull(document.get("placeId")).toString());
                        startActivity(intent);
                    } else {
                        Utils.showSnackBar(this.mDrawerLayout, getResources().getString(R.string.no_choice_restaurant_workers));
                    }
                }
            }
        });
    }
}
