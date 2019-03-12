package me.daemon.colorpicker.painter.impl

import android.graphics.*
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

    private var currentValue = 1f

    private var selectorSize = 0f

    init {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = 0F
        borderPaint.color = Color.BLACK

        selectorPaint.color = Color.BLACK

        selectorPath.fillType = Path.FillType.WINDING
    }

    override fun onSizeChanged(view: AlphaView, w: Int, h: Int) {
        val hsv = FloatArray(3).apply {
            Color.colorToHSV(Color.BLACK, this)
        }

        val startColor = Color.HSVToColor(hsv.apply { this[2] = 0f })
        val endColor = Color.HSVToColor(hsv.apply { this[2] = 1f })

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

        selectorSize = h * 0.25f

        selectorPath.apply {
            reset()
            moveTo(0f, 0f)
            lineTo(selectorSize * 2, 0f)
            lineTo(selectorSize, selectorSize)
            close()
        }
    }

    override fun onDraw(view: AlphaView, canvas: Canvas, isChanging: Boolean) {
        val width = view.width
        val height = view.height

        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), solidPaint)
        canvas.drawRect(selectorSize, selectorSize, width - selectorSize, height.toFloat(), borderPaint)

        selectorPath.offset(currentValue * (width - 2 * selectorSize), 0f, currentSelectorPath)
        canvas.drawPath(currentSelectorPath, selectorPaint)
    }

    override fun onUpdate(view: AlphaView, x: Float, y: Float) {
        currentValue = (x - selectorSize) / (view.width - 2 * selectorSize)
        currentValue = Math.max(0f, Math.min(1f, currentValue))
        view.getValue().setValue(currentValue)
    }

    override fun updateByValue(view: AlphaView) {
    }
}