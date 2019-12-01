package fr.nelfdesign.go4lunch.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.nelfdesign.go4lunch.R;

public class ConnexionActivity extends AppCompatActivity {

    @BindView(R.id.layout_main)
    ConstraintLayout mConstraintLayout;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.main_activity_google_login_button)
    public void onClickLoginButton() {
       //Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivity();
        this.startMainActivity();
    }

    @OnClick(R.id.main_activity_facebook_login_button)
    public void onClickFacebookLoginButton() {
        //Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityFacebook();
    }

    @OnClick(R.id.main_activity_twitter_login_button)
    public void onClickTwitterLoginButton() {
        //Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityTwitter();
    }

    @OnClick(R.id.main_activity_email_login_button)
    public void onClickMailLoginButton() {
        //Launch Sign-In Activity when user clicked on Login Button
        this.startSignInActivityMail();
    }
    // --------------------
    // Authentification
    // --------------------

    AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
            .Builder(R.layout.activity_connexion)
            .setGoogleButtonId(R.id.main_activity_google_login_button)
            .setFacebookButtonId(R.id.main_activity_facebook_login_button)
            .setTwitterButtonId(R.id.main_activity_twitter_login_button)
            .setEmailButtonId(R.id.email_button)
            .build();

    private void startSignInActivity(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityMail(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityFacebook(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityTwitter(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAuthMethodPickerLayout(customLayout)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.TwitterBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
