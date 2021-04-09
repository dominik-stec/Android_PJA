package pl.pjatk.pamo.calculatorbmi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    EditText heightText;
    EditText weightText;
    Button button;
    RadioButton rButton;

    Intent intentAdult;
    Intent intentChild;

    boolean isAdult;

    float height;
    float weight;
    boolean calcHeight;
    boolean calcWeight;
    float bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calcHeight = false;
        calcWeight = false;

        getAllLayoutIds();
        initIntents();
        setBtnListener();

    }

    private void getAllLayoutIds() {
        heightText = (EditText) findViewById(R.id.human_height);
        weightText = (EditText) findViewById(R.id.human_weight);
        button = (Button) findViewById(R.id.calc_btn);
        rButton = (RadioButton) findViewById(R.id.adult);
    }

    private void initIntents() {
        intentAdult = new Intent(this, AdultCalcResultActivity.class);
        intentChild = new Intent(this, ChildCalcResultActivity.class);
    }

    private void setBtnListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isChildOrAdultChecker();

                if(isAdult) {

                    heightExceptionHandler(1, 270);
                    weightExceptionHandler(1, 300);
                    startAdultActivityChecker();

                } else  {   // for child

                    heightExceptionHandler(1, 260);
                    weightExceptionHandler(1, 260);
                    startChildActivityChecker();

                }
            }
        });

    }

    private void heightTextFormat() {
        heightText.setText("");
        heightText.setHint("Podaj prawidłowy wzrost");
        calcHeight = false;
    }

    private void weightTextFormat() {
        weightText.setText("");
        weightText.setHint("Podaj prawidłową wagę");
        calcWeight = false;
    }

    private void heightExceptionHandler(int minHeight, int maxHeight) {

        if(heightText.getText().toString().equals("")) {

            heightTextFormat();

        } else {

            height = Float.parseFloat(heightText.getText().toString());
            if(height > maxHeight || height < minHeight) {

                heightTextFormat();

            } else {
                calcHeight = true;
            }
        }
    }

    private void weightExceptionHandler(int minWeight, int maxWeight) {

        if(weightText.getText().toString().equals("")) {

            weightTextFormat();

        } else {

            weight = Float.parseFloat(weightText.getText().toString());
            if(weight > maxWeight || weight < minWeight) {

                weightTextFormat();

            } else {
                calcWeight = true;
            }
        }
    }

    private void startAdultActivityChecker() {

        if (calcHeight && calcWeight) {

            bmi = calculateBmi(height, weight);

            intentAdult.putExtra(AdultCalcResultActivity.BMI_RESULT, bmi);
            startActivity(intentAdult);
        }
    }

    private void startChildActivityChecker() {

        if (calcHeight && calcWeight) {

            bmi = calculateBmi(height, weight);

            intentChild.putExtra(ChildCalcResultActivity.BMI_RESULT, bmi);
            startActivity(intentChild);
        }
    }

    private float calculateBmi(float height, float weight) {
        float bmi = 0;
        height /= 100;
        bmi = weight / (height*height);
        return bmi;
    }

    public void isChildOrAdultChecker() {
        this.isAdult = rButton.isChecked();
    }
}
