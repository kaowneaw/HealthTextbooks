package th.book.texts.health.healthtextbooks.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.OrderSumAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;
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
    private ListView lv_order;
    private TextView order_list_total, order_price_total;
    private List<Matirial> listMat;
    private ResultEntity results;

    public ReciveDetailFragment() {
        // Required empty public constructor
    }

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(ReciveDetailFragment.myOrder.getOrderStatus() != 0)
        inflater.inflate(R.menu.recive_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.recive) {
            confirmDialog();
        }

        return true;
    }

    private void confirmDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("รับสินค้า")
                .setMessage("ยืนยันการรับสินค้าไปยังตู้เย็นของฉัน")
                .setIcon(android.R.drawable.ic_input_add)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        reciveMatirial();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
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


    private void reciveMatirial() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String url = "http://www.jaa-ikuzo.com/htb/reciveMat.php";
                UserPreference pref = new UserPreference(getContext());
                FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
                int index = 0;
                for (Matirial m : listMat) {
                    formBodyBuilder.add("matAmount[" + index + "]", m.getAmount() + "");
                    formBodyBuilder.add("matId[" + index + "]", m.getMatId() + "");

                    index++;
                }
                formBodyBuilder.add("personId", pref.getUserID());
                formBodyBuilder.add("orderId", ReciveDetailFragment.myOrder.getOrderId() + "");

                RequestBody formBody = formBodyBuilder.build();

                OkHttpClient client = new OkHttpClient();

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
                    closeFragment();
                } else {

                    Toast.makeText(getActivity(), "ไม่สามารถบันทึกได้", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void closeFragment() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();

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
