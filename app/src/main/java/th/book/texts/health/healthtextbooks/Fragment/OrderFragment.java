package th.book.texts.health.healthtextbooks.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.OrderAdapter;
import th.book.texts.health.healthtextbooks.Adapter.RefrigeratorAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv_order;
    private List<Matirial> listMat;
    OrderAdapter adapter;

    public OrderFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        lv_order = (ListView) v.findViewById(R.id.lv_order);


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next) {

            for (int i = 0; i < adapter.getCount(); i++) {
                View view = lv_order.getChildAt(i);
                EditText editText = (EditText) view.findViewById(R.id.matAmount);
                String string = editText.getText().toString();
                if (!string.equals("")) listMat.get(i).setAmount(Double.parseDouble(string));
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, SumOrderFragment.newInstance("", "", listMat));
            fragmentTransaction.addToBackStack(null).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("สั่งซื้อสินค้า");
        callService();
    }

    private void callService() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/common/getAllMat.php";
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
                    listMat = results.getResultsMatirial();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new OrderAdapter(listMat, getActivity());
                lv_order.setAdapter(adapter);
            }
        }.execute();

    }


}
