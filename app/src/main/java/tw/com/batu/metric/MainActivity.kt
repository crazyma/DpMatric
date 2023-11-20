package tw.com.batu.metric

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tw.com.batu.metric.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_OVERLAY_PERMISSION = 1
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (Settings.canDrawOverlays(this)) {
                // permission granted...
                launchOverlay2()
            } else {
                // permission not granted...
                Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupViews(){
        with(binding){
            permissionCardView.setOnClickListener {
                checkPermission()
            }

            emptyCardView.setOnClickListener {

            }

            twoCardView.setOnClickListener {

            }

            fourCardView.setOnClickListener {

            }

            eightCardView.setOnClickListener {

            }

            sixteenCardView.setOnClickListener {

            }

            thirtyTwoCardView.setOnClickListener {

            }
        }
    }

    private fun checkPermission(){
        if (!Settings.canDrawOverlays(this)) {
            // ask for setting
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
        } else {
            //  DO nothing
        }
    }

    private fun launchOverlay() {
        val intent = Intent(this, OverlayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun launchOverlay2() {
        startService(Intent(this, OverlayService::class.java))
    }
}