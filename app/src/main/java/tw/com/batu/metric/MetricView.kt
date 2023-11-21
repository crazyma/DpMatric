package tw.com.batu.metric

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class MetricView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var interval: Int = 8
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint()

    init {
        // Initialize your custom view attributes and paint settings here
        paint.color = ContextCompat.getColor(context, R.color.ultramarine)
        paint.strokeWidth = 1.dpToPixel()
        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (interval > 0) {
            val width = width.toFloat()
            val height = height.toFloat()
            Log.d("badu", "width: $width, height: $height")

            for (i in 0..width.toInt() step interval.dpToPixelSize()) {
                canvas.drawLine(i.toFloat(), 0f, i.toFloat(), height, paint)
            }

            for (i in 0..height.toInt() step interval.dpToPixelSize()) {
                canvas.drawLine(0f, i.toFloat(), width, i.toFloat(), paint)
            }
        }
    }

    private fun Int.dpToPixel() = (this * context.resources.displayMetrics.density)
    private fun Int.dpToPixelSize() = this.dpToPixel().roundToInt()
}

