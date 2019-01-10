package es.uniovi.uo257977.clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.db.chart.model.BarSet;
import com.db.chart.model.ChartEntry;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.BarChartView;

import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.logic.ChartAnimations.Easing.BounceEase;

public class ScoreboardActivity extends AppCompatActivity {

    private BarChartView scoreboardChart;
    private ProgressBar progressBar;
    private TextView txtMejorDiaResultado;
    private TextView txtPorcentajeCalidad;
    final static long TIEMPO=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboardChart = findViewById(R.id.scoreboard_chart);
        progressBar = findViewById(R.id.pBCalidadSueño);
        txtMejorDiaResultado = findViewById(R.id.txtMejorDiaResultado);
        txtPorcentajeCalidad = findViewById(R.id.txtCalidadNum);


        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getApplicationContext(), "AlarmAppPreferences", MODE_PRIVATE);
        BarSet barSet = complexPreferences.getObject("Puntuacion", BarSet.class);

         Long lunes= complexPreferences.getObject("Lunes",long.class);
         if (lunes == null)
             lunes =0l;
         lunes = TIEMPO-lunes;
         if (lunes <0 || lunes==TIEMPO)
            lunes = 0l;
         Long martes =complexPreferences.getObject("Martes",long.class);
        if (martes == null)
            martes =0l;
        martes = TIEMPO-martes;
        if (martes <0 || martes==TIEMPO)
            martes = 0l;
        Long  miercoles= complexPreferences.getObject("Miercoles",long.class);
        if (miercoles == null)
            miercoles =0l;
        miercoles = TIEMPO-miercoles;
        if (miercoles <0 || miercoles==TIEMPO)
            miercoles = 0l;
        Long  jueves= complexPreferences.getObject("Jueves",long.class);
        if (jueves == null)
            jueves =0l;
        jueves = TIEMPO-jueves;
        if (jueves <0 || jueves==TIEMPO)
            jueves = 0l;
        Long      viernes=complexPreferences.getObject("Viernes",long.class);
        if (viernes == null)
            viernes =0l;
        viernes = TIEMPO-viernes;
        if (viernes <0 || viernes==TIEMPO)
            viernes = 0l;
        Long     sabado= complexPreferences.getObject("Sabado",long.class);
        if (sabado == null)
            sabado =0l;
        sabado = TIEMPO-sabado;
        if (sabado <0 || sabado==TIEMPO)
            sabado = 0l;
        Long   domingo= complexPreferences.getObject("Domingo",long.class);
        if (domingo == null)
            domingo =0l;
        domingo = TIEMPO-domingo;
        if (domingo <0 || domingo==TIEMPO)
            domingo = 0l;

        // GRAFICA DE PRUEBA
        barSet.addBar("Lunes", lunes);
        barSet.addBar("Martes", martes);
        barSet.addBar("Miércoles", miercoles);
        barSet.addBar("Jueves",jueves);
        barSet.addBar("Viernes",viernes);
        barSet.addBar("Sábado", sabado);
        barSet.addBar("Domingo",domingo);

        float sum = 0;
        for(ChartEntry ce : barSet.getEntries())
            sum += ce.getValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            progressBar.setProgress((int) sum/7, true);
        else
            progressBar.setProgress((int) sum/7);
        txtMejorDiaResultado.setText(Collections.max(barSet.getEntries()).getLabel());
        txtPorcentajeCalidad.setText(String.format("%s%%", progressBar.getProgress() / 10));

        scoreboardChart.setYAxis(false);
        scoreboardChart.setRoundCorners(10);
        scoreboardChart.setYLabels(AxisRenderer.LabelPosition.NONE);
        scoreboardChart.addData(barSet.setColor(ResourcesCompat.getColor(getResources(), R.color.accent, getTheme())));
        scoreboardChart.show(new BounceEase());
    }
}
