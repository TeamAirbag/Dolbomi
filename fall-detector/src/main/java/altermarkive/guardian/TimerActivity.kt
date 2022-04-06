package altermarkive.guardian

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class TimerActivity : AppCompatActivity() {
    var count = 60
    var TimerTextView: TextView? = null
    var handler = Handler()
    var runnable: Runnable = object : Runnable {
        override fun run() {
            TimerTextView!!.text = count.toString() + ""
            count--
            if (count < 0) {
                handler.removeCallbacks(this)
            } else {
                handler.postDelayed(this, 1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        TimerTextView = findViewById(R.id.TimerTextView)
        handler.post(runnable)
    }
}