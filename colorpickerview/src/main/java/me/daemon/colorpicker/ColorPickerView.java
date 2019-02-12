package me.daemon.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

import me.daemon.colorpicker.painter.DefaultIndicatorPainter;
import me.daemon.colorpicker.painter.DefaultPalettePainter;
import me.daemon.colorpicker.painter.IndicatorPainter;
import me.daemon.colorpicker.painter.PalettePainter;

/**
 * color picker view
 * 颜色选择器View
 *
 * @author daemon
 * @since 2019-01-27 18:04
 */
public class ColorPickerView extends View implements ColorObservable {

    /**
     * initial color
     * 初始颜色值
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    private int initialColor;

    /**
     * padding of palette
     * 调色板留白
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    private int palettePadding;

    /**
     * whether automatic measure this ColorPickerView as square,
     * if true then minimum size of width and height will used as
     * measured width and height
     */
    @ViewDebug.ExportedProperty(category = "daemon")
    private boolean square = false;

    private final PointF currentPoint;

    private int paletteCenterX;
    private int paletteCenterY;

    private final BrightnessProvider defaultBrightnessProvider = new BrightnessProvider() {
        @Override
        public float getBrightness() {
            return 1.0f;
        }
    };

    private BrightnessProvider brightnessProvider;

    private final PalettePainter defaultPalettePainter = new DefaultPalettePainter();

    private PalettePainter palettePainter = null;

    private final IndicatorPainter defaultIndicatorPainter = new DefaultIndicatorPainter();

    private IndicatorPainter indicatorPainter = null;

    private final List<ColorObserver> observers = new ArrayList<>();

    private int color;

    private boolean isChanging = false;

    private boolean disallowInterceptTouchEven = false;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        currentPoint = new PointF();

