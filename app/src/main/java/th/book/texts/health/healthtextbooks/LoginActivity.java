package th.book.texts.health.healthtextbooks;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import th.book.texts.health.healthtextbooks.Utill.UserPreference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername;
    private EditText edtPassword;
    private ImageButton btLoginFB;
    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackmanager;
    private LoginActivity THIS = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserPreference pref = new UserPreference(this);
        if (!pref.getUserID().equals("NULL")) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();

        } else {

            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_login);
            initWidget();

            getKeyHash();
        }

    }

    private void initWidget() {

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btLoginFB = (ImageButton) findViewById(R.id.btLoginFB);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);
        btLoginFB.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btLogin) {

            String username = this.edtUsername.getText().toString();
            String password = this.edtPassword.getText().toString();
            if (!username.equals("") && !password.equals("")) {
                //login api
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
//              checkUserServer(username,password);
            } else {

                Snackbar.make(v, "Plese fill in the blank", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if (username.equals("")) {
                    edtUsername.requestFocus();
                } else if (password.equals("")) {
                    edtPassword.requestFocus();
                }
            }
        } else if (v == btLoginFB) {
            onFblogin();
        }
    }


    private void checkUserServer(final String username, final String password) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://" + HeathTextBook.IP_CONFIG + "/heathTextBook/login.php").post(formBody).build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        return response.body().string();
                    } else {
                        return "Not Success - code : " + response.code();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error - " + e.getMessage();
                }
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsObj = new JSONObject(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    // Private method to handle Facebook login and callback
    private void onFblogin() {
        callbackmanager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Application code
                                        Log.v("LoginActivity", response.toString());
                                        try {
                                            String id = object.getString("id");
                                            String name = object.getString("name");
                                            String email = object.getString("email");
                                            UserPreference pref = new UserPreference(getApplicationContext(), id, name, email);//Facebook not have username and password
                                            if (pref.commit()) {
                                                Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                                                toMain.putExtra("name", name);
                                                toMain.putExtra("email", email);
                                                startActivity(toMain);
                                                THIS.finish();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("Cancel", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Error", error.toString());
                        Toast.makeText(getApplicationContext(), "ไม่สามารถเข้าสู่ระบบได้", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }


    private void getKeyHash() {

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.example.matinee.travel_south", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
}
