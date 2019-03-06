package me.daemon.colorpicker

import android.annotation.SuppressLint
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
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.DefaultIndicatorPainter
import me.daemon.colorpicker.painter.DefaultPalettePainter
import me.daemon.colorpicker.painter.IndicatorPainter
import me.daemon.colorpicker.painter.PalettePainter

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
 * @since 2019-01-27 18:04
 */
class ColorPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), ColorObservable, Callback {

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

    private val currentPoint: PointF = PointF()

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0

    private val colorPicker: ColorPicker = ColorPicker().apply { addCallback(this@ColorPickerView) }

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

    private var disallowInterceptTouchEvent = false

    init {
        @SuppressLint("CustomViewStyleable")
        val t = context.obtainStyledAttributes(attrs, R.styleable.DaemonCpColorPickerView)

        try {
            paletteRadius = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteRadius, 0f).toInt()
            val paletteGravityInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteGravity, 0)
            if (paletteGravityInt == 0) {
                // using Gravity.CENTER if paletteRadius attribute not been set
                paletteGravity = Gravity.CENTER
            } else {
                paletteGravity = Gravity.from(paletteGravityInt)
                if (paletteGravity == Gravity.UNKNOWN) {
                    throw IllegalArgumentException("Illegal paletteGravity: $paletteGravityInt")
                }
            }

            paletteOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteOffsetX, 0f).toInt()
            paletteOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteOffsetY, 0f).toInt()

            val initialColor = t.getColor(R.styleable.DaemonCpColorPickerView_daemon_cp_initialColor, Color.BLACK)
            setColor(initialColor)

            val disallowInterceptTouchEvent = t.getBoolean(
                    R.styleable.DaemonCpColorPickerView_daemon_cp_disallowInterceptTouchEvent,
                    false)
            setDisallowInterceptTouchEvent(disallowInterceptTouchEvent)
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
        colorPicker.setColor(color, true)
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
    fun setDisallowInterceptTouchEvent(disallow: Boolean) {
        this.disallowInterceptTouchEvent = disallow
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        updatePaletteCenter(w, h)

        colorPicker.setColor(colorPicker.getColor(), false)
    }

    override fun onDraw(canvas: Canvas) {
        (palettePainter ?: defaultPalettePainter).drawPalette(
                this,
                canvas,
                paletteRadius,
                paletteCenterX,
                paletteCenterY
        )

        (indicatorPainter ?: defaultIndicatorPainter).drawIndicator(
                this,
                canvas,
                currentPoint,
                colorPicker.getColor(),
                isChanging
        )
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
                if (disallowInterceptTouchEvent) {
                    // resolve touch conflicts
                    parent?.requestDisallowInterceptTouchEvent(true)
                }

                isChanging = true
                update(x, y, true)
                return true
            }

            MotionEvent.ACTION_MOVE -> update(x, y, false)

            MotionEvent.ACTION_UP -> {
                isChanging = false
                update(x, y, true)

                performClick()

                if (disallowInterceptTouchEvent) {
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

    private fun update(eventX: Float, eventY: Float, forceCommit: Boolean) {
        val x = eventX - paletteCenterX
        val y = eventY - paletteCenterY
        val r = Math.sqrt((x * x + y * y).toDouble())

        val b = (brightnessProvider ?: defaultBrightnessProvider).brightness

        val hue = (Math.atan2(y.toDouble(), (-x).toDouble()) / Math.PI * 180f).toFloat() + 180
        val saturation = Math.max(0f, Math.min(1f, (r / paletteRadius).toFloat()))
        val brightness = Math.max(0f, Math.min(1f, b))
        val alpha = 1f

        colorPicker
                .beginTransaction()
                .hue(hue)
                .saturation(saturation)
                .brightness(brightness)
                .alpha(alpha)
                .commit(true, forceCommit)
    }

    private fun updateIndicator(eventX: Float, eventY: Float) {
        var x = eventX - paletteCenterX
        var y = eventY - paletteCenterY
        val r = Math.sqrt((x * x + y * y).toDouble())

        if (r > paletteRadius) {
            val ratio = (paletteRadius / r).toFloat()
            x *= ratio
            y *= ratio
        }
        currentPoint.x = x + paletteCenterX
        currentPoint.y = y + paletteCenterY

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
    fun setPalettePainter(palettePainter: PalettePainter?) {
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
    fun setIndicatorPainter(indicatorPainter: IndicatorPainter?) {
        this.indicatorPainter = indicatorPainter
    }

    /**
     * set custom brightness provider
     *
     * 设置自定义透明度提供器
     *
     * @param brightnessProvider custom brightness provider
     */
    fun setBrightnessProvider(brightnessProvider: BrightnessProvider?) {
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

    override fun callback(
            hue: Float,
            saturation: Float,
            brightness: Float,
            alpha: Float
    ) {
        val r = saturation * paletteRadius
        val radian = (hue / 180f * Math.PI).toFloat()
        updateIndicator((r * Math.cos(radian.toDouble()) + paletteCenterX).toFloat(), (-r * Math.sin(radian.toDouble()) + paletteCenterY).toFloat())
        invalidate()
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

        (palettePainter ?: defaultPalettePainter).onSizeChanged(
                w,
                h,
                paletteRadius,
                paletteCenterX,
                paletteCenterY
        )

        invalidate()
    }
}
