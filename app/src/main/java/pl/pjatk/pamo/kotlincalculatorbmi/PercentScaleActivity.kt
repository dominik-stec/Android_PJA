package pl.pjatk.pamo.kotlincalculatorbmi

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PercentScaleActivity : AppCompatActivity() {
    var bmi: TextView? = null
    var age: TextView? = null
    var percent: TextView? = null
    var category: TextView? = null
    var percentVal: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_percent_scale)
        allId
        setTextEditValuesByKey()
        calculateCategory(percentVal)
    }

    private val allId: Unit
        private get() {
            bmi = findViewById<View>(R.id.bmi) as TextView
            age = findViewById<View>(R.id.age) as TextView
            percent = findViewById<View>(R.id.percent) as TextView
            category = findViewById<View>(R.id.category) as TextView
        }

    private fun setTextEditValuesByKey() {
        val b = intent.extras
        val bmiVal = b!!.getDouble(BMI)
        val ageVal = b.getString(AGE)
        percentVal = b.getString(PERCENT)
        setEditTextValuesById(bmi, bmiVal, age, ageVal, percent, percentVal)
    }

    private fun setEditTextValuesById(
        bmi: TextView?,
        bmiVal: Double,
        age: TextView?,
        ageVal: String?,
        percent: TextView?,
        percentVal: String?
    ) {
        bmi!!.text = bmiVal.toString()
        age!!.text = ageVal
        percent!!.text = percentVal
    }

    private fun calculateCategory(percentValString: String?) {
        val percentInt = percentValString!!.toInt()
        var categoryStr = ""
        if (percentInt < 5) categoryStr =
            "Niedowagę" else if (percentInt >= 5 && percentInt < 25) categoryStr =
            "Szczupłość" else if (percentInt >= 25 && percentInt < 85) categoryStr =
            "Wagę prawidłową" else if (percentInt >= 85 && percentInt < 95) categoryStr =
            "Nadwagę" else if (percentInt >= 95) categoryStr = "Otyłość"
        category!!.text = categoryStr
    }

    companion object {
        const val BMI = "bmi"
        const val AGE = "age"
        const val PERCENT = "percent"
    }
}


//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class PercentScaleActivity extends AppCompatActivity {
//
//    public static final String BMI = "bmi";
//    public static final String AGE = "age";
//    public static final String PERCENT = "percent";
//
//    TextView bmi;
//    TextView age;
//    TextView percent;
//    TextView category;
//
//    String percentVal;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_percent_scale);
//
//        getAllId();
//        setTextEditValuesByKey();
//        calculateCategory(percentVal);
//
//    }
//
//    private void getAllId() {
//        bmi = (TextView) findViewById(R.id.bmi);
//        age = (TextView) findViewById(R.id.age);
//        percent = (TextView) findViewById(R.id.percent);
//        category = (TextView) findViewById(R.id.category);
//    }
//
//    private void setTextEditValuesByKey() {
//        Bundle b = getIntent().getExtras();
//        Double bmiVal = b.getDouble(BMI);
//        String ageVal = b.getString(AGE);
//        percentVal = b.getString(PERCENT);
//
//        setEditTextValuesById(bmi, bmiVal, age, ageVal, percent, percentVal);
//    }
//
//   private void setEditTextValuesById(TextView bmi, double bmiVal, TextView age, String ageVal, TextView percent, String percentVal) {
//       bmi.setText(String.valueOf(bmiVal));
//       age.setText(ageVal);
//       percent.setText(percentVal);
//   }
//
//   private void calculateCategory(String percentValString) {
//       int percentInt = Integer.parseInt(percentValString);
//       String categoryStr = "";
//       if(percentInt < 5) categoryStr = "Niedowagę";
//       else if (percentInt >= 5 && percentInt < 25) categoryStr = "Szczupłość";
//       else if (percentInt >= 25 && percentInt < 85) categoryStr = "Wagę prawidłową";
//       else if (percentInt >= 85 && percentInt < 95) categoryStr = "Nadwagę";
//       else if (percentInt >= 95) categoryStr = "Otyłość";
//       category.setText(categoryStr);
//   }
//}
