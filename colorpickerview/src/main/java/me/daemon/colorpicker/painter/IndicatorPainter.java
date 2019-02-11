package me.daemon.colorpicker.painter;

import android.graphics.Canvas;
import android.graphics.PointF;

import me.daemon.colorpicker.ColorPickerView;

/**
 * indicator painter, draw color picker indicator
 * 颜色选择指示器绘制
 *
 * @author daemon
 * @since 2019-02-09 22:30
 */
public interface IndicatorPainter {

    /**
     * draw indicator
     * 绘制指示器
     *
     * @param colorPickerView color picker view
     *                        颜色选择View
     * @param canvas          canvas for indicator
     *                        画布
     * @param point           current position of indicator
     *                        指示器当前位置
     * @param color           current color
     *                        当前颜色
     * @param isChanging      whether color is changing, true when finger down and false when finger up
     *                        颜色是否在变化中，手指按下到抬起之前的过程属于变化中
     */
    void drawIndicator(
            final ColorPickerView colorPickerView,
            final Canvas canvas,
            final PointF point,
            final int color,
            final boolean isChanging
    );
}
