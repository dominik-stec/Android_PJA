package pl.pjatk.pamo.calculatorbmi;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import pl.pjatk.pamo.calculatorbmi.game.GameMainActivity;


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

    Intent intentLogo;

    ImageButton playGame;
    Intent startGame;

    Button statBtn;
    Intent showStat;
    boolean statBtnHeightActive = false;
    boolean statBtnWeightActive = false;
    boolean statBtnDisabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLogoWelcome();

        setContentView(R.layout.activity_main);

        calcHeight = false;
        calcWeight = false;

        getAllLayoutIds();
        initIntents();
        setBtnListener();

        setGameListener();

        setStatDataListener();
        setStatBtnListener();
        statBtn.setEnabled(false);

        setRadioButtonListener();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void getAllLayoutIds() {
        heightText = (EditText) findViewById(R.id.human_height);
        weightText = (EditText) findViewById(R.id.human_weight);
        button = (Button) findViewById(R.id.calc_btn);
        rButton = (RadioButton) findViewById(R.id.adult);

        statBtn = (Button) findViewById(R.id.stat_btn);
    }

    private void initIntents() {
        intentAdult = new Intent(this, AdultCalcResultActivity.class);
        intentChild = new Intent(this, ChildCalcResultActivity.class);

        showStat = new Intent(this, ShowStatActivity.class);
    }

    private void setBtnListener() {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isChildOrAdultChecker();

                if (isAdult) {

                    heightExceptionHandler(1, 270);
                    weightExceptionHandler(1, 300);
                    startAdultActivityChecker();

                } else {   // for child

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

        if (heightText.getText().toString().equals("")) {

            heightTextFormat();

        } else {

            height = Float.parseFloat(heightText.getText().toString());
            if (height > maxHeight || height < minHeight) {

                heightTextFormat();

            } else {
                calcHeight = true;
            }
        }
    }

    private void weightExceptionHandler(int minWeight, int maxWeight) {

        if (weightText.getText().toString().equals("")) {

            weightTextFormat();

        } else {

            weight = Float.parseFloat(weightText.getText().toString());
            if (weight > maxWeight || weight < minWeight) {

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
            intentAdult.putExtra("height", height);
            intentAdult.putExtra("weight", weight);
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
        bmi = weight / (height * height);
        return bmi;
    }

    public void isChildOrAdultChecker() {
        this.isAdult = rButton.isChecked();
    }

    private void startLogoWelcome() {
        intentLogo = new Intent(this, StartLogoActivity.class);
        startActivityForResult(intentLogo, 1);
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    private void setGameListener() {

        playGame = (ImageButton) findViewById(R.id.play_game);
        startGame = new Intent(this, GameMainActivity.class);

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startGame);
            }
        });
    }

    private void setStatBtnListener() {

        statBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChildOrAdultChecker();
                if(isAdult)
                    startActivity(showStat);
                else {
                    statBtn.setEnabled(false);
                    statBtnDisabled = true;
                    Toast.makeText(getApplicationContext(), "Dane statystyczne dotyczą tylko osób dorosłych.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setStatDataListener() {

        heightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                isChildOrAdultChecker();
                if(isAdult) statBtnHeightActive = true;
                else statBtnHeightActive = false;

                if(statBtnHeightActive && statBtnWeightActive)
                    statBtn.setEnabled(true);

            }
        });

        weightText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                isChildOrAdultChecker();
                if(isAdult) statBtnWeightActive = true;
                else statBtnWeightActive = false;

                if(statBtnHeightActive && statBtnWeightActive)
                    statBtn.setEnabled(true);

            }
        });
    }

    private void setRadioButtonListener() {
        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChildOrAdultChecker();
                if(statBtnDisabled && isAdult) {
                    statBtn.setEnabled(true);
                }
            }
        });
    }
}