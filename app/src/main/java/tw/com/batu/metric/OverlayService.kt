package tw.com.batu.metric

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import android.view.WindowManager
import androidx.core.view.updatePadding
import kotlinx.parcelize.Parcelize

class OverlayService : Service() {

    companion object {

        private const val EXTRA_INPUT = "extra_input"

        fun newIntent(context: Context, input: Input? = null) =
            Intent(context, OverlayService::class.java).apply {
                input?.let { putExtra(EXTRA_INPUT, it) }
            }
    }

    @Parcelize
    data class Input(
        val systemTopInset: Int,
        val systemBottomInset: Int,
        val interval: Int
    ) : Parcelable

    inner class OverlayBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): OverlayService = this@OverlayService
    }

    private val binder = OverlayBinder()
    private var windowManager: WindowManager? = null

    var interval = 0
        set(value) {
            field = value
            if (value > 0) {
                updateOverlayView()
            } else {
                removeOverlayView()
            }
        }

    private var systemTopInset = 0
        set(value) {
            field = value
            updateOverlayView()
        }
    private var systemBottomInset = 0
        set(value) {
            field = value
            updateOverlayView()
        }

    private var metricView: MetricView? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        metricView = MetricView(this)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // This is the important flag
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        windowManager?.addView(metricView, params)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.getParcelableExtraCompat<Input>(EXTRA_INPUT)?.let { input ->
            interval = input.interval
            systemTopInset = input.systemTopInset
            systemBottomInset = input.systemBottomInset
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        removeOverlayView()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun removeOverlayView() {
        metricView?.let {
            windowManager?.removeView(it)
        }
    }

    private fun updateOverlayView() {
        metricView?.let {
            it.interval = this.interval
            Log.d("badu", "systemTopInset: $systemTopInset, systemBottomInset: $systemBottomInset")
            //  TODO by Batu: need more testing about the padding setting
//            it.updatePadding(
//                top = systemTopInset,
//                bottom = systemBottomInset
//            )
        }
    }

    private inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
                key,
                T::class.java
            )

            else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
        }
}