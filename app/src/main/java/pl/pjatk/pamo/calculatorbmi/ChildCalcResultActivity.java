package pl.pjatk.pamo.calculatorbmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChildCalcResultActivity extends AppCompatActivity {

    public static final String BMI_RESULT = "bmi_result";

    EditText childAge;

    TextView bmiTextView;

    Button girlBtn;
    Button boyBtn;

    Intent girlPercentIntent;
    Intent boyPercentIntent;

    double bmiRound;
    String childAgeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_calc_result);

        setAllLayoutIds();
        initIntents();
        setBmiTextView();
        setBtnListener(girlBtn, girlPercentIntent);
        setBtnListener(boyBtn, boyPercentIntent);

    }



    private void setAllLayoutIds() {
        bmiTextView = (TextView) findViewById(R.id.bmiChildResult);
        childAge = (EditText) findViewById(R.id.childAge);
        girlBtn = (Button) findViewById(R.id.girlBtn);
        boyBtn = (Button) findViewById(R.id.boyBtn);
    }

    private void initIntents() {
        girlPercentIntent = new Intent(this, GirlPercentActivity.class);
        boyPercentIntent = new Intent(this, BoyPercentActivity.class);
    }

    private void setBmiTextView() {
        Bundle b = getIntent().getExtras();
        Float bmi = b.getFloat(BMI_RESULT, -1);

        bmiRound = roundBmi(bmi);

        bmiTextView.setText(String.valueOf(bmiRound));
    }

    private double roundBmi(float bmi) {

        double ret = 0.0;
        int floorBmi = Math.round(bmi * 10);
        ret = floorBmi / 10.0;
        return ret;
    }

    private void setBtnListener(Button btn, Intent intent) {

        final Intent intentDestination = intent;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeActivity(intentDestination);
            }
        });
    }


    private void changeActivity(Intent intent) {

        editTextExceptionHandler(intent);

    }

    private void editTextExceptionHandler(Intent intent) {

        if(childAge.getText().toString().equals("") ) {

            editTextFormat(childAge);

        }  else  {

            int ageInt = Integer.parseInt(childAge.getText().toString());

            if(ageInt < 2 || ageInt > 20) {

                editTextFormat(childAge);

            } else {

                childAgeString = String.valueOf(childAge.getText());

                intent.putExtra(GirlPercentActivity.BMI_RESULT, bmiRound);
                intent.putExtra(GirlPercentActivity.CHILD_AGE, childAgeString);
                startActivity(intent);

            }
        }

    }

    private void editTextFormat(EditText childAge) {

        childAge.setHint("Podaj prawidłowy wiek (2-20)");
        childAge.setText("");
        childAge.setWidth(650);

    }


}

