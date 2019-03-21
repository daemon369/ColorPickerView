//package me.daemon.colorpicker.demo;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PointF;
//
//import me.daemon.colorpicker.ColorPickerView;
//import me.daemon.colorpicker.painter.IndicatorPainter;
//
///**
// * @author daemon
// * @since 2019-01-27 23:27
// */
//public class ShowPopIndicator implements IndicatorPainter {
//
//    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//
//    private final float innerSize1 = 33;
//    private final float outerSize1 = 45;
//
//    private final float innerSize2 = 46.5f;
//    private final float outerSize2 = 63;
//
//    @Override
//    public void drawIndicator(
//            final ColorPickerView colorPickerView,
//            final Canvas canvas,
//            final PointF point,
//            final int color,
//            boolean isChanging) {
//
//        paint.setColor(Color.WHITE);
//
//        canvas.drawCircle(point.x, point.y, outerSize1, paint);
//
//        if (colorPickerView.isEnabled()) {
//            if (isChanging) {
//                canvas.drawCircle(point.x, point.y - 123, outerSize2, paint);
//            }
//
//            paint.setColor(color);
//
//            canvas.drawCircle(point.x, point.y, innerSize1, paint);
//
//            if (isChanging) {
//                canvas.drawCircle(point.x, point.y - 123, innerSize2, paint);
//            }
//        } else {
//
//            paint.setColor(Color.GRAY);
//
//            canvas.drawCircle(point.x, point.y, innerSize1, paint);
//        }
//    }
//}
