package me.daemon.colorpicker

/**
 * @author daemon
 * @since 2019-01-27 22:20
 */
interface ColorObservable {

    fun subscribe(observer: ColorObserver)

    fun unsubscribe(observer: ColorObserver)

    fun getColor(): Int

}
