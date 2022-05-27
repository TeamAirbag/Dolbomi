package altermarkive.guardian

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class BmiActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)


        val heightEditText : EditText = findViewById(R.id.heightEditText)  // 명시적
        val weightEditText = findViewById<EditText>(R.id.weightEditText)   // 추론적
        val resultButton = findViewById<Button>(R.id.resultButton)

        resultButton.setOnClickListener {
            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이 아래로는 절대 빈 값이 올 수 없음

            val height: Int = heightEditText.text.toString().toInt()
            val weight: Int = weightEditText.text.toString().toInt()

            val bmi = weight / (height / 100.0).pow(2.0)
            val resultText = when {
                bmi >= 35.0 -> "고도 비만"
                bmi >= 30.0 -> "중정도 비만"
                bmi >= 25.0 -> "경도 비만"
                bmi >= 23.0 -> "과체충"
                bmi >= 18.5 -> "정상 체중"
                else -> "저체중"
            }

            val bmi2 = String.format("%.2f", bmi)

            val resultValueTextView = findViewById<TextView>(R.id.bmiResultTextView)
            val resultStringTextView = findViewById<TextView>(R.id.resultTextView)

            resultValueTextView.text = bmi2.toString()
            resultStringTextView.text = resultText

        }
    }
}