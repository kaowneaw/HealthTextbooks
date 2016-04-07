package th.book.texts.health.healthtextbooks.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;
import th.book.texts.health.healthtextbooks.model.Matirial;
import th.book.texts.health.healthtextbooks.model.ResultEntity;


public class NextStepReciveBookFragment extends Fragment implements View.OnClickListener {
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
    Button chooseImage;
    ImageView imgRecipe;
    int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    String realPath;

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
//            createReciveBook();
            File myfile = new File(realPath);
            callService(myfile);
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
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_next_step_recive_book, container, false);
        reciveStep = (EditText) v.findViewById(R.id.reciveStep);
        chooseImage = (Button) v.findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(this);
        imgRecipe = (ImageView) v.findViewById(R.id.imgRecipe);
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
                UserPreference pref = new UserPreference(getContext());
                formBodyBuilder.add("personId", pref.getUserID());
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

    @Override
    public void onClick(View v) {

        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }


    private void callService(final File src) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        final UserPreference pref = new UserPreference(getActivity());
        final String reciveDesc = reciveStep.getText().toString();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                String url = "http://www.jaa-ikuzo.com/htb/addMyRecive.php";
                OkHttpClient client = new OkHttpClient();
                client.setConnectTimeout(60, TimeUnit.SECONDS); // connect timeout
                client.setReadTimeout(60, TimeUnit.SECONDS);    // socket tim

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                String memberId = pref.getUserID();

                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.type(MultipartBuilder.FORM);

                int index = 0;
                for (Matirial m : listMatConv) {
                    multipartBuilder.addFormDataPart("matAmount[" + index + "]", m.getAmount() + "");
                    multipartBuilder.addFormDataPart("matId[" + index + "]", m.getMatId() + "");
                    index++;
                }
                UserPreference pref = new UserPreference(getContext());
                multipartBuilder.addFormDataPart("personId", pref.getUserID());
                multipartBuilder.addFormDataPart("recipeName", reciveName);
                multipartBuilder.addFormDataPart("recipeDesc", reciveDesc);
                multipartBuilder.addFormDataPart("myfile", src.getName(), RequestBody.create(MEDIA_TYPE_PNG, reduceSizeFile(src)));

                RequestBody requestBody = multipartBuilder.build();

                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(url)
                        .build();

                try {

                    Gson gson = new Gson();
                    Response response = client.newCall(request).execute();
                    String reponse = response.body().string();
                    Log.v("result=>", reponse);
                    ResultEntity results = gson.fromJson(reponse, ResultEntity.class);
                    if (results.isStatus()) {

                        getActivity().getFragmentManager().popBackStack();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, MyReciveFragment.newInstance("", ""));
                        fragmentTransaction.addToBackStack(null).commit();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
        }.execute();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == getActivity().RESULT_OK && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                realPath = getPath(selectedImage);
                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                // Move to first row
                assert cursor != null;
                cursor.moveToFirst();
                Log.v("filePathColumn", filePathColumn[0]);
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                imgRecipe.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public File reduceSizeFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 4;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

            return file;

        } catch (Exception e) {
            return null;
        }
    }
}
