package th.book.texts.health.healthtextbooks.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.RefrigeratorAdapter;
import th.book.texts.health.healthtextbooks.Adapter.SpinnerMatirialAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.Refrigerator;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

public class RefrigeratorFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GridView refrag_lv;
    private List<Refrigerator> listRefri;

    public RefrigeratorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RefrigeratorFragment newInstance(String param1, String param2) {
        RefrigeratorFragment fragment = new RefrigeratorFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_refrigerator, container, false);
        refrag_lv = (GridView) v.findViewById(R.id.refrag_lv);
        refrag_lv.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ตู้เย็นของฉัน");
        callService();
    }

    private void callService() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                UserPreference pref = new UserPreference(getContext());
                String url = "http://www.jaa-ikuzo.com/htb/getRefrag.php?personId=" + pref.getUserID();
                OkHttpClient client = new OkHttpClient();
//                RequestBody formBody = new FormEncodingBuilder()
//                        .add("type", String.valueOf(typeId))
//                        .add("province_id", String.valueOf(provinceId)).build();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("=>", reponse);
                    ResultEntity results = gson.fromJson(reponse, ResultEntity.class);
                    listRefri = results.getResultsRefrigerator();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                RefrigeratorAdapter adapter = new RefrigeratorAdapter(listRefri, getActivity());
                refrag_lv.setAdapter(adapter);
            }
        }.execute();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, RefrigeratorDetailFragment.newInstance("", "", listRefri.get(position)));
        fragmentTransaction.addToBackStack(null).commit();
    }
}
