package es.uniovi.uo257977.clock.Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListAlarms implements Parcelable {

    public List<Alarm> alarmas;

    protected ListAlarms(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListAlarms> CREATOR = new Creator<ListAlarms>() {
        @Override
        public ListAlarms createFromParcel(Parcel in) {
            return new ListAlarms(in);
        }

        @Override
        public ListAlarms[] newArray(int size) {
            return new ListAlarms[size];
        }
    };

    public List<Alarm> getAlarms() {
        return alarmas;
    }
    public void setAlarms(List<Alarm> alarmas) {
        this.alarmas = alarmas;
    }
}
