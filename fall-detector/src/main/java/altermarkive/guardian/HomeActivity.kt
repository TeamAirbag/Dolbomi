package altermarkive.guardian

//import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 3000 // 1 sec
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var actionBar : ActionBar?
        actionBar = supportActionBar;
        actionBar?.hide()

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, Main::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}