package fr.nelfdesign.go4lunch.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import fr.nelfdesign.go4lunch.R;
import fr.nelfdesign.go4lunch.base.BaseActivity;
import fr.nelfdesign.go4lunch.utils.Utils;

public class ConnexionActivity extends BaseActivity {

    @BindView(R.id.layout_main)
    ConstraintLayout mConstraintLayout;

    //FOR DATA
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onResume() {
        super.onResume();
        if (this.isCurrentUserLogged()){
            this.startMainActivity();
        }
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.activity_connexion;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return null;
    }

    @OnClick(R.id.main_activity_google_login_button)
    public void onClickLoginButton() {
        this.startSignInActivityGoogle();
    }

    @OnClick(R.id.main_activity_facebook_login_button)
    public void onClickFacebookLoginButton() {
        this.startSignInActivityFacebook();
    }

    @OnClick(R.id.main_activity_twitter_login_button)
    public void onClickTwitterLoginButton() {
        this.startSignInActivityTwitter();
    }

    @OnClick(R.id.main_activity_email_login_button)
    public void onClickMailLoginButton() {
        this.startSignInActivityMail();
    }
    // --------------------
    // Authentification
    // --------------------

    private void startSignInActivityGoogle(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // 3 - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                Utils.showSnackBar(this.mConstraintLayout, getString(R.string.connection_succeed));
                this.startMainActivity();
            } else { // ERRORS
                if (response == null) {
                    Utils.showSnackBar(this.mConstraintLayout, getString(R.string.error_authentication_canceled));
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Utils.showSnackBar(this.mConstraintLayout, getString(R.string.error_no_internet));
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Utils.showSnackBar(this.mConstraintLayout, getString(R.string.error_unknown_error));
                }
            }
        }
    }

}
