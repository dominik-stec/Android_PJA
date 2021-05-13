package pl.pjatk.pamo.kotlincalculatorbmi

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class AdultCalcResultActivity : AppCompatActivity() {
    var bmi = 0f
        private set
    var height = 0f
    var weight = 0f
    var age = 0
    private val toLowBmi = "niedowagę"
    private val normalBmi = "normę"
    private val toHighBmi = "nadwagę"
    private val veryHighBmi_I = "otyłość I st."
    private val veryHighBmi_II = "otyłość II st."
    private val veryHighBmi_III = "otyłość III st."
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bundleBmiVal
        setContentView(R.layout.activity_adult_calc_result)
        val bmiRound = roundBmi(bmi)
        setBmiTextView(bmiRound)
        calculateBmiCategory()
    }

    private val bundleBmiVal: Unit
        private get() {
            val b = intent.extras
            bmi = b!!.getFloat(BMI_RESULT, -1f)
            height = b.getFloat("height", -1f)
            weight = b.getFloat("weight", -1f)
        }

    private fun roundBmi(bmi: Float): Double {
        val floorBmi = Math.round(bmi * 10)
        return floorBmi / 10.0
    }

    private fun setBmiTextView(bmi: Double) {
        val bmiResult = findViewById<View>(R.id.bmiResult) as TextView
        bmiResult.text = bmi.toString()
    }

    private fun calculateBmiCategory() {
        val bmiCategory = findViewById<View>(R.id.bmiCategory) as TextView
        if (bmi <= 18.5) bmiCategory.text =
            toLowBmi else if (bmi > 18.5 && bmi <= 24.9) bmiCategory.text =
            normalBmi else if (bmi > 24.9 && bmi <= 29.9) bmiCategory.text =
            toHighBmi else if (bmi > 29.9 && bmi <= 34.9) bmiCategory.text =
            veryHighBmi_I else if (bmi > 34.9 && bmi <= 39.9) bmiCategory.text =
            veryHighBmi_II else if (bmi > 39.9) bmiCategory.text =
            veryHighBmi_III
    }

    companion object {
        const val BMI_RESULT = "bmi_result"
    }
}


//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//public class AdultCalcResultActivity extends AppCompatActivity {
//
//    public static final String BMI_RESULT = "bmi_result";
//
//    private float bmi;
//    public float height = 0;
//    public float weight = 0;
//    public int age = 0;
//
//    private final String toLowBmi = "niedowagę";
//    private final String normalBmi = "normę";
//    private final String toHighBmi = "nadwagę";
//    private final String veryHighBmi_I = "otyłość I st.";
//    private final String veryHighBmi_II = "otyłość II st.";
//    private final String veryHighBmi_III = "otyłość III st.";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getBundleBmiVal();
//
//        setContentView(R.layout.activity_adult_calc_result);
//
//        double bmiRound = roundBmi(this.bmi);
//        setBmiTextView(bmiRound);
//        calculateBmiCategory();
//
//    }
//
//    private void getBundleBmiVal() {
//        Bundle b = getIntent().getExtras();
//        bmi = b.getFloat(BMI_RESULT, -1);
//        height = b.getFloat("height", -1);
//        weight = b.getFloat("weight", -1);
//    }
//
//    private double roundBmi(float bmi) {
//        int floorBmi = Math.round(bmi*10);
//        double bmiRound = floorBmi/10.0;
//        return bmiRound;
//    }
//
//    private void setBmiTextView(double bmi) {
//        TextView bmiResult = (TextView) findViewById(R.id.bmiResult);
//        bmiResult.setText(String.valueOf(bmi));
//    }
//
//    private void calculateBmiCategory() {
//        TextView bmiCategory = (TextView) findViewById(R.id.bmiCategory);
//        if(bmi<=18.5) bmiCategory.setText(toLowBmi);
//        else if(bmi>18.5 && bmi<=24.9) bmiCategory.setText(normalBmi);
//        else if(bmi>24.9 && bmi<=29.9) bmiCategory.setText(toHighBmi);
//        else if(bmi>29.9 && bmi<=34.9) bmiCategory.setText(veryHighBmi_I);
//        else if(bmi>34.9 && bmi<=39.9) bmiCategory.setText(veryHighBmi_II);
//        else if(bmi>39.9) bmiCategory.setText(veryHighBmi_III);
//    }
//
//    public float getBmi() {
//        return bmi;
//    }
//
//    public float getHeight() {
//        return height;
//    }
//
//    public float getWeight() {
//        return weight;
//    }
//
//
//}
