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
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.book.texts.health.healthtextbooks.Adapter.OrderAdapter;
import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.ResultEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReciveBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReciveBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv_mat_recivebook;
    private List<Matirial> listMat;
    OrderAdapter adapter;
    EditText reciveName;

    public AddReciveBookFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddReciveBookFragment newInstance(String param1, String param2) {
        AddReciveBookFragment fragment = new AddReciveBookFragment();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_recivebook, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.next_recivebook) {

            for (int i = 0; i < lv_mat_recivebook.getLastVisiblePosition() - lv_mat_recivebook.getFirstVisiblePosition(); i++) {

                if (lv_mat_recivebook.getChildAt(i) != null) {

                    View view = lv_mat_recivebook.getChildAt(i);
                    EditText editText = (EditText) view.findViewById(R.id.matAmount);
                    String string = editText.getText().toString();
                    if (!string.equals("")) listMat.get(i).setAmount(Double.parseDouble(string));
                }
            }

            List<Matirial> listMatConv = convertArray(listMat);

            if (listMatConv.size() > 0) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, NextStepReciveBookFragment.newInstance("", reciveName.getText().toString(), listMat));
                fragmentTransaction.addToBackStack(null).commit();

            } else {

                Toast.makeText(getActivity(), "กรุณาเลือกวัตถุดิบอย่างน้อยหนึ่งชนิด", Toast.LENGTH_SHORT).show();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("เพิ่มสูตรอาหาร");
        callService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_recive_book, container, false);
        lv_mat_recivebook = (ListView) v.findViewById(R.id.lv_mat_recivebook);
        reciveName = (EditText) v.findViewById(R.id.reciveName);
        return v;
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
                lv_mat_recivebook.setAdapter(adapter);
            }

        }.execute();

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


}
