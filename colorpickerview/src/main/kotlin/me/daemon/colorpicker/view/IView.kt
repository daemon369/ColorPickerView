package me.daemon.colorpicker.view

/**
 * @author daemon
 * @since 2019-03-11 16:08
 */
interface IView<VALUE : IView.Value> {

    abstract class Value {

        var set = false
            internal set

        fun <T : Value> reset(): T {
            this.set = false
            return this as T
        }

    }

    fun getColor(): Int

}