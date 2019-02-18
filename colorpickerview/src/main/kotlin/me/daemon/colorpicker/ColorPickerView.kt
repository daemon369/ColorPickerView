package me.daemon.colorpicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.os.Build
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewDebug
import android.view.ViewParent
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.DefaultIndicatorPainter
import me.daemon.colorpicker.painter.DefaultPalettePainter
import me.daemon.colorpicker.painter.IndicatorPainter
import me.daemon.colorpicker.painter.PalettePainter

/**
 * color picker view
 * 颜色选择器View
 *
 * @author daemon
 * @since 2019-01-27 18:04
 */
class ColorPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), ColorObservable {

    /**
     * padding of palette
     *
     * 调色板留白
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var palettePadding: Int = 0
        /**
         * set padding of palette
         *
         * 设置调色板留白
         *
         * @param palettePadding 调色板留白
         */
        set(palettePadding) {
            if (this.palettePadding != palettePadding) {
                field = palettePadding

                invalidate()
            }
        }

    /**
     * whether automatic measure this ColorPickerView as square,
     * if true then minimum size of width and height will used as
     * measured width and height
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var square = false
        /**
         * set square mode
         *
         * 设置正方形模式
         *
         * @param square whether use square mode
         */
        set(square) {
            if (this.square != square) {
                field = square

                invalidate()
            }
        }

    private val currentPoint: PointF = PointF()

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0

    private val colorPicker: ColorPicker = ColorPicker()

    private val defaultBrightnessProvider = object : BrightnessProvider {
        override val brightness: Float
            get() = 1.0f
    }

    private var brightnessProvider: BrightnessProvider? = null

    private val defaultPalettePainter = DefaultPalettePainter()

    private var palettePainter: PalettePainter? = null

    private val defaultIndicatorPainter = DefaultIndicatorPainter()

    private var indicatorPainter: IndicatorPainter? = null

    private var isChanging = false

    private var disallowInterceptTouchEven = false

    private val radius: Int
        get() {
            val width = measuredWidth - paddingLeft - paddingRight - this.palettePadding * 2
            val height = measuredHeight - paddingTop - paddingBottom - this.palettePadding * 2
            val limit = Math.max(Math.min(width, height), 0)

            return limit / 2
        }

    init {

        val t = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView)

