package es.uniovi.uo257977.clock.Logic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.MainActivity;
import es.uniovi.uo257977.clock.R;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class AlarmRecyclerAdapter extends Adapter<AlarmRecyclerAdapter.ViewHolder> {

    private ArrayList<Alarm> alarms = new ArrayList<Alarm>();


    public void updateAlarms(List<Alarm> newList) {
        final DiffUtilAlarm diffCallback = new DiffUtilAlarm(this.alarms, newList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        alarms.clear();
        Collections.reverse(newList);
        alarms.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void removeAlarm(int position){
        alarms.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_alarms, parent, false),parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        for (Alarm alarmTemp:alarms){
            AlarmIntentUtil.añadirAlarmManager(alarmTemp,holder.context);
        }
        AlarmIntentUtil.actualizarPreferences(holder.context,alarms);
        Alarm alarm = alarms.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        if (alarm.isActivada()){
            holder.switchAlarm.setChecked(true);
        }
        holder.hourAlarmTxt.setText(formatter.format(alarm.getFecha_alarma().getTime()));
        holder.txtNombreAlarma.setText(alarm.getNombre());


      //  holder.vibrar.setEnabled(alarm.isVibrar());

        checkChips(holder, alarm);

        for(Chip chip:holder.chips) {
                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Alarm.DIAS_ALARMA> diasAlarma = new ArrayList<>();
                        for (int i = 0; i < holder.chips.size(); i++) {
                            switch (i) {
                                case 0:
                                    if (holder.chips.get(0).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.LUNES);
                                    break;
                                case 1:
                                    if (holder.chips.get(1).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.MARTES);
                                    break;
                                case 2:
                                    if (holder.chips.get(2).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.MIERCOLES);
                                    break;
                                case 3:
                                    if (holder.chips.get(3).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.JUEVES);
                                    break;
                                case 4:
                                    if (holder.chips.get(4).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.VIERNES);
                                    break;
                                case 5:
                                    if (holder.chips.get(5).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.SABADO);
                                    break;
                                case 6:
                                    if (holder.chips.get(6).isChecked())
                                        diasAlarma.add(Alarm.DIAS_ALARMA.DOMINGO);
                                    break;
                            }
                        }
                            Alarm.DIAS_ALARMA[] dias =new Alarm.DIAS_ALARMA[diasAlarma.size()];
                            for(int j=0;j<diasAlarma.size();j++){
                                dias[j]=diasAlarma.get(j);
                            }
                            alarm.setDiasAlarma(dias);
                            if (alarm.isActivada()) {
                                Snackbar.make(v, "Alarma desactivada al cambiar de dia", Snackbar.LENGTH_SHORT).show();
                                alarm.cambiarEstado();
                                holder.switchAlarm.setChecked(false);
                                AlarmIntentUtil.actualizarPreferences(holder.context,alarms);
                            }
                        }
                });
            }


            holder.trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmIntentUtil.borrarIntents(alarm,holder.context);
                    alarms.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, alarms.size());
                    AlarmIntentUtil.actualizarPreferences(holder.context,alarms);

                }
            });

        holder.switchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarma = alarms.get(position);
                alarma.cambiarEstado();

                if (alarma.isActivada()) {
                    Snackbar.make(v, "Alarma activada", Snackbar.LENGTH_SHORT).show();
                    AlarmIntentUtil.añadirAlarmManager(alarma, holder.context);
                } else{
                    Snackbar.make(v, "Alarma desactivada", Snackbar.LENGTH_SHORT).show();
                    AlarmIntentUtil.borrarIntents(alarma, holder.context);
            }
                AlarmIntentUtil.actualizarPreferences(holder.context,alarms);

            }
        });

        holder.vibrar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ((isChecked)) {
                    Snackbar.make(buttonView, "Vibración activada", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(buttonView, "Vibración desactivada", Snackbar.LENGTH_SHORT).show();
                }
                alarms.get(position).setVibrar(isChecked);
            }
        });
    }

    private List<Intent> borrarIntents(Alarm alarma, ViewHolder holder) {
        List<Intent> intents = alarma.getIntents();
        for (Intent intent:intents) {
            PendingIntent yourIntent = PendingIntent.getBroadcast(holder.context, 0, intent, 0);
            holder.manager.cancel(yourIntent);
            yourIntent.cancel();
        }
        intents.clear();
        return intents;
    }

    private void checkChips(@NonNull ViewHolder holder, Alarm alarm) {
        for (Alarm.DIAS_ALARMA dia : alarm.getDiasAlarma())
            switch (dia) {
                case LUNES:
                    holder.chipGroup.check(R.id.chipLunes);
                    break;
                case MARTES:
                    holder.chipGroup.check(R.id.chipMartes);
                    break;
                case MIERCOLES:
                    holder.chipGroup.check(R.id.chipMiercoles);
                    break;
                case JUEVES:
                    holder.chipGroup.check(R.id.chipJueves);
                    break;
                case VIERNES:
                    holder.chipGroup.check(R.id.chipViernes);
                    break;
                case SABADO:
                    holder.chipGroup.check(R.id.chipSabado);
                    break;
                case DOMINGO:
                    holder.chipGroup.check(R.id.chipDomingo);
                    break;
            }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        ChipGroup chipGroup;
        Switch switchAlarm;
        TextView hourAlarmTxt;
        TextView txtNombreAlarma;
        Button trashButton;
        Context context;
        CheckBox vibrar;
        AlarmManager manager;
        List<Chip> chips;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            chipGroup = itemView.findViewById(R.id.chipGroup);
            switchAlarm = itemView.findViewById(R.id.switch_alarm);
            hourAlarmTxt = itemView.findViewById(R.id.hour_alarm_txt);
            txtNombreAlarma = itemView.findViewById(R.id.txtNombreAlarma);
            trashButton = itemView.findViewById(R.id.trash_btn_alarm);
            vibrar= itemView.findViewById(R.id.checkBoxVibrar);
            chips=cargarChips();

            this.context=context;
            manager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        }

        private List<Chip> cargarChips() {
            List<Chip> chips = new ArrayList<Chip>();
            Chip tempChip= itemView.findViewById(R.id.chipLunes);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipMartes);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipMiercoles);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipJueves);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipViernes);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipSabado);
            chips.add(tempChip);
            tempChip= itemView.findViewById(R.id.chipDomingo);
            chips.add(tempChip);

            return chips;
        }
    }

}
