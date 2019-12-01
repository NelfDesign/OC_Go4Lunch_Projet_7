package fr.nelfdesign.go4lunch.ui.activity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.ui.fragments.MapFragment;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.ui.fragments.RestaurantListFragment;
import fr.nelfdesign.go4lunch.ui.fragments.WorkersFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment mFragment;

    @BindView(R.id.nav_view) BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (mFragment == null){
            mFragment = new MapFragment();
            configureFragment(mFragment);
        }

        navView.setOnNavigationItemSelectedListener(item ->
                updateMainFragment(item.getItemId()));

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
}
