package me.daemon.colorpicker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.view.*
import me.daemon.colorpicker.internal.ColorCenter
import me.daemon.colorpicker.painter.IAlphaPainter
import me.daemon.colorpicker.painter.IBrightnessPainter
import me.daemon.colorpicker.painter.IPalettePainter
import me.daemon.colorpicker.painter.impl.DefaultAlphaPainter
import me.daemon.colorpicker.painter.impl.DefaultBrightnessPainter
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
) : ViewGroup(context, attrs, defStyleAttr), ColorObservable {

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

    /**
     * palette gravity
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteGravity: Gravity = Gravity.CENTER
        set(paletteGravity) {
            if (field != paletteGravity) {
                field = paletteGravity

                invalidate()
            }
        }

    /**
     * palette center offset x
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteOffsetX: Int = 0
        set(paletteOffsetX) {
            if (field != paletteOffsetX) {
                field = paletteOffsetX

                invalidate()
            }
        }

    /**
     * palette center offset y
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var paletteOffsetY: Int = 0
        set(paletteOffsetY) {
            if (field != paletteOffsetY) {
                field = paletteOffsetY

                invalidate()
            }
        }

    private val colorCenter = ColorCenter()

    private val paletteView = PaletteView(context, attrs).apply {
        setColorCenter(colorCenter)
    }

    private val brightnessView = BrightnessView(context, attrs).apply {
        setColorCenter(colorCenter)
        painter = DefaultBrightnessPainter()
    }

    private val alphaView = AlphaView(context, attrs).apply {
        setColorCenter(colorCenter)
        painter = DefaultAlphaPainter()
    }

    private var isAddingInternal = false

    private var disallowInterceptTouchEvent = false

    /**
     * enable brightness view if `brightnessEnable` is true
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessEnable: Boolean = true
        set(brightnessEnable) {
            if (field != brightnessEnable) {
                field = brightnessEnable

                invalidate()
            }
        }

    /**
     * width of brightness view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessWidth: Int = 0
        set(brightnessWidth) {
            if (field != brightnessWidth) {
                field = brightnessWidth

                invalidate()
            }
        }

    /**
     * height of brighness view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessHeight: Int = 0
        set(brightnessHeight) {
            if (field != brightnessHeight) {
                field = brightnessHeight

                invalidate()
            }
        }

    /**
     * gravity of brightness view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessGravity: Gravity = Gravity.CENTER_BOTTOM
        set(brightnessGravity) {
            if (field != brightnessGravity) {
                field = brightnessGravity

                invalidate()
            }
        }

    /**
     * brightness view offset x
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessOffsetX: Int = 0
        set(brightnessOffsetX) {
            if (field != brightnessOffsetX) {
                field = brightnessOffsetX

                invalidate()
            }
        }

    /**
     * brightness view offset y
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var brightnessOffsetY: Int = 0
        set(brightnessOffsetY) {
            if (field != brightnessOffsetY) {
                field = brightnessOffsetY

                invalidate()
            }
        }

    /**
     * enable alpha view if `alphaEnable` is true
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaEnable: Boolean = true
        set(alphaEnable) {
            if (field != alphaEnable) {
                field = alphaEnable

                invalidate()
            }
        }

    /**
     * width of alpha view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaWidth: Int = 0
        set(alphaWidth) {
            if (field != alphaWidth) {
                field = alphaWidth

                invalidate()
            }
        }

    /**
     * height of alpha view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaHeight: Int = 0
        set(alphaHeight) {
            if (field != alphaHeight) {
                field = alphaHeight

                invalidate()
            }
        }

    /**
     * gravity of alpha view
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaGravity: Gravity = Gravity.CENTER_BOTTOM
        set(alphaGravity) {
            if (field != alphaGravity) {
                field = alphaGravity

                invalidate()
            }
        }

    /**
     * alpha view offset x
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    var alphaOffsetX: Int = 0
        set(alphaOffsetX) {
            if (field != alphaOffsetX) {
                field = alphaOffsetX

                invalidate()
            }
        }

    /**
     * alpha view offset y
     */
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
            // base
            val initialColor = t.getColor(R.styleable.DaemonCpColorPickerView_daemon_cp_initialColor, Color.BLACK)
            setColor(initialColor)

            val disallowInterceptTouchEvent = t.getBoolean(
                    R.styleable.DaemonCpColorPickerView_daemon_cp_disallowInterceptTouchEvent,
                    false)
            setDisallowInterceptTouchEvent(disallowInterceptTouchEvent)

            // palette
            paletteRadius = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteRadius, 0f).toInt()
            val paletteGravityInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteGravity, 0)
            paletteGravity = if (paletteGravityInt == 0) {
                Gravity.CENTER
            } else {
                Gravity.from(paletteGravityInt)
            }

            paletteOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteOffsetX, 0f).toInt()
            paletteOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_paletteOffsetY, 0f).toInt()

            // brightness
            brightnessEnable = t.getBoolean(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessEnable, true)
            brightnessWidth = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessWidth, 0f).toInt()
            brightnessHeight = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessHeight, 0f).toInt()
            val brightnessGravityInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessGravity, 0)
            brightnessGravity = if (brightnessGravityInt == 0) {
                Gravity.CENTER
            } else {
                Gravity.from(brightnessGravityInt)
            }
            brightnessOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOffsetX, 0f).toInt()
            brightnessOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_brightnessOffsetY, 0f).toInt()

            // alpha
            alphaEnable = t.getBoolean(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaEnable, true)
            alphaWidth = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaWidth, 0f).toInt()
            alphaHeight = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaHeight, 0f).toInt()
            val alphaGravityInt = t.getInt(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaGravity, 0)
            alphaGravity = if (alphaGravityInt == 0) {
                Gravity.CENTER
            } else {
                Gravity.from(alphaGravityInt)
            }
            alphaOffsetX = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaOffsetX, 0f).toInt()
            alphaOffsetY = t.getDimension(R.styleable.DaemonCpColorPickerView_daemon_cp_alphaOffsetY, 0f).toInt()
        } finally {
            t.recycle()
        }

        addViewInternal(paletteView)
        addViewInternal(brightnessView)
        addViewInternal(alphaView)

        clipChildren = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureChild(
                paletteView,
                MeasureSpec.makeMeasureSpec(paletteRadius * 2, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(paletteRadius * 2, MeasureSpec.EXACTLY)
        )

        if (brightnessEnable) {
            measureChild(
                    brightnessView,
                    MeasureSpec.makeMeasureSpec(brightnessWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(brightnessHeight, MeasureSpec.EXACTLY)
            )
        }

        if (alphaEnable) {
            measureChild(
                    alphaView,
                    MeasureSpec.makeMeasureSpec(alphaWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(alphaHeight, MeasureSpec.EXACTLY)
            )
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        layout(
                paletteView,
                paletteGravity,
                paletteOffsetX,
                paletteOffsetY
        )

        if (brightnessEnable) {
            layout(
                    brightnessView,
                    brightnessGravity,
                    brightnessOffsetX,
                    brightnessOffsetY
            )
        }

        if (alphaEnable) {
            layout(
                    alphaView,
                    alphaGravity,
                    alphaOffsetX,
                    alphaOffsetY
            )
        }
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

        savedState.color = colorCenter.getColor()

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

    /**
     * set current picked color
     *
     * 设置选中颜色值
     *
     * @param color 颜色值
     */
    fun setColor(color: Int) {
        colorCenter.setColor(color, true)
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

    /**
     * set custom palette painter
     *
     * 设置自定义调色板绘制器
     *
     * @param painter custom palette painter
     *                       调色板绘制器
     */
    fun setPalettePainter(painter: IPalettePainter?) = apply { paletteView.painter = painter }

    /**
     * get custom palette painter
     */
    fun getPalettePainter() = paletteView.painter

    /**
     * set custom brightness painter
     */
    fun setBrightnessPainter(painter: IBrightnessPainter?) = apply { brightnessView.painter = painter }

    /**
     * get custom brightness painter
     */
    fun getBrightnessPainter() = brightnessView.painter

    /**
     * set custom alpha painter
     */
    fun setAlphaPainter(painter: IAlphaPainter?) = apply { alphaView.painter = painter }

    /**
     * get custom alpha painter
     */
    fun getAlphaPainter() = alphaView.painter

    override fun subscribe(observer: ColorObserver) {
        colorCenter.subscribe(observer)
    }

    override fun unsubscribe(observer: ColorObserver) {
        colorCenter.unsubscribe(observer)
    }

    override fun getColor() = colorCenter.getColor()

    private fun layout(view: View, viewGravity: Gravity, offsetX: Int, offsetY: Int) {
        val gravity = Gravity.calibrate(viewGravity)
        val w = view.measuredWidth
        val h = view.measuredHeight

        val left = when (gravity) {
            Gravity.LEFT_TOP, Gravity.LEFT_CENTER, Gravity.LEFT_BOTTOM -> 0
            Gravity.RIGHT_TOP, Gravity.RIGHT_CENTER, Gravity.RIGHT_BOTTOM -> measuredWidth - w
            else -> (measuredWidth - w) / 2
        } + offsetX

        val top = when (gravity) {
            Gravity.LEFT_TOP, Gravity.CENTER_TOP, Gravity.RIGHT_TOP -> 0
            Gravity.LEFT_BOTTOM, Gravity.CENTER_BOTTOM, Gravity.RIGHT_BOTTOM -> measuredHeight - h
            else -> (measuredHeight - h) / 2
        } + offsetY

        view.layout(
                left,
                top,
                left + w,
                top + h
        )
    }
}