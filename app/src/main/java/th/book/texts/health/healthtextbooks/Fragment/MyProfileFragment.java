package th.book.texts.health.healthtextbooks.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import th.book.texts.health.healthtextbooks.Adapter.OrderAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;
import th.book.texts.health.healthtextbooks.model.ResultEntity;


public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText name_send_profile, address_profile, tel_profile;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ข้อมูลผู้ใช้งาน");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profilemenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveprofile) {
            saveService();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initial(v);
        getUserService();
        return v;
    }

    private void initial(View myView) {

        ImageView img_profile = (ImageView) myView.findViewById(R.id.img_profile);
        TextView name_profile = (TextView) myView.findViewById(R.id.name_profile);
        TextView email_profile = (TextView) myView.findViewById(R.id.email_profile);
        name_send_profile = (EditText) myView.findViewById(R.id.name_send_profile);
        address_profile = (EditText) myView.findViewById(R.id.address_profile);
        tel_profile = (EditText) myView.findViewById(R.id.tel_profile);
        UserPreference pref = new UserPreference(getContext());
        AQuery aq = new AQuery(getContext());
        aq.id(img_profile).image("https://graph.facebook.com/" + pref.getUserID() + "/picture?width=200&height=200");
        name_profile.setText(pref.getName());
        email_profile.setText(pref.getEmail());
    }


    private void getUserService() {

        new AsyncTask<Void, Void, Void>() {

            ResultEntity results;

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/getPerson.php";
                OkHttpClient client = new OkHttpClient();
                UserPreference pref = new UserPreference(getContext());
                RequestBody formBody = new FormEncodingBuilder()
                        .add("personId", pref.getUserID()).build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("=>", reponse);
                    results = gson.fromJson(reponse, ResultEntity.class);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (results.isStatus()) {
                    name_send_profile.setText(results.getResultsPerson().get(0).getPersonNameSend());
                    tel_profile.setText(results.getResultsPerson().get(0).getPersonTel());
                    address_profile.setText(results.getResultsPerson().get(0).getPersonAddress());
                } else {
                    Toast.makeText(getActivity(), "บันทึกไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    private void saveService() {
        final String name_send = name_send_profile.getText().toString();
        final String address_send = address_profile.getText().toString();
        final String tel = tel_profile.getText().toString();

        new AsyncTask<Void, Void, Void>() {
            ResultEntity results;

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/editPerson.php";
                OkHttpClient client = new OkHttpClient();
                UserPreference pref = new UserPreference(getContext());
                RequestBody formBody = new FormEncodingBuilder()
                        .add("personId", pref.getUserID())
                        .add("personAddress", address_send)
                        .add("personTel", tel)
                        .add("personNameSend", name_send).build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("=>", reponse);
                    results = gson.fromJson(reponse, ResultEntity.class);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (results.isStatus()) {
                    Toast.makeText(getActivity(), "บันทึกสำเร็จ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "บันทึกไม่สำเร็จ", Toast.LENGTH_LONG).show();
                }

            }
        }.execute();

    }


}
