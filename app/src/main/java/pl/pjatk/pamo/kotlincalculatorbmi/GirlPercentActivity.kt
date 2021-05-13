package pl.pjatk.pamo.kotlincalculatorbmi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class GirlPercentActivity : AppCompatActivity() {
    var bmiView: TextView? = null
    var ageView: TextView? = null
    var percentVal: EditText? = null
    var submitPercentBtn: Button? = null
    var percentScaleActIntent: Intent? = null
    var bmi: Double? = null
    var age: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_girl_percent)
        allLayoutIds
        bundleValues
        setLayoutFields()
        setBtnListener(submitPercentBtn)
    }

    private val allLayoutIds: Unit
        private get() {
            bmiView = findViewById<View>(R.id.girlBmi) as TextView
            ageView = findViewById<View>(R.id.girlAge) as TextView
            percentVal = findViewById<View>(R.id.percentVal) as EditText
            submitPercentBtn = findViewById<View>(R.id.calcChildBmiCategory) as Button
        }
    private val bundleValues: Unit
        private get() {
            val b = intent.extras
            bmi = b!!.getDouble(BMI_RESULT, -1.0)
            age = b.getString(CHILD_AGE, "null")
        }

    private fun setLayoutFields() {
        bmiView!!.text = bmi.toString()
        ageView!!.text = age
    }

    private fun setBtnListener(btn: Button?) {
        percentScaleActIntent = Intent(this, PercentScaleActivity::class.java)
        btn!!.setOnClickListener { setEditTextExceptionHandler() }
    }

    private fun setEditTextExceptionHandler() {
        if (percentVal!!.text.toString() == "") {
            resetTextField()
        } else {
            val percentInt = percentVal!!.text.toString().toInt()
            if (percentInt < 1 || percentInt > 100) {
                resetTextField()
            } else {
                putLayoutIntentData()
            }
        }
    }

    private fun resetTextField() {
        percentVal!!.setText("")
        percentVal!!.hint = "podaj centyl od 1 do 100"
    }

    private fun putLayoutIntentData() {
        val percent = percentVal!!.text.toString()
        percentScaleActIntent!!.putExtra(
            PercentScaleActivity.BMI,
            bmi!!
        )
        percentScaleActIntent!!.putExtra(PercentScaleActivity.AGE, age)
        percentScaleActIntent!!.putExtra(
            PercentScaleActivity.PERCENT,
            percent
        )
        startActivity(percentScaleActIntent)
    }

    companion object {
        const val BMI_RESULT = "bmi_result"
        const val CHILD_AGE = "child_age"
    }
}


//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//
//public class GirlPercentActivity extends AppCompatActivity {
//
//    public static final String BMI_RESULT = "bmi_result";
//    public static final String CHILD_AGE = "child_age";
//
//    TextView bmiView;
//    TextView ageView;
//
//    EditText percentVal;
//
//    Button submitPercentBtn;
//
//    Intent percentScaleActIntent;
//
//    Double bmi;
//    String age;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_girl_percent);
//
//        getAllLayoutIds();
//        getBundleValues();
//        setLayoutFields();
//        setBtnListener(submitPercentBtn);
//
//    }
//
//    private void getAllLayoutIds() {
//        bmiView = (TextView) findViewById(R.id.girlBmi);
//        ageView = (TextView) findViewById(R.id.girlAge);
//        percentVal = (EditText) findViewById(R.id.percentVal);
//        submitPercentBtn = (Button) findViewById(R.id.calcChildBmiCategory);
//    }
//
//    private void getBundleValues() {
//        Bundle b = getIntent().getExtras();
//        bmi = b.getDouble(BMI_RESULT, -1);
//        age = b.getString(CHILD_AGE,"null");
//    }
//
//    private void setLayoutFields() {
//        bmiView.setText(String.valueOf(bmi));
//        ageView.setText(age);
//    }
//
//    private void setBtnListener(Button btn) {
//
//        percentScaleActIntent = new Intent(this, pl.pjatk.pamo.calculatorbmi.PercentScaleActivity.class);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                setEditTextExceptionHandler();
//
//            }
//        });
//    }
//
//    private void setEditTextExceptionHandler() {
//
//        if(percentVal.getText().toString().equals(""))  {
//
//            resetTextField();
//
//        } else {
//
//            int percentInt = Integer.parseInt(percentVal.getText().toString());
//
//            if(percentInt < 1 || percentInt > 100) {
//
//                resetTextField();
//
//            } else {
//
//                putLayoutIntentData();
//
//            }
//        }
//    }
//
//    private void resetTextField() {
//        percentVal.setText("");
//        percentVal.setHint("podaj centyl od 1 do 100");
//    }
//
//    private void putLayoutIntentData() {
//        String percent = String.valueOf(percentVal.getText());
//
//        percentScaleActIntent.putExtra(pl.pjatk.pamo.calculatorbmi.PercentScaleActivity.BMI, bmi);
//        percentScaleActIntent.putExtra(pl.pjatk.pamo.calculatorbmi.PercentScaleActivity.AGE, age);
//        percentScaleActIntent.putExtra(pl.pjatk.pamo.calculatorbmi.PercentScaleActivity.PERCENT, percent);
//        startActivity(percentScaleActIntent);
//    }
//}
