package me.daemon.colorpicker.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewDebug
import me.daemon.colorpicker.Orientation
import me.daemon.colorpicker.R
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorCenter
import me.daemon.colorpicker.painter.IBrightnessPainter

/**
 * @author daemon
 * @since 2019-02-22 14:40
 */
class BrightnessView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback, IView<BrightnessView.BrightnessValue> {

    @ViewDebug.ExportedProperty(category = "daemon")
    var orientation: Orientation = Orientation.HORIZONTAL
        set(orientation) {
            if (field != orientation) {
                field = orientation

                invalidate()
            }
        }

    private lateinit var colorCenter: ColorCenter

    var painter: IBrightnessPainter? = null
        set(painter) {
            field = painter?.apply {
                updateByValue(
                        this@BrightnessView
                )
            }
        }

    private var isChanging = false

    class BrightnessValue : IView.Value() {

        var brightness: Float = 1f
            private set

        fun setValue(brightness: Float) = apply {
            this.brightness = brightness
            set()
        }
    }

    private val brightnessValue = BrightnessValue()

    init {
        @SuppressLint("CustomViewStyleable")
        val t = context.obtainStyledAttributes(attrs, R.styleable.DaemonCpColorPickerView)

        try {
            val orientationInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOrientation, Orientation.HORIZONTAL.ordinal)
            orientation = Orientation.from(orientationInt)
        } finally {
            t.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        painter?.onSizeChanged(
                this,
                w,
                h
        )

        painter?.updateByValue(
                this
        )
    }

    override fun onDraw(canvas: Canvas) {
        painter?.onDraw(
                this,
                canvas,
                isChanging
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        painter ?: return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            ACTION_DOWN -> {
                isChanging = true

                update(x, y, true)
                return true
            }

            ACTION_MOVE -> {
                update(x, y, true)
            }

            ACTION_UP -> {
                isChanging = false

                update(x, y, true)

                performClick()
            }

        }

        return super.onTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        colorCenter.addCallback(this)
    }

    override fun onDetachedFromWindow() {
        colorCenter.removeCallback(this)
        super.onDetachedFromWindow()
    }

    internal fun setColorCenter(colorCenter: ColorCenter) {
        this.colorCenter = colorCenter.apply {
            addCallback(this@BrightnessView)
        }
    }

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = painter ?: return

        brightnessValue.reset()
        painter.onUpdate(this, x, y)

        if (!brightnessValue.set) {
            throw java.lang.IllegalStateException("BrightnessView{$this}: ${painter.javaClass.name}" +
                    "#onUpdate did not update brightness by calling" +
                    " BrightnessValue#setValue(Float)"
            )
        }

        colorCenter
                .beginTransaction()
                .brightness(brightnessValue.brightness)
                .commit(propagate, force = true)
    }

    override fun callback(
            color: Int,
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
        brightnessValue.setValue(brightness)
        painter?.onColorChanged(this, color)
        invalidate()
    }

    override fun getValue() = brightnessValue

    override fun getColor() = colorCenter.getColor()

}