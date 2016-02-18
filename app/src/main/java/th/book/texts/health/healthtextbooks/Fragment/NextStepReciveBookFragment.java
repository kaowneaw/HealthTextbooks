package th.book.texts.health.healthtextbooks.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.ResultEntity;


public class NextStepReciveBookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String reciveName;
    private EditText reciveStep;
    private static List<Matirial> listMat;
    private List<Matirial> listMatConv;
    ResultEntity results;

    public NextStepReciveBookFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static NextStepReciveBookFragment newInstance(String param1, String reciveName, List<Matirial> listMat) {
        NextStepReciveBookFragment fragment = new NextStepReciveBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, reciveName);
        NextStepReciveBookFragment.listMat = listMat;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.complete_recivebook, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.ok_recivebook) {
            createReciveBook();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            reciveName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_next_step_recive_book, container, false);
        reciveStep = (EditText) v.findViewById(R.id.reciveStep);
        this.listMatConv = convertArray(listMat);
        return v;
    }

    private void createReciveBook() {

        Log.v("createReciveBook", reciveName);
        final String reciveDesc = reciveStep.getText().toString();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {

                String url = "http://www.jaa-ikuzo.com/htb/addMyRecive.php";
                OkHttpClient client = new OkHttpClient();

                FormEncodingBuilder formBodyBuilder = new FormEncodingBuilder();
                int index = 0;
                for (Matirial m : listMatConv) {
                    formBodyBuilder.add("matAmount[" + index + "]", m.getAmount() + "");
                    formBodyBuilder.add("matId[" + index + "]", m.getMatId() + "");
                    index++;
                }
                Log.v("reciveName", reciveName);
                formBodyBuilder.add("recipeName", reciveName);
                formBodyBuilder.add("recipeDesc", reciveDesc);

                RequestBody formBody = formBodyBuilder.build();

                Request request = new Request.Builder()
                        .post(formBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("=>", reponse);
                    results = gson.fromJson(reponse, ResultEntity.class);

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

                    Toast.makeText(getActivity(), "สร้างสูตรสำเร็จ", Toast.LENGTH_SHORT).show();
                    closeFragment();
                } else {
                    Toast.makeText(getActivity(), "สร้างสูตรไม่สำเร็จ", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();

    }

    private void closeFragment() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, MyReciveFragment.newInstance("", "")).commit();

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
