package altermarkive.guardian

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
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

//        setSupportActionBar(toolbar) // 툴바 적용

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



        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu) // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false) // 툴바에 타이틀 안보이게

        // 네비게이션 드로어 생성
        drawerLayout = findViewById(R.id.drawer_layout)
        // 네비게이션 드로어 내에있는 화면의 이벤트를 처리하기 위해 생성
        navigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this) //navigation 리스너

//        eula(this)

        createNotificationChannel(CHANNEL_ID, "testChannel", "this is a test Channel")

        //initDatabase()




        myRef.addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (messageData in dataSnapshot.children) {
                    Log.d("첫번째 리스너 속 로그임ㅋㅋ", "값"+messageData.value)
                    if(messageData.value=="fall")
                        displayNotification()
                    // child 내에 있는 데이터만큼 반복합니다.
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

        val notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.dolbomi)
            .setContentTitle("Example")
            .setContentText("This is Notification Test")
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    fun createNotificationChannel(channelId: String, name: String, channelDescription: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT // set importance
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
                Log.d("두번째 리스너 속 로그임ㅋㅋ", "값"+s)
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

        // 클릭한 툴바 메뉴 아이템 id 마다 다르게 실행하도록 설정
        when(item!!.itemId){
            android.R.id.home->{
                // 햄버거 버튼 클릭시 네비게이션 드로어 열기
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item1-> Toast.makeText(this,"menu_item1 실행", Toast.LENGTH_SHORT).show()
            R.id.menu_item2-> {
                var intent = Intent(this, BmiActivity::class.java)
                startActivity(intent) }
            R.id.menu_item3-> Toast.makeText(this,"menu_item3 실행", Toast.LENGTH_SHORT).show()
        }
        return false
    }



}
