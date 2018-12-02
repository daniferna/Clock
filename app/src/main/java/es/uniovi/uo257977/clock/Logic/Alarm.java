package es.uniovi.uo257977.clock.Logic;

import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class Alarm {

    private int ID;
    private Date fecha_alarma;
    private boolean vibrar;
    private String nombre;
    private Uri sonido;
    private DIAS_ALARMA[] diasAlarma;

    public enum DIAS_ALARMA {LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO}

    public Alarm(Date fecha_alarma, boolean vibrar, String nombre, DIAS_ALARMA[] diasAlarma, Uri ringtone) {
        this.fecha_alarma = fecha_alarma;
        this.vibrar = vibrar;
        this.nombre = nombre;
        this.diasAlarma = diasAlarma;
        this.sonido = ringtone;
        ID = ThreadLocalRandom.current().nextInt();
    }

    public Alarm() {
        this.fecha_alarma = new Date();
        this.nombre = "Alarma en blanco";
        this.diasAlarma = new DIAS_ALARMA[]{DIAS_ALARMA.LUNES, DIAS_ALARMA.MARTES, DIAS_ALARMA.MIERCOLES, DIAS_ALARMA.JUEVES, DIAS_ALARMA.VIERNES};
        this.sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

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
}
