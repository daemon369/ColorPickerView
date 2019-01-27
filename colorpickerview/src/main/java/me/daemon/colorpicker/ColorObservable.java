package me.daemon.colorpicker;

/**
 * @author daemon
 * @since 2019-01-27 22:20
 */
public interface ColorObservable {

    void subscribe(ColorObserver observer);

    void unsubscribe(ColorObserver observer);

    int getColor();

}
