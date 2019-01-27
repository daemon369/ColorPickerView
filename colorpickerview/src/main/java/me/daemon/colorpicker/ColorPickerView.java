package me.daemon.colorpicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daemon
 * @since 2019-01-27 18:04
 */
public class ColorPickerView extends View implements ColorObservable {

    @ViewDebug.ExportedProperty(category = "daemon")
    private int indicatorRadius;

    @ViewDebug.ExportedProperty(category = "daemon")
    private int initialColor;

    @ViewDebug.ExportedProperty(category = "daemon")
    private int paletteRadius;

    private final Paint huePaint;
    private final Paint saturationPaint;
    private final Paint indicatorPaint;

    private final PointF currentPoint;

    private int paletteCenterX;
    private int paletteCenterY;

    private final IndicatorPainter defaultIndicatorPainter = new DefaultIndicatorPainter();

    private IndicatorPainter indicatorPainter = null;

    private final List<ColorObserver> observers = new ArrayList<>();

    private int color;

    private boolean isChanging = false;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        huePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        currentPoint = new PointF();

        final TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ColorPickerView);

        try {
            final int paletteRadius = (int) t.getDimension(R.styleable.ColorPickerView_paletteRadius, 0);
            setPaletteRadius(paletteRadius);

            final int indicatorRadius = (int) t.getDimension(R.styleable.ColorPickerView_indicatorRadius, 0);
            setIndicatorRadius(indicatorRadius);

            final int initialColor = t.getColor(R.styleable.ColorPickerView_initialColor, Color.BLACK);
            setInitialColor(initialColor);
            setColor(initialColor);
        } finally {
            t.recycle();
        }

    }

    public void setPaletteRadius(final int paletteRadius) {
        if (this.paletteRadius != paletteRadius) {
            this.paletteRadius = paletteRadius;

            invalidate();
        }
    }

    public void setIndicatorRadius(final int indicatorRadius) {
        if (this.indicatorRadius != indicatorRadius) {
            this.indicatorRadius = indicatorRadius;

            invalidate();
        }
    }

    public void setInitialColor(final int initialColor) {
        if (this.initialColor != initialColor) {
            this.initialColor = initialColor;

            invalidate();
        }
    }

    public void setColor(final int color) {
        if (this.color != color) {
            this.color = color;

            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            float r = hsv[1] * getRadius();
            float radian = (float) (hsv[0] / 180f * Math.PI);
            updateIndicator((float) (r * Math.cos(radian) + paletteCenterX), (float) (-r * Math.sin(radian) + paletteCenterY));

            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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

        final int radius = getRadius();

        final Shader hueShader = new SweepGradient(paletteCenterX, paletteCenterY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED},
                null);
        huePaint.setShader(hueShader);

        final Shader saturationShader = new RadialGradient(paletteCenterX, paletteCenterY, radius,
                Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP);
        saturationPaint.setShader(saturationShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int radius = getRadius();

        canvas.drawCircle(paletteCenterX, paletteCenterY, radius, huePaint);
        canvas.drawCircle(paletteCenterX, paletteCenterY, radius, saturationPaint);

        drawIndicator(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isChanging = true;
                updateIndicator(event.getX(), event.getY());
                return true;

            case MotionEvent.ACTION_UP:
                isChanging = false;
                updateIndicator(event.getX(), event.getY());
                return true;
        }
        return super.onTouchEvent(event);
    }

    protected void drawIndicator(@NonNull final Canvas canvas) {
        final IndicatorPainter provider = indicatorPainter != null ? indicatorPainter : defaultIndicatorPainter;
        provider.drawIndicator(canvas, indicatorPaint, currentPoint, indicatorRadius, isChanging);
    }

    private int getRadius() {
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - indicatorRadius * 2;
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - indicatorRadius * 2;
        final int limit = Math.max(Math.min(width, height), 0);

        final int radius;
        if (paletteRadius == 0) {
            radius = limit / 2;
        } else {
            radius = Math.max(Math.min(limit / 2, paletteRadius - indicatorRadius), 0);
        }

        return radius;
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

        notifyObservers(getColorAtPoint(eventX, eventY));
    }

    public interface IndicatorPainter {

        void drawIndicator(
                @NonNull final Canvas canvas,
                @NonNull final Paint indicatorPaint,
                @NonNull final PointF point,
                final int indicatorRadius,
                final boolean isChanging
        );
    }

    public static class DefaultIndicatorPainter implements IndicatorPainter {

        @Override
        public void drawIndicator(
                @NonNull Canvas canvas,
                @NonNull Paint indicatorPaint,
                @NonNull PointF point,
                int indicatorRadius,
                final boolean isChanging) {

            indicatorPaint.setColor(Color.BLACK);
            indicatorPaint.setStrokeWidth(2);
            canvas.drawLine(
                    point.x - indicatorRadius,
                    point.y,
                    point.x + indicatorRadius,
                    point.y,
                    indicatorPaint);
            canvas.drawLine(
                    point.x,
                    point.y - indicatorRadius,
                    point.x,
                    point.y + indicatorRadius,
                    indicatorPaint);
        }
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
        float[] hsv = {0, 0, 1};
        hsv[0] = (float) (Math.atan2(y, -x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (r / getRadius())));
        return Color.HSVToColor(hsv);
    }

    private void notifyObservers(final int color) {
        for (ColorObserver observer : observers) {
            observer.onColor(color);
        }
    }
}
