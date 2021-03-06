package altermarkive.guardian

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot


class Main : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    val CHANNEL_ID = "testChannel01"   // Channel for notification
    var notificationManager: NotificationManager? = null

    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()


    private var mChild: ChildEventListener? = null

//    private fun eula(context: Context) {
//        // Run the guardian
//        Guardian.initiate(this)
//        // Load the EULA
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.eula)
//        dialog.setTitle("EULA")
//        val web = dialog.findViewById<View>(R.id.eula) as WebView
//        web.loadUrl("file:///android_asset/eula.html")
//        val accept = dialog.findViewById<View>(R.id.accept) as Button
//        accept.setOnClickListener { dialog.dismiss() }
//        val layout = WindowManager.LayoutParams()
//        val window = dialog.window
//        window ?: return
//        layout.copyFrom(window.attributes)
//        layout.width = WindowManager.LayoutParams.MATCH_PARENT
//        layout.height = WindowManager.LayoutParams.MATCH_PARENT
//        window.attributes = layout
//        dialog.show()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setSupportActionBar(toolbar) // ?????? ??????

//        Detector.instance(this)
        val binding = altermarkive.guardian.databinding.MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.about, R.id.signals, R.id.settings))


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        supportActionBar?.setDisplayHomeAsUpEnabled(true) // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu) // ????????? ????????? ??????
        supportActionBar?.setDisplayShowTitleEnabled(false) // ????????? ????????? ????????????

        // ??????????????? ????????? ??????
        drawerLayout = findViewById(R.id.drawer_layout)
        // ??????????????? ????????? ???????????? ????????? ???????????? ???????????? ?????? ??????
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) //navigation ?????????

//        eula(this)

        createNotificationChannel(CHANNEL_ID, "testChannel", "this is a test Channel")

        //initDatabase()




        myRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (messageData in dataSnapshot.children) {
                    Log.d("????????? ????????? ??? ???????????????", "???"+messageData.value)
                    if(messageData.value=="fall")
                        displayNotification()
                    // child ?????? ?????? ??????????????? ???????????????.
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }
    override fun onStart() {
        super.onStart()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun displayNotification() {
        val notificationId = 45

        val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val intent = Intent(this, Main::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.dolbomi)
            .setContentTitle("???????????? ??????")
            .setContentText("?????? ????????????")
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    fun createNotificationChannel(channelId: String, name: String, channelDescription: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH // set importance
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = channelDescription
            }
            // Register the channel with the system
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun initDatabase() {
        mChild = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {}
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                var s = dataSnapshot.getValue()
                Log.d("????????? ????????? ??? ???????????????", "???"+s)
                if(dataSnapshot.getValue()=="fall")
                    displayNotification()
            }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        myRef.addChildEventListener(mChild as ChildEventListener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // ????????? ?????? ?????? ????????? id ?????? ????????? ??????????????? ??????
        when(item!!.itemId){
            android.R.id.home->{
                // ????????? ?????? ????????? ??????????????? ????????? ??????
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item1-> Toast.makeText(this,"menu_item1 ??????", Toast.LENGTH_SHORT).show()
            R.id.menu_item2-> {
                var intent = Intent(this, BmiActivity::class.java)
                startActivity(intent) }
            R.id.menu_item3-> Toast.makeText(this,"menu_item3 ??????", Toast.LENGTH_SHORT).show()
        }
        return false
    }



}