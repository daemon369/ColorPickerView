package me.daemon.colorpicker.painter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF

import me.daemon.colorpicker.ColorPickerView

/**
 * @author daemon
 * @since 2019-02-09 22:29
 */
class DefaultIndicatorPainter : IndicatorPainter {

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun drawIndicator(
            colorPickerView: ColorPickerView,
            canvas: Canvas,
            point: PointF,
            color: Int,
            isChanging: Boolean
    ) {

        indicatorPaint.color = color
        indicatorPaint.strokeWidth = 2f

        val radius = 20

        canvas.drawLine(
                point.x - radius,
                point.y,
                point.x + radius,
                point.y,
                indicatorPaint)
        canvas.drawLine(
                point.x,
                point.y - radius,
                point.x,
                point.y + radius,
                indicatorPaint)
    }
}
