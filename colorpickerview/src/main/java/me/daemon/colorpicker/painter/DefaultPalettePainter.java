package me.daemon.colorpicker.painter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;

import me.daemon.colorpicker.ColorPickerView;

/**
 * @author daemon
 * @since 2019-02-11 20:29
 */
public class DefaultPalettePainter implements PalettePainter {

    private final Paint huePaint;
    private final Paint saturationPaint;

    public DefaultPalettePainter() {
        huePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        saturationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void onSizeChanged(int w, int h, int radius, int centerX, int centerY) {
        final Shader hueShader = new SweepGradient(centerX, centerY,
                new int[]{Color.RED, Color.MAGENTA, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED},
                null);
        huePaint.setShader(hueShader);

        final Shader saturationShader = new RadialGradient(centerX, centerY, radius,
                Color.WHITE, 0x00FFFFFF, Shader.TileMode.CLAMP);
        saturationPaint.setShader(saturationShader);

    }

    @Override
    public void drawPalette(ColorPickerView colorPickerView, Canvas canvas, int radius, int centerX, int centerY) {
        canvas.drawCircle(centerX, centerY, radius, huePaint);
        canvas.drawCircle(centerX, centerY, radius, saturationPaint);
    }
}
