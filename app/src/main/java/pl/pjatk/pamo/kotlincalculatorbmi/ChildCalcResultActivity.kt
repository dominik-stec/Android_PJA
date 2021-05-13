package pl.pjatk.pamo.kotlincalculatorbmi

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ChildCalcResultActivity : AppCompatActivity() {
    var childAge: EditText? = null
    var bmiTextView: TextView? = null
    var girlBtn: Button? = null
    var boyBtn: Button? = null
    var girlPercentIntent: Intent? = null
    var boyPercentIntent: Intent? = null
    var bmiRound = 0.0
    var childAgeString: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_calc_result)
        setAllLayoutIds()
        initIntents()
        setBmiTextView()
        setBtnListener(girlBtn, girlPercentIntent)
        setBtnListener(boyBtn, boyPercentIntent)
    }

    private fun setAllLayoutIds() {
        bmiTextView = findViewById<View>(R.id.bmiChildResult) as TextView
        childAge = findViewById<View>(R.id.childAge) as EditText
        girlBtn = findViewById<View>(R.id.girlBtn) as Button
        boyBtn = findViewById<View>(R.id.boyBtn) as Button
    }

    private fun initIntents() {
        girlPercentIntent = Intent(this, GirlPercentActivity::class.java)
        boyPercentIntent = Intent(this, BoyPercentActivity::class.java)
    }

    private fun setBmiTextView() {
        val b = intent.extras
        val bmi = b!!.getFloat(BMI_RESULT, -1f)
        bmiRound = roundBmi(bmi)
        bmiTextView!!.text = bmiRound.toString()
    }

    private fun roundBmi(bmi: Float): Double {
        var ret = 0.0
        val floorBmi = Math.round(bmi * 10)
        ret = floorBmi / 10.0
        return ret
    }

    private fun setBtnListener(btn: Button?, intent: Intent?) {
        btn!!.setOnClickListener { changeActivity(intent) }
    }

    private fun changeActivity(intent: Intent?) {
        editTextExceptionHandler(intent)
    }

    private fun editTextExceptionHandler(intent: Intent?) {
        if (childAge!!.text.toString() == "") {
            editTextFormat(childAge)
        } else {
            val ageInt = childAge!!.text.toString().toInt()
            if (ageInt < 2 || ageInt > 20) {
                editTextFormat(childAge)
            } else {
                childAgeString = childAge!!.text.toString()
                intent!!.putExtra(
                    GirlPercentActivity.BMI_RESULT,
                    bmiRound
                )
                intent.putExtra(
                    GirlPercentActivity.CHILD_AGE,
                    childAgeString
                )
                startActivity(intent)
            }
        }
    }

    private fun editTextFormat(childAge: EditText?) {
        childAge!!.hint = "Podaj prawidłowy wiek (2-20)"
        childAge.setText("")
        childAge.width = 650
    }

    companion object {
        const val BMI_RESULT = "bmi_result"
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
//public class ChildCalcResultActivity extends AppCompatActivity {
//
//    public static final String BMI_RESULT = "bmi_result";
//
//    EditText childAge;
//
//    TextView bmiTextView;
//
//    Button girlBtn;
//    Button boyBtn;
//
//    Intent girlPercentIntent;
//    Intent boyPercentIntent;
//
//    double bmiRound;
//    String childAgeString;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_child_calc_result);
//
//        setAllLayoutIds();
//        initIntents();
//        setBmiTextView();
//        setBtnListener(girlBtn, girlPercentIntent);
//        setBtnListener(boyBtn, boyPercentIntent);
//
//    }
//
//
//
//    private void setAllLayoutIds() {
//        bmiTextView = (TextView) findViewById(R.id.bmiChildResult);
//        childAge = (EditText) findViewById(R.id.childAge);
//        girlBtn = (Button) findViewById(R.id.girlBtn);
//        boyBtn = (Button) findViewById(R.id.boyBtn);
//    }
//
//    private void initIntents() {
//        girlPercentIntent = new Intent(this, pl.pjatk.pamo.calculatorbmi.GirlPercentActivity.class);
//        boyPercentIntent = new Intent(this, pl.pjatk.pamo.calculatorbmi.BoyPercentActivity.class);
//    }
//
//    private void setBmiTextView() {
//        Bundle b = getIntent().getExtras();
//        Float bmi = b.getFloat(BMI_RESULT, -1);
//
//        bmiRound = roundBmi(bmi);
//
//        bmiTextView.setText(String.valueOf(bmiRound));
//    }
//
//    private double roundBmi(float bmi) {
//
//        double ret = 0.0;
//        int floorBmi = Math.round(bmi * 10);
//        ret = floorBmi / 10.0;
//        return ret;
//    }
//
//    private void setBtnListener(Button btn, Intent intent) {
//
//        final Intent intentDestination = intent;
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                changeActivity(intentDestination);
//            }
//        });
//    }
//
//
//    private void changeActivity(Intent intent) {
//
//        editTextExceptionHandler(intent);
//
//    }
//
//    private void editTextExceptionHandler(Intent intent) {
//
//        if(childAge.getText().toString().equals("") ) {
//
//            editTextFormat(childAge);
//
//        }  else  {
//
//            int ageInt = Integer.parseInt(childAge.getText().toString());
//
//            if(ageInt < 2 || ageInt > 20) {
//
//                editTextFormat(childAge);
//
//            } else {
//
//                childAgeString = String.valueOf(childAge.getText());
//
//                intent.putExtra(pl.pjatk.pamo.calculatorbmi.GirlPercentActivity.BMI_RESULT, bmiRound);
//                intent.putExtra(pl.pjatk.pamo.calculatorbmi.GirlPercentActivity.CHILD_AGE, childAgeString);
//                startActivity(intent);
//
//            }
//        }
//
//    }
//
//    private void editTextFormat(EditText childAge) {
//
//        childAge.setHint("Podaj prawidłowy wiek (2-20)");
//        childAge.setText("");
//        childAge.setWidth(650);
//
//    }
//
//
//}
//
