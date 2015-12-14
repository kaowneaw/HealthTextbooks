package th.book.texts.health.healthtextbooks;


import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideToolbar();
        initWidget();

    }


    private void initWidget() {

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        Button btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btLogin) {

            String username = this.edtUsername.getText().toString();
            String password = this.edtPassword.getText().toString();
            if (!username.equals("") && !password.equals("")) {
                //login api
                checkUserServer(username,password);
            } else {
                Toast.makeText(getApplicationContext(), "Plese fill in the blank", Toast.LENGTH_SHORT).show();
                if (username.equals("")) {
                    edtUsername.requestFocus();
                } else if (password.equals("")) {
                    edtPassword.requestFocus();
                }
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void hideToolbar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void checkUserServer(final String username, final String password){

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody formBody = new FormEncodingBuilder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url("http://"+HeathTextBook.IP_CONFIG+"/heathTextBook/login.php").post(formBody).build();

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
                Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsObj = new JSONObject(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
