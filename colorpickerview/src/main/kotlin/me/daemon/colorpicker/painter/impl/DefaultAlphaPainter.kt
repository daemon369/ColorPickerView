package me.daemon.colorpicker.painter.impl

import android.graphics.*
import me.daemon.colorpicker.Orientation
import me.daemon.colorpicker.painter.IAlphaPainter
import me.daemon.colorpicker.view.AlphaView

/**
 * @author daemon
 * @since 2019-03-12 21:41
 */
class DefaultAlphaPainter : IAlphaPainter {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val solidPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectorPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val selectorPath = Path()
    private val currentSelectorPath = Path()

    private var selectorSize = 0f

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 0F
        borderPaint.color = Color.BLACK

        selectorPaint.color = Color.BLACK

        selectorPath.fillType = Path.FillType.WINDING
    }

    override fun onSizeChanged(
            view: AlphaView,
            w: Int,
            h: Int
    ) {
        updatePainter(view, view.getColor())

        selectorSize = when (view.orientation) {
            Orientation.HORIZONTAL -> h
            Orientation.VERTICAL -> w
        } * 0.25f

        selectorPath.apply {
            reset()
            when (view.orientation) {
                Orientation.HORIZONTAL -> {
                    moveTo(0f, 0f)
                    lineTo(selectorSize * 2, 0f)
                    lineTo(selectorSize, selectorSize)
                }
                Orientation.VERTICAL -> {
                    moveTo(0f, selectorSize)
                    lineTo(selectorSize, 0f)
                    lineTo(selectorSize, selectorSize * 2)
                }
            }
            close()
        }
    }

    override fun onDraw(
            view: AlphaView,
            canvas: Canvas,
            isChanging: Boolean
    ) {
        val width = view.width
        val height = view.height

        when (view.orientation) {
            Orientation.HORIZONTAL -> {
                canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), solidPaint)
                canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), borderPaint)

                selectorPath.offset(view.getValue().alpha * (width - 2 * selectorSize), 0f, currentSelectorPath)
            }
            Orientation.VERTICAL -> {
                canvas.drawRect(0f, selectorSize, width - selectorSize, height - selectorSize, solidPaint)
                canvas.drawRect(0f, selectorSize, width - selectorSize, height - selectorSize, borderPaint)

                selectorPath.offset(width - selectorSize, view.getValue().alpha * (height - 2 * selectorSize), currentSelectorPath)
            }
        }
        canvas.drawPath(currentSelectorPath, selectorPaint)
    }

    override fun onUpdate(
            view: AlphaView,
            x: Float,
            y: Float
    ) {
        var currentValue = when (view.orientation) {
            Orientation.HORIZONTAL -> (x - selectorSize) / (view.width - 2 * selectorSize)
            Orientation.VERTICAL -> (y - selectorSize) / (view.height - 2 * selectorSize)
        }
        currentValue = Math.max(0f, Math.min(1f, currentValue))
        view.getValue().setValue(currentValue)
    }

    override fun updateByValue(view: AlphaView) {
    }

    override fun onColorChanged(view: AlphaView, color: Int) {
        updatePainter(view, color)
    }

    private fun updatePainter(
            view: AlphaView,
            color: Int
    ) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        val startColor = Color.HSVToColor(0, hsv)
        val endColor = Color.HSVToColor(255, hsv)

        val shader = LinearGradient(
                0f,
                0f,
                view.width.toFloat(),
                view.height.toFloat(),
                startColor,
                endColor,
                Shader.TileMode.CLAMP
        )

        solidPaint.shader = shader
    }
}