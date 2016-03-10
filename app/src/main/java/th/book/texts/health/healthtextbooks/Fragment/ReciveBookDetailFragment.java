package th.book.texts.health.healthtextbooks.Fragment;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.ReciveBookDetailAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
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
    List<Matirial> listMatirial;

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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            howto = getArguments().getString(ARG_PARAM1);
            reciveId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cooking) {

            callService2();//check stock with refragerator
        }

        return super.onOptionsItemSelected(item);
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

    private void callService3() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String url = "http://www.jaa-ikuzo.com/htb/Cooking.php";
                OkHttpClient client = new OkHttpClient();

                FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
                int index = 0;
                for (ReciveDetail m : listReciveDetail) {

                    formBodyBuilder.add("matAmount[" + index + "]", m.getAmount() + "");
                    formBodyBuilder.add("matId[" + index + "]", m.getMatId() + "");
                    index++;
                }

                formBodyBuilder.add("personId", "1");

                RequestBody formBody = formBodyBuilder.build();
                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("=>TTTTT=> ", reponse);
                    ResultEntity results = gson.fromJson(reponse, ResultEntity.class);
                    if (results.isStatus()) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }

        }.execute();

    }

    private void callService2() {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //get stock total in refrigerator
                String url = "http://www.jaa-ikuzo.com/htb/getRefragTotal.php?personId=1";
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("callService2=>", reponse);
                    ResultEntity results = gson.fromJson(reponse, ResultEntity.class);
                    listMatirial = results.getResultsMatirial();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                List<Matirial> matToOrder = new ArrayList<>();
                for (ReciveDetail obj : listReciveDetail) {
                    Matirial matNotEnough = checkStrockRefrigerator(obj);
                    if (matNotEnough != null) {
                        matToOrder.add(matNotEnough);
                    }
                }
                Log.v(">>>>>>", matToOrder.size() + "<<<<");
                for (Matirial obj : matToOrder) {
                    Log.v(">>>>>>", obj.getMatName() + "<<<<");
                }

                if (matToOrder.size() == 0) {
                    confirmDialog();
                } else {
                    //Order
                    for (Matirial matOrder : matToOrder) {
                        Toast.makeText(getActivity(), matOrder.getMatId() + "", Toast.LENGTH_SHORT).show();
                    }

                    confirmOrder();

                }
            }

        }.execute();
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

    private void confirmDialog() {

        new AlertDialog.Builder(getActivity())
                .setTitle("ใช้วัตถุดิบ")
                .setMessage("ทำการเบิกวัตถุดิบออกจากตู้เย็นของฉัน")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        callService3();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void confirmOrder() {

        new AlertDialog.Builder(getActivity())
                .setTitle("วัตถุดิบไม่เพียงพอ")
                .setMessage("ต้องการไปยังหน้าสั่งซื้อวัตถุดิบ")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, OrderFragment.newInstance("", ""));
                        fragmentTransaction.addToBackStack(null).commit();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private Matirial checkStrockRefrigerator(ReciveDetail use) {

        int isNotHaveInRefrag = 0;
        for (Matirial obj : listMatirial) { //obj is mat in Refrigerator
            if (use.getMatId() == obj.getMatId()) {
                if (use.getAmount() > obj.getAmountTotal()) {
                    return obj;
                }
            }
            if (use.getMatId() != obj.getMatId()) {
                isNotHaveInRefrag++;
            }
        }
        if (isNotHaveInRefrag == listMatirial.size()) {
            //if mat is not have in my refragerator
            Matirial mat = new Matirial();
            mat.setMatId(use.getMatId());
            mat.setAmount(use.getAmount());
            return mat;
        }
        return null;
    }


}
