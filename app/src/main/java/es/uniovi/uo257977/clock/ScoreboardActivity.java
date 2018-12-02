package es.uniovi.uo257977.clock;

import android.os.Bundle;

import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;

import java.util.concurrent.ThreadLocalRandom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class ScoreboardActivity extends AppCompatActivity {

    private BarChartView scoreboardChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreboardChart = findViewById(R.id.scoreboard_chart);

        // GRAFICA DE PRUEBA
        BarSet barSet = new BarSet();
        barSet.addBar("Lunes", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Martes", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Miércoles", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Jueves", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Viernes", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Sábado", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.addBar("Domingo", ThreadLocalRandom.current().nextInt(0, 1000));
        barSet.setColor(ResourcesCompat.getColor(getResources(), R.color.accent, getTheme()));

        scoreboardChart.addData(barSet);
        scoreboardChart.setRoundCorners(20);
        scoreboardChart.show();
    }
}
