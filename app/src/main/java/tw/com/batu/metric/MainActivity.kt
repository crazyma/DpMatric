package tw.com.batu.metric

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import tw.com.batu.metric.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var overlayService: OverlayService? = null
    private var serviceBound: Boolean = false

    private lateinit var binding: ActivityMainBinding
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as OverlayService.OverlayBinder
            overlayService = binder.getService()
            serviceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBound = false
        }
    }

    private val requestOverlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        switchTopViewState()
    }

    private val isOverlayPermissionGranted: Boolean
        get() = Settings.canDrawOverlays(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    override fun onStart() {
        super.onStart()
        // If the Service is existing then bind to it, else nothing happens.
        bindService(OverlayService.newIntent(this), connection, 0)
    }

    override fun onStop() {
        unbindService(connection)
        super.onStop()
    }

    private fun setupViews() {
        with(binding) {

            permissionCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    //  Do nothing.
                } else {
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    ).let(requestOverlayPermissionLauncher::launch)
                }
            }

            emptyCardView.setOnClickListener {
                if (isOverlayPermissionGranted && overlayService != null) {
                    stopService()
                }
            }

            twoCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    updateOverlayService(2)
                }
            }

            fourCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    updateOverlayService(4)
                }
            }

            eightCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    updateOverlayService(8)
                }
            }

            sixteenCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    updateOverlayService(16)
                }
            }

            thirtyTwoCardView.setOnClickListener {
                if (isOverlayPermissionGranted) {
                    updateOverlayService(32)
                }
            }
        }
    }

    private fun switchTopViewState() {
        if (isOverlayPermissionGranted) {
            //  TODO by Batu: setup granted layout
        } else {
            //  TODO by Batu: setup not granted layout
        }
    }

    private fun updateOverlayService(interval: Int) {
        if(overlayService == null) {
            launchOverlayService(interval)
        }else{
            overlayService?.interval = interval
        }
    }

    private fun launchOverlayService(interval: Int) {
        startService(OverlayService.newIntent(this, interval))
        bindService(OverlayService.newIntent(this), connection, 0)
    }

    private fun stopService() {
        stopService(Intent(this, OverlayService::class.java)).takeIf { it }?.run {
            overlayService = null
        }
    }
}