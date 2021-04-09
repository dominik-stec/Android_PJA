package pl.pjatk.pamo.calculatorbmi;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GirlPercentActivity extends AppCompatActivity {

    public static final String BMI_RESULT = "bmi_result";
    public static final String CHILD_AGE = "child_age";

    TextView bmiView;
    TextView ageView;

    EditText percentVal;

    Button submitPercentBtn;

    Intent percentScaleActIntent;

    Double bmi;
    String age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_girl_percent);

        getAllLayoutIds();
        getBundleValues();
        setLayoutFields();
        setBtnListener(submitPercentBtn);

    }

    private void getAllLayoutIds() {
        bmiView = (TextView) findViewById(R.id.girlBmi);
        ageView = (TextView) findViewById(R.id.girlAge);
        percentVal = (EditText) findViewById(R.id.percentVal);
        submitPercentBtn = (Button) findViewById(R.id.calcChildBmiCategory);
    }

    private void getBundleValues() {
        Bundle b = getIntent().getExtras();
        bmi = b.getDouble(BMI_RESULT, -1);
        age = b.getString(CHILD_AGE,"null");
    }

    private void setLayoutFields() {
        bmiView.setText(String.valueOf(bmi));
        ageView.setText(age);
    }

    private void setBtnListener(Button btn) {

        percentScaleActIntent = new Intent(this, PercentScaleActivity.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEditTextExceptionHandler();

            }
        });
    }

    private void setEditTextExceptionHandler() {

        if(percentVal.getText().toString().equals(""))  {

            resetTextField();

        } else {

            int percentInt = Integer.parseInt(percentVal.getText().toString());

            if(percentInt < 1 || percentInt > 100) {

                resetTextField();

            } else {

                putLayoutIntentData();

            }
        }
    }

    private void resetTextField() {
        percentVal.setText("");
        percentVal.setHint("podaj centyl od 1 do 100");
    }

    private void putLayoutIntentData() {
        String percent = String.valueOf(percentVal.getText());

        percentScaleActIntent.putExtra(PercentScaleActivity.BMI, bmi);
        percentScaleActIntent.putExtra(PercentScaleActivity.AGE, age);
        percentScaleActIntent.putExtra(PercentScaleActivity.PERCENT, percent);
        startActivity(percentScaleActIntent);
    }
}