        try {
            palettePadding = t.getDimension(R.styleable.ColorPickerView_palettePadding, 0f).toInt()

            val initialColor = t.getColor(R.styleable.ColorPickerView_initialColor, Color.BLACK)
            setColorInternal(initialColor, true)

            val disallowInterceptTouchEven = t.getBoolean(
                    R.styleable.ColorPickerView_disallowInterceptTouchEvent,
                    false)
            setDisallowInterceptTouchEven(disallowInterceptTouchEven)
        } finally {
            t.recycle()
        }

    }

    /**
     * set current picked color
     *
     * 设置选中颜色值
     *
     * @param color 颜色值
     */
    fun setColor(color: Int) {
        setColorInternal(color, true)
    }

    /**
     * resolve touch conflict with it's parent and ancestors<br></br><br></br>
     *
     * if disallow is set to be true, [ColorPickerView]  will call
     * it's [parent][ViewParent.getParent]'s
     * [ViewParent.requestDisallowInterceptTouchEvent]
     * when received [MotionEvent.ACTION_DOWN] event in
     * [ColorPickerView.onTouchEvent]
     * to disallow it's parent and ancestors to intercept touch event,
     * and restore when received [MotionEvent.ACTION_UP] event
     * <br></br><br></br>
     * 解决[ColorPickerView]与其父View或祖先View的触摸事件冲突<br></br><br></br>
     *
     * disallow设置为true时，在[ColorPickerView.onTouchEvent]
     * 方法中接收到[MotionEvent.ACTION_DOWN] 触摸事件时会调用
     * [父View][ViewParent.getParent]的
     * [ViewParent.requestDisallowInterceptTouchEvent]方法来紧张父View
     * 及祖先View拦截触摸事件，并在收到[MotionEvent.ACTION_UP]触摸事件时恢复
     *
     * @param disallow whether to disallow it's parent and ancestors to intercept touch event
     *                 是否禁止父View或祖先View拦截触摸事件冲突
     */
    fun setDisallowInterceptTouchEven(disallow: Boolean) {
        this.disallowInterceptTouchEven = disallow
    }

    private fun setColorInternal(color: Int, notify: Boolean) {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        setColorInternal(hsv[0], hsv[1], hsv[2], Color.alpha(color) / 255f, notify)
    }

    private fun setColorInternal(hue: Float, saturation: Float, brightness: Float, alpha: Float, notify: Boolean) {
        val r = saturation * radius
        val radian = (hue / 180f * Math.PI).toFloat()
        updateIndicator((r * Math.cos(radian.toDouble()) + paletteCenterX).toFloat(), (-r * Math.sin(radian.toDouble()) + paletteCenterY).toFloat())

        colorPicker.beginTransaction().hue(hue).saturation(saturation).brightness(brightness).alpha(alpha).commit(notify)

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!this.square) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)

        val size = Math.min(width, height)

        super.onMeasure(
                View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        paletteCenterX = w / 2
        paletteCenterY = h / 2

        val palettePainter = this.palettePainter ?: defaultPalettePainter

        palettePainter.onSizeChanged(w, h, radius, paletteCenterX, paletteCenterY)

        setColorInternal(colorPicker.getHue(), colorPicker.getSaturation(), colorPicker.getBrightness(), colorPicker.getAlpha(), false)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = radius

        val palettePainter = this.palettePainter ?: defaultPalettePainter

        palettePainter.drawPalette(
                this,
                canvas,
                radius, paletteCenterX,
                paletteCenterY
        )

        drawIndicator(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)

        val clickable = (isClickable
                || isLongClickable
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isContextClickable)
        if (!clickable) return super.onTouchEvent(event)

        val x = event.x
        val y = event.y

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (disallowInterceptTouchEven) {
                    // resolve touch conflicts
                    parent?.requestDisallowInterceptTouchEvent(true)
                }

                isChanging = true
                update(x, y)
                return true
            }

            MotionEvent.ACTION_MOVE -> update(x, y)

            MotionEvent.ACTION_UP -> {
                isChanging = false
                update(x, y)

                performClick()

                if (disallowInterceptTouchEven) {
                    // resolve touch conflicts
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())

        savedState.color = colorPicker.getColor()

        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.superState)

            setColor(state.color)

        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        val provider = indicatorPainter ?: defaultIndicatorPainter
        provider.drawIndicator(this, canvas, currentPoint, colorPicker.getColor(), isChanging)
    }

    private fun update(eventX: Float, eventY: Float) {
        val x = eventX - paletteCenterX
        val y = eventY - paletteCenterY
        val r = Math.sqrt((x * x + y * y).toDouble())

        val bp = brightnessProvider ?: defaultBrightnessProvider
        val b = bp.brightness

        val hue = (Math.atan2(y.toDouble(), (-x).toDouble()) / Math.PI * 180f).toFloat() + 180
        val saturation = Math.max(0f, Math.min(1f, (r / radius).toFloat()))
        val brightness = Math.max(0f, Math.min(1f, b))
        val alpha = 1f

        setColorInternal(hue, saturation, brightness, alpha, true)
    }

    private fun updateIndicator(eventX: Float, eventY: Float) {
        var x = eventX - paletteCenterX
        var y = eventY - paletteCenterY
        val r = Math.sqrt((x * x + y * y).toDouble())

        val radius = radius
        if (r > radius) {
            x *= (radius / r).toFloat()
            y *= (radius / r).toFloat()
        }
        currentPoint.x = x + paletteCenterX
        currentPoint.y = y + paletteCenterY

        invalidate()

    }

    /**
     * set custom palette painter, using [DefaultPalettePainter] as default
     * palette painter
     *
     * 设置自定义调色板绘制器，默认使用[DefaultPalettePainter]
     *
     * @param palettePainter custom palette painter
     *                       调色板绘制器
     */
    fun setPalettePainter(palettePainter: PalettePainter) {
        this.palettePainter = palettePainter
    }

    /**
     * set custom indicator painter, using [DefaultIndicatorPainter]
     * if custom indicator not been set
     *
     * 设置自定义指示器绘制器，如果没有设置则使用[默认绘制器][DefaultIndicatorPainter]
     *
     * @param indicatorPainter custom indicator painter
     */
    fun setIndicatorPainter(indicatorPainter: IndicatorPainter) {
        this.indicatorPainter = indicatorPainter
    }

    /**
     * set custom brightness provider
     *
     * 设置自定义透明度提供器
     *
     * @param brightnessProvider custom brightness provider
     */
    fun setBrightnessProvider(brightnessProvider: BrightnessProvider) {
        this.brightnessProvider = brightnessProvider
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

}
