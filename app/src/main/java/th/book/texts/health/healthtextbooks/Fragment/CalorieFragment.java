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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import th.book.texts.health.healthtextbooks.R;
import th.book.texts.health.healthtextbooks.Utill.UserPreference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalorieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalorieFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView img_profile;
    private TextView calrories, recomendMenu;
    EditText height, weight, age;
    Button cal;
    RadioGroup sex;
    View myView;

    public CalorieFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CalorieFragment newInstance(String param1, String param2) {
        CalorieFragment fragment = new CalorieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("คำนวณแคลลอรี่");
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
        myView = inflater.inflate(R.layout.fragment_calrorie, container, false);
        recomendMenu = (TextView) myView.findViewById(R.id.recomendMenu);
        sex = (RadioGroup) myView.findViewById(R.id.sex);
        recomendMenu.setVisibility(View.INVISIBLE);
        height = (EditText) myView.findViewById(R.id.height);
        weight = (EditText) myView.findViewById(R.id.weight);
        cal = (Button) myView.findViewById(R.id.cal);
        age = (EditText) myView.findViewById(R.id.age);
        calrories = (TextView) myView.findViewById(R.id.calrories);
        cal.setOnClickListener(this);
        recomendMenu.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View v) {
        if (v == cal) {

            if (!this.height.getText().toString().isEmpty() && !this.weight.getText().toString().isEmpty() && !this.age.getText().toString().isEmpty()) {
                double BMR = 0;
                // get selected radio button from radioGroup
                int selectedId = sex.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) myView.findViewById(selectedId);
                String valSex = radioButton.getText().toString();
                double height = Double.parseDouble(this.height.getText().toString());
                double weight = Double.parseDouble(this.weight.getText().toString());
                double age = Double.parseDouble(this.age.getText().toString());
                if (valSex.equals("ชาย")) {

                    BMR = 10 * weight + 6.25 * height - 5 * age + 5;

                } else {
                    BMR = 10 * weight + 6.25 * height - 5 * age - 161;
                }
                calrories.setText("แคลลอรี่แนะนำต่อวัน " + BMR);
                recomendMenu.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "Plese fill in the blank", Toast.LENGTH_LONG).show();
            }

        } else {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, HomeFragment.newInstance("", ""));
            fragmentTransaction.addToBackStack(null).commit();
        }


    }
}