        final TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);

        try {
            final int palettePadding = (int) t.getDimension(R.styleable.ColorPickerView_palettePadding, 0);
            setPalettePadding(palettePadding);

            final int initialColor = t.getColor(R.styleable.ColorPickerView_initialColor, Color.BLACK);
            setInitialColor(initialColor);
            setColorInternal(initialColor, false);

            final boolean disallowInterceptTouchEven = t.getBoolean(
                    R.styleable.ColorPickerView_disallowInterceptTouchEvent,
                    false);
            setDisallowInterceptTouchEven(disallowInterceptTouchEven);
        } finally {
            t.recycle();
        }

    }

    /**
     * set padding of palette
     * 设置调色板留白
     *
     * @param palettePadding 调色板留白
     */
    public void setPalettePadding(final int palettePadding) {
        if (this.palettePadding != palettePadding) {
            this.palettePadding = palettePadding;

            invalidate();
        }
    }

    public int getPalettePadding() {
        return palettePadding;
    }

    /**
     * set initial color
     * 设置初始颜色
     *
     * @param initialColor 初始颜色
     */
    public void setInitialColor(final int initialColor) {
        if (this.initialColor != initialColor) {
            this.initialColor = initialColor;

            invalidate();
        }
    }

    public int getInitialColor() {
        return initialColor;
    }

    /**
     * set square mode
     * <p>
     * 设置正方形模式
     *
     * @param square whether use square mode
     */
    public void setSquare(final boolean square) {
        if (this.square != square) {
            this.square = square;

            invalidate();
        }
    }

    public boolean getSquare() {
        return square;
    }

    /**
     * set current picked color
     * 设置选中颜色值
     *
     * @param color 颜色值
     */
    public void setColor(final int color) {
        setColorInternal(color, true);
    }

    /**
     * resolve touch conflict with it's parent and ancestors<br/><br/>
     * <p>
     * if disallow is set to be true, {@link ColorPickerView}  will call
     * it's {@link ViewParent#getParent() parent}'s
     * {@link ViewParent#requestDisallowInterceptTouchEvent(boolean)}
     * when received {@link MotionEvent#ACTION_DOWN} event in
     * {@link #onTouchEvent(MotionEvent)}
     * to disallow it's parent and ancestors to intercept touch event,
     * and restore when received {@link MotionEvent#ACTION_UP} event
     * <br/><br/>
     * 解决{@link ColorPickerView}与其父View或祖先View的触摸事件冲突<br/><br/>
     * <p>
     * disallow设置为true时，在{@link #onTouchEvent(MotionEvent)}
     * 方法中接收到{@link MotionEvent#ACTION_DOWN} 触摸事件时会调用
     * {@link ViewParent#getParent() 父View}的
     * {@link ViewParent#requestDisallowInterceptTouchEvent(boolean)}方法来紧张父View
     * 及祖先View拦截触摸事件，并在收到{@link MotionEvent#ACTION_UP}触摸事件时恢复
     *
     * @param disallow whether to disallow it's parent and ancestors to intercept touch event<br/>
     *                 是否禁止父View或祖先View拦截触摸事件冲突
     */
    public void setDisallowInterceptTouchEven(final boolean disallow) {
        this.disallowInterceptTouchEven = disallow;
    }

    private void setColorInternal(final int color, final boolean notify) {
        this.color = color;

        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float r = hsv[1] * getRadius();
        float radian = (float) (hsv[0] / 180f * Math.PI);
        updateIndicator((float) (r * Math.cos(radian) + paletteCenterX), (float) (-r * Math.sin(radian) + paletteCenterY));

        if (notify) {
            notifyObservers(color);
        }

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!square) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        final int size = Math.min(width, height);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        );
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        paletteCenterX = w / 2;
        paletteCenterY = h / 2;

        getPalettePainter().onSizeChanged(w, h, getRadius(), paletteCenterX, paletteCenterY);

        setColorInternal(color, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int radius = getRadius();

        getPalettePainter().drawPalette(
                this,
                canvas,
                radius, paletteCenterX,
                paletteCenterY
        );

        drawIndicator(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return super.onTouchEvent(event);

        final boolean clickable = isClickable()
                || isLongClickable()
                || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isContextClickable();
        if (!clickable) return super.onTouchEvent(event);

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (disallowInterceptTouchEven) {
                    // resolve touch conflicts
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                isChanging = true;
                update(x, y);
                return true;

            case MotionEvent.ACTION_MOVE:
                update(x, y);
                break;

            case MotionEvent.ACTION_UP:
                isChanging = false;
                update(x, y);

                performClick();

                if (disallowInterceptTouchEven) {
                    // resolve touch conflicts
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private static class SavedState extends View.BaseSavedState {

        private int color;

        public SavedState(Parcel source) {
            super(source);
            color = source.readInt();
        }

        @TargetApi(Build.VERSION_CODES.N)
        public SavedState(Parcel source, ClassLoader loader) {
            super(source, loader);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(color);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.ClassLoaderCreator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState createFromParcel(Parcel source, ClassLoader loader) {
                return new SavedState(source, loader);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());

        savedState.color = color;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            final SavedState savedState = (SavedState) state;
            super.onRestoreInstanceState(savedState.getSuperState());

            setColor(savedState.color);

        } else {
            super.onRestoreInstanceState(state);
        }
    }

    protected void drawIndicator(final Canvas canvas) {
        final IndicatorPainter provider = indicatorPainter != null ? indicatorPainter : defaultIndicatorPainter;
        provider.drawIndicator(this, canvas, currentPoint, color, isChanging);
    }

    private int getRadius() {
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - palettePadding * 2;
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - palettePadding * 2;
        final int limit = Math.max(Math.min(width, height), 0);

        return limit / 2;
    }

    private void update(final float eventX, final float eventY) {
        final int color = getColorAtPoint(eventX, eventY);
        setColorInternal(color, true);
    }

    private void updateIndicator(final float eventX, final float eventY) {
        float x = eventX - paletteCenterX;
        float y = eventY - paletteCenterY;
        double r = Math.sqrt(x * x + y * y);

        final int radius = getRadius();
        if (r > radius) {
            x *= radius / r;
            y *= radius / r;
        }
        currentPoint.x = x + paletteCenterX;
        currentPoint.y = y + paletteCenterY;

        invalidate();

    }

    /**
     * set custom palette painter, using {@link #defaultPalettePainter} as default
     * palette painter
     * <p>
     * 设置自定义调色板绘制器，默认使用{@link #defaultPalettePainter}
     *
     * @param palettePainter custom palette painter
     *                       调色板绘制器
     */
    public void setPalettePainter(final PalettePainter palettePainter) {
        this.palettePainter = palettePainter;
    }

    /**
     * set custom indicator painter, using {@link #defaultIndicatorPainter}
     * if custom indicator not been set
     * <p>
     * 设置自定义指示器绘制器，如果没有设置则使用{@link #defaultIndicatorPainter 默认绘制器}
     *
     * @param indicatorPainter custom indicator painter
     */
    public void setIndicatorPainter(final IndicatorPainter indicatorPainter) {
        this.indicatorPainter = indicatorPainter;
    }

    /**
     * set custom brightness provider, {@link #defaultBrightnessProvider} is
     * used if custom brightness provider is not been set
     * <p>
     * 设置自定义透明度提供器，为空则使用{@link #defaultBrightnessProvider 默认透明度提供器}
     *
     * @param brightnessProvider custom brightness provider
     */
    public void setBrightnessProvider(final BrightnessProvider brightnessProvider) {
        this.brightnessProvider = brightnessProvider;
    }

    @Override
    public void subscribe(ColorObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    @Override
    public void unsubscribe(ColorObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    @Override
    public int getColor() {
        return color;
    }

    private int getColorAtPoint(float eventX, float eventY) {
        float x = eventX - paletteCenterX;
        float y = eventY - paletteCenterY;
        double r = Math.sqrt(x * x + y * y);
        float[] hsv = {0, 0, 0};

        final BrightnessProvider bp = brightnessProvider != null ?
                brightnessProvider : defaultBrightnessProvider;
        final float brightness = bp.getBrightness();

        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / getRadius())));
        hsv[2] = Math.max(0f, Math.min(1f, brightness));
        return Color.HSVToColor(hsv);
    }

    private void notifyObservers(final int color) {
        for (ColorObserver observer : observers) {
            observer.onColor(color);
        }
    }

    private PalettePainter getPalettePainter() {
        return palettePainter != null ?
                palettePainter : defaultPalettePainter;
    }
}
