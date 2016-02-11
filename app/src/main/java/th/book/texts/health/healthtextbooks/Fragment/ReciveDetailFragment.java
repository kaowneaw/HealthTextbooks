package th.book.texts.health.healthtextbooks.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import th.book.texts.health.healthtextbooks.Adapter.OrderReciveAdapter;
import th.book.texts.health.healthtextbooks.Adapter.OrderSumAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.Order;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReciveDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReciveDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Order myOrder;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lv_order;
    TextView order_list_total,order_price_total;
    List<Matirial> listMat;


    public ReciveDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReciveDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReciveDetailFragment newInstance(String param1, String param2, Order myOrder) {
        ReciveDetailFragment fragment = new ReciveDetailFragment();
        Bundle args = new Bundle();
        ReciveDetailFragment.myOrder = myOrder;
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

    private void callService() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/getReciveMat.php?orderId=" + ReciveDetailFragment.myOrder.getOrderId();
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
                OrderSumAdapter adapter = new OrderSumAdapter(listMat, getActivity());
                lv_order.setAdapter(adapter);
                order_list_total.setText("รวมสินค้าทั้งหมด  " + listMat.size() + "  รายการ");
                order_price_total.setText("รวมเป็นเงิน  " + getPrice(listMat) + "  บาท");

            }
        }.execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_recive_detail, container, false);
        lv_order = (ListView) v.findViewById(R.id.lv_order);
        order_list_total = (TextView) v.findViewById(R.id.order_list_total);
        order_price_total = (TextView) v.findViewById(R.id.order_price_total);
        callService();

        return v;
    }

    private double getPrice(List<Matirial> arrConv) {
        double total = 0;

        for (Matirial mat : arrConv) {

            total += mat.getPrice() * mat.getAmount();
        }

        return total;
    }

}
