package pl.pjatk.pamo.kotlincalculatorbmi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment


class FragmentCalcPPM : Fragment() {
    var radioGroup: RadioGroup? = null
    var radioButton: RadioButton? = null
    var ageView: TextView? = null
    var textView: TextView? = null
    var view: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_adult_calc_result_ppm, container, false)
        val button = view.findViewById<View>(R.id.commitAge) as Button
        button.setOnClickListener {
            val activity = activity as AdultCalcResultActivity?
            val height: Float = activity.getHeight()
            val weight: Float = activity.getWeight()
            ageView = view.findViewById<View>(R.id.ageTextView) as TextView
            val age = ageView!!.text.toString().toInt()
            radioGroup = view.findViewById<View>(R.id.radioGroup) as RadioGroup
            val id = radioGroup!!.checkedRadioButtonId
            radioButton = view.findViewById(id)
            val gender = radioButton.getText().toString()
            textView = view.findViewById<View>(R.id.calories) as TextView
            val ppm = calcPPM(height, weight, age, gender)
            textView!!.text = ppm.toString()
        }
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun calcPPM(
        height: Float,
        weight: Float,
        age: Int,
        gender: String
    ): Long {
        var ppm = 0.0
        if (gender == "mężczyzna") {
            ppm = 66.47 + 13.7 * weight + 5 * height - 6.76 * age
        } else if (gender == "kobieta") {
            ppm = 655.1 + 9.567 * weight + 1.85 * height - 4.68 * age
        }
        return Math.round(ppm)
    }
}


//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//public class FragmentCalcPPM extends Fragment {
//
//    RadioGroup radioGroup;
//    RadioButton radioButton;
//    TextView ageView;
//    TextView textView;
//    View view;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_adult_calc_result_ppm, container, false);
//
//        final Button button = (Button) view.findViewById(R.id.commitAge);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                pl.pjatk.pamo.calculatorbmi.AdultCalcResultActivity activity = (pl.pjatk.pamo.calculatorbmi.AdultCalcResultActivity) getActivity();
//                float height = activity.getHeight();
//                float weight = activity.getWeight();
//
//                ageView = (TextView) view.findViewById(R.id.ageTextView);
//                int age =  Integer.parseInt(ageView.getText().toString());
//
//                radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
//                int id = radioGroup.getCheckedRadioButtonId();
//                radioButton = view.findViewById(id);
//                String gender = radioButton.getText().toString();
//
//                textView = (TextView) view.findViewById(R.id.calories);
//                long ppm = calcPPM(height,weight,age,gender);
//                textView.setText(String.valueOf(ppm));
//
//            }
//        });
//
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }
//
//    private long calcPPM(float height, float weight, int age, String gender) {
//        double ppm = 0;
//        if(gender.equals("mężczyzna")) {
//            ppm = 66.47 + (13.7 * weight) + (5 * height) - (6.76 * age);
//        } else if(gender.equals("kobieta")) {
//            ppm = 655.1 + (9.567 * weight) + (1.85 * height) - (4.68 * age);
//        }
//        long ret = Math.round(ppm);
//        return ret;
//    }
//}
