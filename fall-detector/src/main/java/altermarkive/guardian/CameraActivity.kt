package altermarkive.guardian

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class CameraActivity : AppCompatActivity() {

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val FLAG_PERM_CAMERA = 100
    val FLAG_REQ_CAMERA = 100



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if (IsPermitted(CAMERA_PERMISSION)) {
            openCamera()
        }
        else{
            ActivityCompat.requestPermissions(this,CAMERA_PERMISSION,FLAG_PERM_CAMERA)
        }

        var buttonCamera=findViewById<Button>(R.id.buttonCamera)

        buttonCamera.setOnClickListener {
            if (IsPermitted(CAMERA_PERMISSION)) {
                openCamera()
            }
            else{
                ActivityCompat.requestPermissions(this,CAMERA_PERMISSION,FLAG_PERM_CAMERA)
            }
        }
    }
    fun IsPermitted(permission:Array<String>) : Boolean{
        for(permission in permission) {
            val result = ContextCompat.checkSelfPermission(this, permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,FLAG_REQ_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var imagePreview= findViewById<ImageView>(R.id.imagePreview)
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                FLAG_REQ_CAMERA->{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    imagePreview.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            FLAG_PERM_CAMERA->{
                var checked = true
                for(grant in grantResults){
                    if(grant!= PackageManager.PERMISSION_GRANTED){
                        checked=false
                        break
                    }
                }
                if(checked){
                    openCamera()
                }
            }
        }
    }
}