package tw.com.batu.metric

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.IBinder
import android.view.WindowManager

class OverlayService : Service() {

    companion object {

        private const val EXTRA_INTERVAL = "extra_interval"

        fun newIntent(context: Context, interval: Int? = null) =
            Intent(context, OverlayService::class.java).apply {
                interval?.let { putExtra(EXTRA_INTERVAL, it) }
            }
    }

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
        intent?.let {
            interval = it.getIntExtra(EXTRA_INTERVAL, 0)
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
        }
    }
}