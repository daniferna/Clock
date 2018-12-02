package es.uniovi.uo257977.clock.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.uniovi.uo257977.clock.Logic.Alarm;
import es.uniovi.uo257977.clock.Logic.AlarmRecyclerAdapter;
import es.uniovi.uo257977.clock.R;

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

        ((AlarmRecyclerAdapter) recyclerView.getAdapter()).updateAlarms(Arrays.asList(new Alarm(), new Alarm(), new Alarm())); // Creacion de alarmas de prueba

        super.onViewCreated(view, savedInstanceState);
    }
}
