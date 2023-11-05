package tw.com.batu.metric

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager

class OverlayService: Service() {

    override fun onCreate() {
        super.onCreate()

        val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val overlay = LayoutInflater.from(this).inflate(R.layout.activity_overlay, null)

        val params =  WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // This is the important flag
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        windowManager.addView(overlay, params)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}