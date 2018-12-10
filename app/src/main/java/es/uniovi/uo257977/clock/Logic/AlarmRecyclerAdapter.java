package es.uniovi.uo257977.clock.Logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.R;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_alarms, parent, false),parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Alarm alarm = alarms.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (alarm.isActivada()){
            holder.switchAlarm.setChecked(true);
        }
        holder.hourAlarmTxt.setText(formatter.format(alarm.getFecha_alarma().getTime()));
        holder.txtNombreAlarma.setText(alarm.getNombre());


      //  holder.vibrar.setEnabled(alarm.isVibrar());

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
            holder.trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarms.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, alarms.size());
                    actualizarPreferences(position, holder);

                }
            });

        holder.switchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarms.get(position).cambiarEstado();
                actualizarPreferences(position, holder);
            }
        });


    }

    private void actualizarPreferences(int position, @NonNull ViewHolder holder) {

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(holder.context , "MyPreferences", MODE_PRIVATE);
        ListAlarms alarmasList = complexPreferences.getObject("Alarmas", ListAlarms.class);
        alarmasList.setAlarms(alarms);
        complexPreferences.putObject("Alarmas", alarmasList);
        complexPreferences.commit();
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
        RadioButton vibrar;

        public ViewHolder(@NonNull View itemView,Context context) {
            super(itemView);
            chipGroup = itemView.findViewById(R.id.chipGroup);
            switchAlarm = itemView.findViewById(R.id.switch_alarm);
            hourAlarmTxt = itemView.findViewById(R.id.hour_alarm_txt);
            txtNombreAlarma = itemView.findViewById(R.id.txtNombreAlarma);
            trashButton = itemView.findViewById(R.id.trash_btn_alarm);
            vibrar= itemView.findViewById(R.id.radioButton);
            this.context=context;
        }
    }



}
