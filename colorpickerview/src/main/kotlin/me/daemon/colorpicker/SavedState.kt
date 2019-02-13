package me.daemon.colorpicker

import android.os.Parcel
import android.os.Parcelable
import android.view.View

/**
 * @author daemon
 * @since 2019-02-13 16:11
 */
class SavedState : View.BaseSavedState {

    var color: Int = 0

    constructor(source: Parcel) : super(source) {
        this.color = source.readInt()
    }

    constructor(superState: Parcelable?) : super(superState)

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedState> {
        override fun createFromParcel(parcel: Parcel): SavedState {
            return SavedState(parcel)
        }

        override fun newArray(size: Int): Array<SavedState?> {
            return arrayOfNulls(size)
        }
    }
}