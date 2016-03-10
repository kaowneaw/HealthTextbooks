package th.book.texts.health.healthtextbooks.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.RefrigeratorAdapter;
import th.book.texts.health.healthtextbooks.Adapter.RefrigeratorDetailAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Refrigerator;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RefrigeratorDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RefrigeratorDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static Refrigerator refriObj;
    private List<Refrigerator> listRefri;
    ListView refrag_detail_lv;

    public RefrigeratorDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RefrigeratorDetailFragment newInstance(String param1, String param2, Refrigerator refriObj) {
        RefrigeratorDetailFragment fragment = new RefrigeratorDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        RefrigeratorDetailFragment.refriObj = refriObj;
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
    public void onResume() {
        super.onResume();
        callService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_refrigerator_detail, container, false);
        refrag_detail_lv = (ListView) v.findViewById(R.id.refrag_detail_lv);

        return v;
    }

    private void callService() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/getRefragDetail.php?personId=1&matId=" + RefrigeratorDetailFragment.refriObj.getMatId();
                OkHttpClient client = new OkHttpClient();

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
                RefrigeratorDetailAdapter adapter = new RefrigeratorDetailAdapter(listRefri, getActivity());
                refrag_detail_lv.setAdapter(adapter);
            }
        }.execute();
    }

}
