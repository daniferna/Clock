package es.uniovi.uo257977.clock.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.logic.Alarm;
import es.uniovi.uo257977.clock.logic.AlarmRecyclerAdapter;
import es.uniovi.uo257977.clock.logic.ListAlarms;
import es.uniovi.uo257977.clock.R;

import static android.content.Context.MODE_PRIVATE;

public class AlarmsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AlarmRecyclerAdapter alarmRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler_alarms);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        alarmRecyclerAdapter = new AlarmRecyclerAdapter();
        recyclerView.setAdapter(alarmRecyclerAdapter);

        super.onViewCreated(view, savedInstanceState);

        cargarAlarmas();
        prepararItemTouchHelper();

      //  pasarAlarmas(Arrays.asList(new Alarm(),new Alarm()));
    }

    private void prepararItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity().getApplicationContext(), "AlarmAppPreferences", MODE_PRIVATE);
                ListAlarms alarmas = complexPreferences.getObject("Alarmas", ListAlarms.class);
                alarmas.alarmas.remove(swipedPosition);
                alarmRecyclerAdapter.removeAlarm(swipedPosition);
                alarmRecyclerAdapter.notifyItemRangeChanged(swipedPosition, alarmas.alarmas.size());
                complexPreferences.putObject("Alarmas", alarmas);
                complexPreferences.commit();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void pasarAlarmas(List<Alarm> alarmas){
        if (alarmas.size()>0){
            ((AlarmRecyclerAdapter) recyclerView.getAdapter()).updateAlarms(alarmas);
        }
    }


    private void cargarAlarmas() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity().getApplicationContext(), "AlarmAppPreferences", MODE_PRIVATE);
        ListAlarms alarmas = complexPreferences.getObject("Alarmas", ListAlarms.class);
        if(alarmas!=null)
             pasarAlarmas((alarmas).alarmas);
    }

}
