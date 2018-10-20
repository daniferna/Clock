package es.uniovi.uo257977.clock.Logic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import es.uniovi.uo257977.clock.R;

public class AlarmRecyclerAdapter extends Adapter<AlarmRecyclerAdapter.ViewHolder> {

    private ArrayList<Alarm> alarms;


    public AlarmRecyclerAdapter(ArrayList<Alarm> alarms) {
        this.alarms = alarms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_alarms, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");//formating according to my need
        holder.hourAlarmTxt.setText(formatter.format(alarm.getFecha_alarma().getTime()));
        holder.txtNombreAlarma.getEditText().setText(alarm.getNombre());
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
        TextInputLayout txtNombreAlarma;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chipGroup = itemView.findViewById(R.id.chipGroup);
            switchAlarm = itemView.findViewById(R.id.switch_alarm);
            hourAlarmTxt = itemView.findViewById(R.id.hour_alarm_txt);
            txtNombreAlarma = itemView.findViewById(R.id.txtNombreAlarma);
        }
    }

}
