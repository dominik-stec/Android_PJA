package pl.pjatk.pamo.kotlincalculatorbmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentCalcPPM extends Fragment {

    RadioGroup radioGroup;
    RadioButton radioButton;
    TextView ageView;
    TextView textView;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_adult_calc_result_ppm, container, false);

        final Button button = (Button) view.findViewById(R.id.commitAge);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                pl.pjatk.pamo.calculatorbmi.AdultCalcResultActivity activity = (pl.pjatk.pamo.calculatorbmi.AdultCalcResultActivity) getActivity();
                float height = activity.getHeight();
                float weight = activity.getWeight();

                ageView = (TextView) view.findViewById(R.id.ageTextView);
                int age =  Integer.parseInt(ageView.getText().toString());

                radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(id);
                String gender = radioButton.getText().toString();

                textView = (TextView) view.findViewById(R.id.calories);
                long ppm = calcPPM(height,weight,age,gender);
                textView.setText(String.valueOf(ppm));

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private long calcPPM(float height, float weight, int age, String gender) {
        double ppm = 0;
        if(gender.equals("mężczyzna")) {
            ppm = 66.47 + (13.7 * weight) + (5 * height) - (6.76 * age);
        } else if(gender.equals("kobieta")) {
            ppm = 655.1 + (9.567 * weight) + (1.85 * height) - (4.68 * age);
        }
        long ret = Math.round(ppm);
        return ret;
    }
}
