package es.uniovi.uo257977.clock.logic;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Alarm implements Parcelable {

    private int ID;
    private Date fecha_alarma;
    private boolean vibrar;
    private String nombre;
    private String sonido;
    private Boolean spotify = false;
    private Boolean activada = true;
    private DIAS_ALARMA[] diasAlarma;
    private List<Intent> intentsAlarma = new ArrayList<>();
    public boolean isActivada() {
        return activada;
    }

    public List<Intent> getIntents(){ return intentsAlarma;}

    public void setIntents(List<Intent> intents) {this.intentsAlarma= intents;}

    public String getSonido(){return this.sonido;}

    public void setSonido(String s) {
        sonido=s;
    }

    public enum DIAS_ALARMA implements Parcelable {LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ordinal());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DIAS_ALARMA> CREATOR = new Creator<DIAS_ALARMA>() {
            @Override
            public DIAS_ALARMA createFromParcel(Parcel in) {
                return  DIAS_ALARMA.values()[in.readInt()];
            }

            @Override
            public DIAS_ALARMA[] newArray(int size) {
                return new DIAS_ALARMA[size];
            }
        };
    }

    public Alarm(Date fecha_alarma, boolean vibrar, String nombre, DIAS_ALARMA[] diasAlarma, Uri ringtone) {
        this.fecha_alarma = fecha_alarma;
        this.vibrar = vibrar;
        this.nombre = nombre;
        this.diasAlarma = diasAlarma;
        this.sonido = ringtone.toString();
        ID = ThreadLocalRandom.current().nextInt();
    }

    public Alarm() {
        this.fecha_alarma = new Date();
        this.nombre = "Alarma en blanco";
        this.diasAlarma = new DIAS_ALARMA[]{DIAS_ALARMA.LUNES, DIAS_ALARMA.MARTES, DIAS_ALARMA.MIERCOLES, DIAS_ALARMA.JUEVES, DIAS_ALARMA.VIERNES};
        this.sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    }

    protected Alarm(Parcel in) {
        ID = in.readInt();
        fecha_alarma= new Date(in.readLong());
        vibrar = in.readByte() != 0;
        nombre = in.readString();
        sonido = in.readString();
        diasAlarma = in.createTypedArray(DIAS_ALARMA.CREATOR);
        activada=in.readByte()!=0;
        spotify=in.readByte()!=0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeLong(fecha_alarma.getTime());
        dest.writeByte((byte) (vibrar ? 1 : 0));
        dest.writeString(nombre);
        dest.writeString(sonido);
        dest.writeTypedArray(diasAlarma, flags);
        dest.writeByte((byte) (activada ? 1 : 0));
        dest.writeByte((byte) (spotify ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public Date getFecha_alarma() {
        return fecha_alarma;
    }

    public void setFecha_alarma(Date fecha_alarma) {
        this.fecha_alarma = fecha_alarma;
    }

    public boolean isVibrar() {
        return vibrar;
    }

    public void setVibrar(boolean vibrar) {
        this.vibrar = vibrar;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public DIAS_ALARMA[] getDiasAlarma() {
        return diasAlarma;
    }

    public void setDiasAlarma(DIAS_ALARMA[] diasAlarma) {
        this.diasAlarma = diasAlarma;
    }

    public int getID() {
        return ID;
    }

    public Boolean getSpotify() {
        return spotify;
    }

    public void setSpotify(Boolean spotify) {
        this.spotify = spotify;
    }

    public void cambiarEstado(){
        activada = !activada;
    }

    public void cambiarVibracion(){
        vibrar = !vibrar;
    }
}
