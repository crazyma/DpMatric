package tw.com.batu.metric

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class MetricView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var interval: Int = 0
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
            val startHeight: Int = paddingTop
            val endHeight: Int = height - paddingBottom
            for (i in 0..width step interval.dpToPixelSize()) {
                canvas.drawLine(
                    i.toFloat(),
                    startHeight.toFloat(),
                    i.toFloat(),
                    endHeight.toFloat(),
                    paint
                )
            }

            for (i in startHeight..endHeight step interval.dpToPixelSize()) {
                canvas.drawLine(0f, i.toFloat(), width.toFloat(), i.toFloat(), paint)
            }
        }
    }

    private fun Int.dpToPixel() = (this * context.resources.displayMetrics.density)
    private fun Int.dpToPixelSize() = this.dpToPixel().roundToInt()
}

