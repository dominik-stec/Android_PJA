package pl.pjatk.pamo.calculatorbmi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AdultCalcResultActivity extends AppCompatActivity {

    public static final String BMI_RESULT = "bmi_result";

    private float bmi;

    private final String toLowBmi = "niedowagę";
    private final String normalBmi = "normę";
    private final String toHighBmi = "nadwagę";
    private final String veryHighBmi_I = "otyłość I st.";
    private final String veryHighBmi_II = "otyłość II st.";
    private final String veryHighBmi_III = "otyłość III st.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_calc_result);

        getBundleBmiVal();
        double bmiRound = roundBmi(this.bmi);
        setBmiTextView(bmiRound);
        calculateBmiCategory();

    }

    private void getBundleBmiVal() {
        Bundle b = getIntent().getExtras();
        bmi = b.getFloat(BMI_RESULT, -1);
    }

    private double roundBmi(float bmi) {
        int floorBmi = Math.round(bmi*10);
        double bmiRound = floorBmi/10.0;
        return bmiRound;
    }

    private void setBmiTextView(double bmi) {
        TextView bmiResult = (TextView) findViewById(R.id.bmiResult);
        bmiResult.setText(String.valueOf(bmi));
    }

    private void calculateBmiCategory() {
        TextView bmiCategory = (TextView) findViewById(R.id.bmiCategory);
        if(bmi<=18.5) bmiCategory.setText(toLowBmi);
        else if(bmi>18.5 && bmi<=24.9) bmiCategory.setText(normalBmi);
        else if(bmi>24.9 && bmi<=29.9) bmiCategory.setText(toHighBmi);
        else if(bmi>29.9 && bmi<=34.9) bmiCategory.setText(veryHighBmi_I);
        else if(bmi>34.9 && bmi<=39.9) bmiCategory.setText(veryHighBmi_II);
        else if(bmi>39.9) bmiCategory.setText(veryHighBmi_III);
    }
}
