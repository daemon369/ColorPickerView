package me.daemon.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import me.daemon.colorpicker.internal.Callback
import me.daemon.colorpicker.internal.ColorPicker
import me.daemon.colorpicker.painter.IAlphaPainter
import me.daemon.colorpicker.painter.IBrightnessPainter
import me.daemon.colorpicker.painter.IPalettePainter
import me.daemon.colorpicker.painter.impl.DefaultAlphaPainter
import me.daemon.colorpicker.painter.impl.DefaultBrightnessPainter
import me.daemon.colorpicker.painter.impl.DefaultPalettePainter
import me.daemon.colorpicker.view.AlphaView
import me.daemon.colorpicker.view.BrightnessView
import me.daemon.colorpicker.view.PaletteView

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
class ColorPickerView @JvmOverloads constructor(
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

    private var paletteCenterX: Int = 0
    private var paletteCenterY: Int = 0

    private val colorPicker = ColorPicker().apply { addCallback(this@ColorPickerView) }

    private val paletteView = PaletteView(context, attrs).apply {
        setColorPicker(colorPicker)
        painter = DefaultPalettePainter()
    }

    private val brightnessView = BrightnessView(context, attrs).apply {
        setColorPicker(colorPicker)
        painter = DefaultBrightnessPainter()
    }

    private val alphaView = AlphaView(context, attrs).apply {
        setColorPicker(colorPicker)
        painter = DefaultAlphaPainter()
    }

    private var isAddingInternal = false

    private var disallowInterceptTouchEvent = false

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessEnable: Boolean = true
        set(brightnessEnable) {
            if (field != brightnessEnable) {
                field = brightnessEnable

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessWidth: Int = 0
        set(brightnessWidth) {
            if (field != brightnessWidth) {
                field = brightnessWidth

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessHeight: Int = 0
        set(brightnessHeight) {
            if (field != brightnessHeight) {
                field = brightnessHeight

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessGravity: Gravity = Gravity.CENTER_BOTTOM
        set(brightnessGravity) {
            if (field != brightnessGravity) {
                field = brightnessGravity

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessOffsetX: Int = 0
        set(brightnessOffsetX) {
            if (field != brightnessOffsetX) {
                field = brightnessOffsetX

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessOffsetY: Int = 0
        set(brightnessOffsetY) {
            if (field != brightnessOffsetY) {
                field = brightnessOffsetY

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaEnable: Boolean = true
        set(alphaEnable) {
            if (field != alphaEnable) {
                field = alphaEnable

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaWidth: Int = 0
        set(alphaWidth) {
            if (field != alphaWidth) {
                field = alphaWidth

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaHeight: Int = 0
        set(alphaHeight) {
            if (field != alphaHeight) {
                field = alphaHeight

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaGravity: Gravity = Gravity.CENTER_BOTTOM
        set(alphaGravity) {
            if (field != alphaGravity) {
                field = alphaGravity

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaOffsetX: Int = 0
        set(alphaOffsetX) {
            if (field != alphaOffsetX) {
                field = alphaOffsetX

                invalidate()
            }
        }

    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaOffsetY: Int = 0
        set(alphaOffsetY) {
            if (field != alphaOffsetY) {
                field = alphaOffsetY

                invalidate()
            }
        }

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

            brightnessEnable = t.getBoolean(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessEnable, true)
            brightnessWidth = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessWidth, 0f).toInt()
            brightnessHeight = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessHeight, 0f).toInt()
            brightnessOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOffsetX, 0f).toInt()
            brightnessOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOffsetY, 0f).toInt()

            alphaEnable = t.getBoolean(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaEnable, true)
            alphaWidth = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaWidth, 0f).toInt()
            alphaHeight = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaHeight, 0f).toInt()
            alphaOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaOffsetX, 0f).toInt()
            alphaOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaOffsetY, 0f).toInt()
        } finally {
            t.recycle()
        }

        addViewInternal(paletteView)
        addViewInternal(brightnessView)
        addViewInternal(alphaView)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        updatePaletteCenter(w, h)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val wSize = MeasureSpec.getSize(widthMeasureSpec)

        measureChild(
                paletteView,
                MeasureSpec.makeMeasureSpec(paletteRadius, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(paletteRadius, MeasureSpec.EXACTLY)
        )

        // TODO fix brightnessView & alphaView measurement
        measureChild(
                brightnessView,
                wSize,
                MeasureSpec.makeMeasureSpec(40, MeasureSpec.EXACTLY)
        )

        measureChild(
                alphaView,
                wSize,
                MeasureSpec.makeMeasureSpec(40, MeasureSpec.EXACTLY)
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

        // TODO fix brightnessView & alphaView layout
        brightnessView.layout(
                0,
                measuredHeight - brightnessView.measuredHeight * 3,
                measuredWidth,
                measuredHeight - brightnessView.measuredHeight * 2
        )

        alphaView.layout(
                0,
                measuredHeight - alphaView.measuredHeight,
                measuredWidth,
                measuredHeight
        )
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (disallowInterceptTouchEvent) {
                    // resolve touch conflicts
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (disallowInterceptTouchEvent) {
                    // resolve touch conflicts
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        for (i in 0 until childCount) {
            getChildAt(i).isEnabled = enabled
        }
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

    override fun callback(
            color: Int,
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
     * @param painter custom palette painter
     *                       调色板绘制器
     */
    fun setPalettePainter(painter: IPalettePainter?) = apply { paletteView.painter = painter }

    fun getPalettePainter() = paletteView.painter

    fun setBrightnessPainter(painter: IBrightnessPainter?) = apply { brightnessView.painter = painter }

    fun getBrightnessPainter() = brightnessView.painter

    fun setAlphaPainter(painter: IAlphaPainter?) = apply { alphaView.painter = painter }

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
        val gravity = Gravity.calibrate(paletteGravity)

        val paletteCenterX = when (gravity) {
            Gravity.LEFT_TOP, Gravity.LEFT_CENTER, Gravity.LEFT_BOTTOM -> paletteRadius
            Gravity.RIGHT_TOP, Gravity.RIGHT_CENTER, Gravity.RIGHT_BOTTOM -> w - paletteRadius
            else -> w / 2
        } + paletteOffsetX

        val paletteCenterY = when (gravity) {
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