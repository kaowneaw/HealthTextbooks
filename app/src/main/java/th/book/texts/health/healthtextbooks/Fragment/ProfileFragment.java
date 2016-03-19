package th.book.texts.health.healthtextbooks.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView img_profile;
    TextView name_profile, email_profile, calrories;
    EditText height, weight, age;
    Spinner sex;
    Button cal;

    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.profilemenu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Profile");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveprofile) {

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        img_profile = (ImageView) v.findViewById(R.id.img_profile);
        name_profile = (TextView) v.findViewById(R.id.name_profile);
        email_profile = (TextView) v.findViewById(R.id.email_profile);
        height = (EditText) v.findViewById(R.id.height);
        weight = (EditText) v.findViewById(R.id.weight);
        sex = (Spinner) v.findViewById(R.id.sex);
        cal = (Button) v.findViewById(R.id.cal);
        age = (EditText) v.findViewById(R.id.age);
        calrories = (TextView) v.findViewById(R.id.calrories);
        UserPreference pref = new UserPreference(getContext());
        name_profile.setText("ชื่อผู้ใช้ " + pref.getName());
        email_profile.setText("e-mail " + pref.getEmail());
        cal.setOnClickListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("ชาย");
        categories.add("หญิง");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        sex.setAdapter(dataAdapter);
        return v;
    }

    @Override
    public void onClick(View v) {

        double BMR = 0;
        String valSex = sex.getSelectedItem().toString();
        double height = Double.parseDouble(this.height.getText().toString());
        double weight = Double.parseDouble(this.weight.getText().toString());
        double age = Double.parseDouble(this.age.getText().toString());
        if (valSex.equals("ชาย")) {

            BMR = 10 * weight + 6.25 * height - 5 * age + 5;

        } else {
            BMR = 10 * weight + 6.25 * height - 5 * age - 161;
        }
        calrories.setText("แคลลอรี่แนะนำต่อวัน " + BMR);

    }
}
