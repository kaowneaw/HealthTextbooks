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
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.OrderAdapter;
import th.book.texts.health.healthtextbooks.Adapter.OrderReciveAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Order;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

public class OrderReciveFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv_myorder;
    private List<Order> listOrder;

    public OrderReciveFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderReciveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderReciveFragment newInstance(String param1, String param2) {
        OrderReciveFragment fragment = new OrderReciveFragment();
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
        View v = inflater.inflate(R.layout.fragment_order_recive, container, false);
        lv_myorder = (ListView) v.findViewById(R.id.lv_myorder);
        lv_myorder.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("รายการสั่งซื้อของฉัน");
        callService();
    }

    private void callService() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/getMyRecive.php?personId=1";
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
                    listOrder = results.getResultsOrder();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                OrderReciveAdapter adapter = new OrderReciveAdapter(listOrder, getActivity());
                lv_myorder.setAdapter(adapter);

            }
        }.execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, ReciveDetailFragment.newInstance("", "",listOrder.get(position)));
        fragmentTransaction.addToBackStack(null).commit();
    }
}
