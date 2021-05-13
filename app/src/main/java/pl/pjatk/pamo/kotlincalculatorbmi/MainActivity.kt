package pl.pjatk.pamo.kotlincalculatorbmi


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var heightText: EditText? = null
    var weightText: EditText? = null
    var button: Button? = null
    var rButton: RadioButton? = null

    var intentAdult: Intent? = null
    var intentChild: Intent? = null

    var isAdult = false

    var height = 0f
    var weight = 0f
    var calcHeight = false
    var calcWeight = false
    var bmi = 0f

    var intentLogo: Intent? = null

    var playGame: ImageButton? = null
    var startGame: Intent? = null

    var statBtn: Button? = null
    var showStat: Intent? = null
    var statBtnHeightActive = false
    var statBtnWeightActive = false
    var statBtnDisabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun getAllLayoutIds() {
        heightText = findViewById<View>(R.id.human_height) as EditText
        weightText = findViewById<View>(R.id.human_weight) as EditText
        button = findViewById<View>(R.id.calc_btn) as Button
        rButton = findViewById<View>(R.id.adult) as RadioButton
        statBtn = findViewById<View>(R.id.stat_btn) as Button
    }

    private fun initIntents() {
        intentAdult = Intent(this, AdultCalcResultActivity::class.java)
        intentChild = Intent(this, ChildCalcResultActivity::class.java)
        showStat = Intent(this, ShowStatActivity::class.java)
    }

    private fun setBtnListener() {
        button!!.setOnClickListener {
            isChildOrAdultChecker()
            if (isAdult) {
                heightExceptionHandler(1, 270)
                weightExceptionHandler(1, 300)
                startAdultActivityChecker()
            } else {   // for child
                heightExceptionHandler(1, 260)
                weightExceptionHandler(1, 260)
                startChildActivityChecker()
            }
        }
    }

        open fun heightTextFormat() {
            heightText!!.setText("")
            heightText!!.hint = "Podaj prawidłowy wzrost"
            calcHeight = false
        }

        open fun weightTextFormat() {
            weightText!!.setText("")
            weightText!!.hint = "Podaj prawidłową wagę"
            calcWeight = false
        }

        open fun heightExceptionHandler(minHeight: Int, maxHeight: Int) {
            if (heightText!!.text.toString() == "") {
                heightTextFormat()
            } else {
                height = heightText!!.text.toString().toFloat()
                if (height > maxHeight || height < minHeight) {
                    heightTextFormat()
                } else {
                    calcHeight = true
                }
            }
        }

        open fun weightExceptionHandler(minWeight: Int, maxWeight: Int) {
            if (weightText!!.text.toString() == "") {
                weightTextFormat()
            } else {
                weight = weightText!!.text.toString().toFloat()
                if (weight > maxWeight || weight < minWeight) {
                    weightTextFormat()
                } else {
                    calcWeight = true
                }
            }
        }

        open fun startAdultActivityChecker() {
            if (calcHeight && calcWeight) {
                bmi = calculateBmi(height, weight)
                intentAdult!!.putExtra(AdultCalcResultActivity.BMI_RESULT, bmi)
                intentAdult!!.putExtra("height", height)
                intentAdult!!.putExtra("weight", weight)
                startActivity(intentAdult)
            }
        }

        open fun startChildActivityChecker() {
            if (calcHeight && calcWeight) {
                bmi = calculateBmi(height, weight)
                intentChild!!.putExtra(ChildCalcResultActivity.BMI_RESULT, bmi)
                startActivity(intentChild)
            }
        }

        open fun calculateBmi(height: Float, weight: Float): Float {
            var height = height
            var bmi = 0f
            height /= 100f
            bmi = weight / (height * height)
            return bmi
        }

        fun isChildOrAdultChecker() {
            isAdult = rButton!!.isChecked
        }

        open fun startLogoWelcome() {
            intentLogo = Intent(this, StartLogoActivity::class.java)
            startActivityForResult(intentLogo, 1)
        }

        fun getHeight(): Float {
            return height
        }

        fun getWeight(): Float {
            return weight
        }

        open fun setGameListener() {
            playGame = findViewById<View>(R.id.play_game) as ImageButton
            startGame = Intent(this, GameMainActivity::class.java)
            playGame!!.setOnClickListener { startActivity(startGame) }
        }

        open fun setStatBtnListener() {
            statBtn!!.setOnClickListener {
                isChildOrAdultChecker()
                if (isAdult) startActivity(showStat) else {
                    statBtn!!.isEnabled = false
                    statBtnDisabled = true
                    Toast.makeText(
                        applicationContext, "Dane statystyczne dotyczą tylko osób dorosłych.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        open fun setStatDataListener() {
            heightText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    isChildOrAdultChecker()
                    statBtnHeightActive = if (isAdult) true else false
                    if (statBtnHeightActive && statBtnWeightActive) statBtn!!.isEnabled = true
                }
            })
            weightText!!.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {}
                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    isChildOrAdultChecker()
                    statBtnWeightActive = if (isAdult) true else false
                    if (statBtnHeightActive && statBtnWeightActive) statBtn!!.isEnabled = true
                }
            })
        }

        open fun setRadioButtonListener() {
            rButton!!.setOnClickListener {
                isChildOrAdultChecker()
                if (statBtnDisabled && isAdult) {
                    statBtn!!.isEnabled = true
                }
            }
        }

    }