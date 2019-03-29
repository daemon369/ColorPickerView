package me.daemon.colorpicker.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewDebug
import me.daemon.colorpicker.Orientation
import me.daemon.colorpicker.R
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorCenter
import me.daemon.colorpicker.painter.IAlphaPainter

/**
 * @author daemon
 * @since 2019-03-11 08:24
 */
class AlphaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), Callback, IView<AlphaView.AlphaValue> {

    @ViewDebug.ExportedProperty(category = "daemon")
    var orientation: Orientation = Orientation.HORIZONTAL
        set(orientation) {
            if (field != orientation) {
                field = orientation

                invalidate()
            }
        }

    private lateinit var colorCenter: ColorCenter

    var painter: IAlphaPainter? = null
        set(value) {
            field = value?.apply {
                updateByValue(
                        this@AlphaView
                )
            }
        }

    private var isChanging = false

    class AlphaValue : IView.Value() {

        var alpha: Float = 1f
            private set

        fun setValue(alpha: Float) = apply {
            this.alpha = alpha
            set()
        }
    }

    private val alphaValue = AlphaValue()

    init {
        @SuppressLint("CustomViewStyleable")
        val t = context.obtainStyledAttributes(attrs, R.styleable.DaemonCpColorPickerView)

        try {
            val orientationInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOrientation, Orientation.HORIZONTAL.ordinal)
            orientation = Orientation.from(orientationInt)
            if (orientation == Orientation.UNKNOWN) {
                throw IllegalArgumentException("Illegal orientation: $orientationInt")
            }
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
            MotionEvent.ACTION_DOWN -> {
                isChanging = true

                update(x, y, true)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                update(x, y, true)
            }

            MotionEvent.ACTION_UP -> {
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
            addCallback(this@AlphaView)
        }
    }

    private fun update(
            x: Float,
            y: Float,
            propagate: Boolean
    ) {
        val painter = painter ?: return

        alphaValue.reset()
        painter.onUpdate(this, x, y)

        if (!alphaValue.set) {
            throw java.lang.IllegalStateException("AlphaView{$this}: ${painter.javaClass.name}" +
                    "#onUpdate did not update alpha by calling" +
                    " AlphaValue#setValue(Float)"
            )
        }

        colorCenter
                .beginTransaction()
                .alpha(alphaValue.alpha)
                .commit(propagate, force = true)
    }

    override fun callback(
            color: Int,
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
        alphaValue.setValue(alpha)
        painter?.onColorChanged(this, color)
        invalidate()
    }

    override fun getValue() = alphaValue

    override fun getColor() = colorCenter.getColor()

}