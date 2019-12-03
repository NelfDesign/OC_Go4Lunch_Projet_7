package fr.nelfdesign.go4lunch.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import fr.nelfdesign.go4lunch.R;

/**
 * Created by Nelfdesign at 01/12/2019
 * fr.nelfdesign.go4lunch.base
 */
public abstract class BaseActivity extends AppCompatActivity {

    // fields
    protected FirebaseAuth mFirebaseAuth;

    // Methods
    public abstract int getActivityLayout();
    @Nullable
    protected abstract Toolbar getToolbar();

    // --------------------
    // Activity
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getActivityLayout());
        ButterKnife.bind(this); //Configure Butterknife
        this.configureFirebaseAuth(); // configure firebase
    }


    // --------------------
    // UI
    // --------------------

    protected void configureToolBar(String text) {
        // If ToolBar exists
        if (this.getToolbar() != null) {
            getToolbar().setTitle(text);
            setSupportActionBar(this.getToolbar());
        }
    }

    /**
     * Configures the {@link FirebaseAuth}
     */
    private void configureFirebaseAuth() {
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }
    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser(){ return mFirebaseAuth.getCurrentUser(); }

    protected Boolean isCurrentUserLogged(){ return (this.getCurrentUser() != null); }

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }
}
