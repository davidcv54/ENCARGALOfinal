package mx.com.encargalo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mx.com.encargalo.utils.Activitys;
import mx.com.encargalo.utils.Constants;
import mx.com.encargalo.utils.Session;
import mx.com.encargalo.utils.Utils;

public class activity_is_actiniciarsesionprincipal extends AppCompatActivity {
    Button btniniciarfb;
    Button btniniciargmail;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 9001;
    ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    String token;
    CallbackManager mCallbackManager;
    String id;
    String device_token, device_UDID;
    String TAG = "FragmentLogin";
    String keyHash = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_actiniciarsesionprincipal);
        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        //Init Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.default_web_client_id)).build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),                  //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        btniniciarfb=(Button)findViewById(R.id.is_ispbtnfacebook);
        btniniciarfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookLogin();
            }
        });
        btniniciargmail=(Button)findViewById(R.id.btn_google_singin);
        btniniciargmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressDialog();
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Cargando");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            hideProgressDialog();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with FireBase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            // Pass the activity result back to the Facebook SDK
             mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                        FirebaseUser user = mAuth.getCurrentUser();

                        assert user != null;
                        String personName = user.getDisplayName();

                        if (personName.contains(" ")) {
                            personName = personName.substring(0, personName.indexOf(" "));
                        }
                        String email = user.getEmail();
                        String[] userName = user.getEmail().split("@");

                        if (isNew) {
                            hideProgressDialog();
                            //ShowReferDialog(user.getUid(), userName[0] + id, personName, email, user.getPhotoUrl().toString(), "gmail");
                        } else
                            UserSignUpWithSocialMedia(user.getUid(), "", userName[0] + id, user.getDisplayName(), user.getEmail(), user.getPhotoUrl().toString(), "gmail");
                    } else {
                        hideProgressDialog();

                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException | FirebaseAuthUserCollisionException invalidEmail) {

                            //setSnackBar(invalidEmail.getMessage(), getString(R.string.ok));
                        } catch (Exception e) {
                            e.printStackTrace();
                            //setSnackBar(e.getMessage(), getString(R.string.ok));
                        }
                    }

                });
    }

    public void UserSignUpWithSocialMedia(final String authId, final String fCode, final String referCode, final String name, final String email, final String profile, final String type) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.email, email);
        params.put(Constants.name, name);
        params.put(Constants.PROFILE, profile);
        Activitys.getSingleton(activity_is_actiniciarsesionprincipal.this   , activity_is_actcrearunacuenta.class).muestraActividad(params);

    }

    private void handleFacebookAccessToken(AccessToken token) {
        showProgressDialog();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    try {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            String personName = user.getDisplayName();
                            if (personName.contains(" ")) {
                                personName = personName.substring(0, personName.indexOf(" "));
                            }
                            String referCode = "";

                            if (user.getEmail() != null) {
                                String[] userName = user.getEmail().split("@");
                                referCode = userName[0];
                            } else {
                                referCode = user.getPhoneNumber();
                            }
                            if (isNew) {
                                hideProgressDialog();
                                //ShowReferDialog(user.getUid(), referCode + id, personName, "" + user.getEmail(), user.getPhotoUrl().toString(), "fb");
                            } else
                                UserSignUpWithSocialMedia(user.getUid(), "", referCode + id, personName, "" + user.getEmail(), user.getPhotoUrl().toString(), "fb");
                        } else {
                            // If sign in fails, display a message to the user.

                            LoginManager.getInstance().logOut();
                            hideProgressDialog();
                            try {
                                throw Objects.requireNonNull(task.getException());
                            } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException | FirebaseAuthUserCollisionException invalidEmail) {
                               // setSnackBar(invalidEmail.getMessage(), getString(R.string.ok));
                            } catch (Exception e) {
                                e.printStackTrace();

                                //setSnackBar(e.getMessage(), getString(R.string.ok));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

    public void facebookLogin() {
        if (Utils.isNetworkAvailable(activity_is_actiniciarsesionprincipal.this)) {
            LoginManager.getInstance().logInWithReadPermissions(activity_is_actiniciarsesionprincipal.this,
                    Arrays.asList("public_profile", "email"));


            LoginManager.getInstance().registerCallback(mCallbackManager,
                    new FacebookCallback<LoginResult>() {

                        public void onSuccess(final LoginResult loginResult) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {

                                            @Override
                                            public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                                                try {
                                                    Log.e(TAG, "id" + user.optString("id"));
                                                    Log.e(TAG, "name" + user.optString("first_name"));

                                                    String profileUrl = "https://graph.facebook.com/v2.8/" + user.optString("id") + "/picture?width=1920";

                                                    HashMap<String, String> params = new HashMap<>();
                                                    params.put(Constants.email, user.optString("email"));
                                                    params.put(Constants.name, user.optString("first_name"));
                                                    params.put(Constants.PROFILE, profileUrl);
                                                    Activitys.getSingleton(activity_is_actiniciarsesionprincipal.this   , activity_is_actcrearunacuenta.class).muestraActividad(params);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.d("facebookExp", e.getMessage());
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,first_name,last_name,email");
                                request.setParameters(parameters);
                                request.executeAsync();
                                Log.e("getAccessToken", "" + loginResult.getAccessToken().getToken());
                                //SharedHelper.putKey(Login.this, "accessToken", loginResult.getAccessToken().getToken());
//                                        login(loginResult.getAccessToken().getToken(), URLHelper.FACEBOOK_LOGIN, "facebook");
                            }

                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e("exceptionfacebook", exception.toString());
                            // App code
                        }
                    });
        } else {
            //mProgressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity_is_actiniciarsesionprincipal.this);
            builder.setMessage("Check your Internet").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent NetworkAction = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(NetworkAction);

                }
            });
            builder.show();
        }
    }

    public void setSnackBarStatus() {
        final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), getString(R.string.account_deactivate), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.ok), view -> {

            Session.clearUserSession(this);
            mAuth.signOut();
            LoginManager.getInstance().logOut();

        });

        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
        hideProgressDialog();
    }

    public void setSnackBar(String message, String action) {
        final Snackbar snackbar = Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, view -> {
            if (Utils.isNetworkAvailable(this)) {
                snackbar.dismiss();
            } else {
                snackbar.show();
            }
        });
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }



}