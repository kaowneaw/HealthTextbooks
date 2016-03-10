package th.book.texts.health.healthtextbooks.Fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.OrderSumAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SumOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SumOrderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ListView lv_order;
    private static List<Matirial> listMat;
    private List<Matirial> listMatConv;
    ResultEntity results;

    public SumOrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SumOrderFragment newInstance(String param1, String param2, List<Matirial> listMat) {
        SumOrderFragment fragment = new SumOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        SumOrderFragment.listMat = listMat;
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
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.order_sum, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order) {
            createOrder();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sum_order, container, false);
        TextView order_list_total = (TextView) v.findViewById(R.id.order_list_total);
        TextView order_price_total = (TextView) v.findViewById(R.id.order_price_total);
        lv_order = (ListView) v.findViewById(R.id.lv_order);
        this.listMatConv = convertArray(listMat);
        OrderSumAdapter adapter = new OrderSumAdapter(this.listMatConv, getActivity());
        lv_order.setAdapter(adapter);
        order_list_total.setText("รวมสินค้าทั้งหมด  " + this.listMatConv.size() + "  รายการ");
        order_price_total.setText("รวมเป็นเงิน  " + getPrice(this.listMatConv) + "  บาท");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("สรุปรายการสั่งซื้อ");
    }

    private void createOrder() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                String url = "http://www.jaa-ikuzo.com/htb/addOrder.php";
                OkHttpClient client = new OkHttpClient();

                FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
                int index = 0;
                for (Matirial m : listMatConv) {
                    formBodyBuilder.add("matAmount[" + index + "]", m.getAmount() + "");
                    formBodyBuilder.add("matId[" + index + "]", m.getMatId() + "");
                    formBodyBuilder.add("matPrice[" + index + "]", m.getPrice() + "");
                    index++;
                }
                formBodyBuilder.add("totalPrice", getPrice(listMatConv) + "");

                RequestBody formBody = formBodyBuilder.build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    results = gson.fromJson(reponse, ResultEntity.class);
                    Log.v("=>", reponse);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (results.isStatus()) {

                    Toast.makeText(getActivity(), "สั่งซื้อสำเร็จ", Toast.LENGTH_SHORT).show();
                    closeFragment();
                } else {
                    Toast.makeText(getActivity(), "สั่งซื้อไม่สำเร็จ", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();

    }

    private void closeFragment() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();

    }

    private List<Matirial> convertArray(List<Matirial> arrMat) {
        List<Matirial> list = new ArrayList<>();

        for (Matirial mat : arrMat) {

            Log.v("Amount ", mat.getAmount() + "");
            if (mat.getAmount() > 0.0) {
                list.add(mat);
            }
        }

        return list;
    }

    private double getPrice(List<Matirial> arrConv) {
        double total = 0;

        for (Matirial mat : arrConv) {

            total += mat.getPrice() * mat.getAmount();
        }

        return total;
    }


}
