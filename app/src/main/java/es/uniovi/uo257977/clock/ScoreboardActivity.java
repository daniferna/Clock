package es.uniovi.uo257977.clock;

import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.db.chart.model.BarSet;
import com.db.chart.model.ChartEntry;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.BarChartView;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import br.com.kots.mob.complex.preferences.ComplexPreferences;
import es.uniovi.uo257977.clock.Logic.ChartAnimations.Easing.BounceEase;

public class ScoreboardActivity extends AppCompatActivity {

    private BarChartView scoreboardChart;
    private ProgressBar progressBar;
    private TextView txtMejorDiaResultado;
    private TextView txtPorcentajeCalidad;

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

        // GRAFICA DE PRUEBA
        barSet.addBar("Lunes", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Martes", ThreadLocalRandom.current().nextInt(0, 500));
        barSet.addBar("Miércoles", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Jueves", ThreadLocalRandom.current().nextInt(0, 750));
        barSet.addBar("Viernes", ThreadLocalRandom.current().nextInt(300, 1000));
        barSet.addBar("Sábado", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Domingo", ThreadLocalRandom.current().nextInt(0, 1000));

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
