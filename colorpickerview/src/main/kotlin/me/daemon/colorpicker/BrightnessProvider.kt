package me.daemon.colorpicker

/**
 * @author daemon
 * @since 2019-02-10 15:32
 */
interface BrightnessProvider {

    /**
     * get brightness
     * 获取明亮度
     *
     * @return brightness, limited to 0.0f~1.0f
     */
    val brightness: Float

}
