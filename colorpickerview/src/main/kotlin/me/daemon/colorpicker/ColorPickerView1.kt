package me.daemon.colorpicker

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewDebug
import android.view.ViewGroup
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.DefaultPalettePainter1
import me.daemon.colorpicker.painter.PalettePainter1

/**
 * color picker view
 *
 * 颜色选择器View
 *
 * [GitHub](https://github.com/daemon369/ColorPickerView)
 *
 * [jcenter](https://bintray.com/beta/#/daemon336699/maven/colorpickerview?tab=overview)
 *
 * @author daemon
 * @since 2019-02-26 21:53
 */
class ColorPickerView1 @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), ColorObservable, Callback {

    /**
     * radius of palette
     *
     * 调色板半径
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteRadius: Int = 0
        /**
         * set radius of palette
         *
         * 设置调色板半径
         */
        set(paletteRadius) {
            if (field != paletteRadius) {
                field = paletteRadius

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteGravity: Gravity = Gravity.CENTER
        set(paletteGravity) {
            if (field != paletteGravity) {
                field = paletteGravity

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteOffsetX: Int = 0
        set(paletteOffsetX) {
            if (field != paletteOffsetX) {
                field = paletteOffsetX

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteOffsetY: Int = 0
        set(paletteOffsetY) {
            if (field != paletteOffsetY) {
                field = paletteOffsetY

                invalidate()
            }
        }

    /**
     * when paletteGravity is basic gravity, transform it to combination gravity
     */
    private val realPaletteGravity: Gravity
        get() {
            return when (paletteGravity) {
                Gravity.LEFT -> Gravity.LEFT_CENTER
                Gravity.TOP -> Gravity.CENTER_TOP
                Gravity.RIGHT -> Gravity.RIGHT_CENTER
                Gravity.BOTTOM -> Gravity.CENTER_BOTTOM
                Gravity.CENTER_VERTICAL, Gravity.CENTER_HORIZONTAL -> Gravity.CENTER
                else -> paletteGravity
            }
        }

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0

    private val colorPicker = ColorPicker().apply { addCallback(this@ColorPickerView1) }

    private val paletteView = PaletteView(context).apply {
        setColorPicker(colorPicker)
        setPalettePainter(DefaultPalettePainter1())
    }

    private var isAddingInternal = false

    init {
        val t = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)

        try {
            paletteRadius = t.getDimension(R.styleable.ColorPickerView_paletteRadius, 0f).toInt()
            val paletteGravityInt = t.getInt(R.styleable.ColorPickerView_paletteGravity, 0)
            if (paletteGravityInt == 0) {
                // using Gravity.CENTER if paletteRadius attribute not been set
                paletteGravity = Gravity.CENTER
            } else {
                paletteGravity = Gravity.from(paletteGravityInt)
                if (paletteGravity == Gravity.UNKNOWN) {
                    throw IllegalArgumentException("Illegal paletteGravity: $paletteGravityInt")
                }
            }

            paletteOffsetX = t.getDimension(R.styleable.ColorPickerView_paletteOffsetX, 0f).toInt()
            paletteOffsetY = t.getDimension(R.styleable.ColorPickerView_paletteOffsetY, 0f).toInt()

            val initialColor = t.getColor(R.styleable.ColorPickerView_initialColor, Color.BLACK)
            setColor(initialColor)
        } finally {
            t.recycle()
        }

        addViewInternal(paletteView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updatePaletteCenter(w, h)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChild(
                paletteView,
                MeasureSpec.makeMeasureSpec(paletteRadius, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(paletteRadius, MeasureSpec.EXACTLY)
        )

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        paletteView.layout(
                paletteCenterX - paletteRadius,
                paletteCenterY - paletteRadius,
                paletteCenterX + paletteRadius,
                paletteCenterY + paletteRadius
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    private fun addViewInternal(child: View) {
        isAddingInternal = true
        super.addView(child)
        isAddingInternal = false
    }

    override fun addView(child: View?) {
        if (isAddingInternal) {
            super.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (isAddingInternal) {
            super.addView(child, index)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (isAddingInternal) {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View?, params: LayoutParams?) {
        if (isAddingInternal) {
            super.addView(child, params)
        }
    }

    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        if (isAddingInternal) {
            super.addView(child, index, params)
        }
    }

    override fun removeView(view: View?) {
    }

    override fun removeViewAt(index: Int) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    /**
     * set current picked color
     *
     * 设置选中颜色值
     *
     * @param color 颜色值
     */
    fun setColor(color: Int) {
        colorPicker.setColor(color, true)
    }

    override fun callback(
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
    }

    /**
     * set custom palette painter
     *
     * 设置自定义调色板绘制器
     *
     * @param palettePainter custom palette painter
     *                       调色板绘制器
     */
    fun setPalettePainter(palettePainter: PalettePainter1?) {
        paletteView.setPalettePainter(palettePainter)
    }

    override fun subscribe(observer: ColorObserver) {
        colorPicker.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        colorPicker.unsubscribe(observer)
    }

    override fun getColor(): Int {
        return colorPicker.getColor()
    }

    private fun updatePaletteCenter(w: Int, h: Int) {
        val paletteCenterX = when (realPaletteGravity) {
            Gravity.LEFT_TOP, Gravity.LEFT_CENTER, Gravity.LEFT_BOTTOM -> paletteRadius
            Gravity.RIGHT_TOP, Gravity.RIGHT_CENTER, Gravity.RIGHT_BOTTOM -> w - paletteRadius
            else -> w / 2
        } + paletteOffsetX

        val paletteCenterY = when (realPaletteGravity) {
            Gravity.LEFT_TOP, Gravity.CENTER_TOP, Gravity.RIGHT_TOP -> paletteRadius
            Gravity.LEFT_BOTTOM, Gravity.CENTER_BOTTOM, Gravity.RIGHT_BOTTOM -> w - paletteRadius
            else -> h / 2
        } + paletteOffsetY

        if (this.paletteCenterX == paletteCenterX
                && this.paletteCenterY == paletteCenterY) {
            // nothing will happen
            return
        }

        this.paletteCenterX = paletteCenterX
        this.paletteCenterY = paletteCenterY

        invalidate()
    }

}