package th.book.texts.health.healthtextbooks.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.MyReciverAdapter;
import th.book.texts.health.healthtextbooks.Adapter.ReciveBookDetailAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.ReciveDetail;
import th.book.texts.health.healthtextbooks.model.ResultEntity;


public class ReciveBookDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String howto;
    private String reciveId;
    private ListView lv_detailMat;
    private TextView tv_howto;
    List<ReciveDetail> listReciveDetail;

    public ReciveBookDetailFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ReciveBookDetailFragment newInstance(String howto, String reciveId) {
        ReciveBookDetailFragment fragment = new ReciveBookDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, howto);
        args.putString(ARG_PARAM2, reciveId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            howto = getArguments().getString(ARG_PARAM1);
            reciveId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("ขั้นตอนการทำ");
        callService(reciveId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recive_book_detail, container, false);
        lv_detailMat = (ListView) v.findViewById(R.id.lv_detailMat);
        tv_howto = (TextView) v.findViewById(R.id.tv_howto);
        tv_howto.setText(howto);
        return v;
    }

    private void callService(final String reciveId) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/getMyReciveDetail.php?reciveId=" + reciveId;
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
                    listReciveDetail = results.getResultsRecive();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ReciveBookDetailAdapter adapter = new ReciveBookDetailAdapter(listReciveDetail, getActivity());
                lv_detailMat.setAdapter(adapter);
            }

        }.execute();

    }

}
