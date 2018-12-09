package es.uniovi.uo257977.clock.Logic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ListAlarms  {

    public ArrayList<Alarm> alarmas;

    public ArrayList<Alarm> getAlarms() {
        return alarmas;
    }
    public void setAlarms(ArrayList<Alarm> alarmas) {
        this.alarmas = alarmas;
    }
}
