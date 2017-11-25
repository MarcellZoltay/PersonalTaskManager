package hu.bme.aut.personaltaskmanager.ui.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.StatisticItem;

public class StatisticsActivity extends AppCompatActivity {

    private LineChart weeklyChart;
    private ImageButton btnWeeklyLeft;
    private ImageButton btnWeeklyRight;

    private LineChart monthlyChart;
    private ImageButton btnMonthlyLeft;
    private ImageButton btnMonthlyRight;

    private int currentWeekOffset;
    private int currentMonthOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentWeekOffset = 0;
        currentMonthOffset = 0;

        weeklyChart = (LineChart) findViewById(R.id.lcWeekly);
        Button btnPrevWeekly = (Button) findViewById(R.id.btnWeeklyLeft);
        btnPrevWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeekOffset--;
                loadWeeklyDatas(currentWeekOffset);
                Toast.makeText(StatisticsActivity.this, "Prev pressed: " + currentWeekOffset, Toast.LENGTH_SHORT).show();
            }
        });
        Button btnNextWeekly = (Button) findViewById(R.id.btnWeeklyRight);
        btnNextWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeekOffset++;
                loadWeeklyDatas(currentWeekOffset);
                Toast.makeText(StatisticsActivity.this, "Next pressed: " + currentWeekOffset, Toast.LENGTH_SHORT).show();
            }
        });

        initWeeklyChart();
        initMonthlyChart();
    }

    private void initWeeklyChart(){
        // X AXIS
        final String[] xLabel = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        XAxis xAxis = weeklyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(10f);
        xAxis.setAxisLineWidth(2f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setLabelRotationAngle((float)45.0);
        //xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        xAxis.setLabelCount(7, true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel[(int)value];
            }
        });

        // Y AXIS
        weeklyChart.getAxisRight().setEnabled(false);
        YAxis yAxis = weeklyChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setXOffset(10f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(Color.BLACK);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);

        weeklyChart.setAutoScaleMinMaxEnabled(true);

        // LEGEND
        weeklyChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        weeklyChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        weeklyChart.getLegend().setDrawInside(true);

        // DESCRIPTION
        weeklyChart.getDescription().setEnabled(false);

        loadWeeklyDatas(0);
    }

    private void loadWeeklyDatas(int weekOffset) {
        // DATAS
        Calendar c = Calendar.getInstance();

        int today = c.get(Calendar.DAY_OF_WEEK);

        c.add(Calendar.DAY_OF_WEEK, (-today+2)+(weekOffset*7));
        long firstDay = c.getTimeInMillis();
        //nt firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //nt firstDayDay = c.get(Calendar.DAY_OF_MONTH);
        //nt firstDayMonth = c.get(Calendar.MONTH);

        c.add(Calendar.DAY_OF_WEEK, today+(7-today)-1);
        long lastDay = c.getTimeInMillis();
        //int lastDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        //int lastDayDay = c.get(Calendar.DAY_OF_MONTH);
        //int lastDayMonth = c.get(Calendar.MONTH);

        List<StatisticItem> statItems = DataManager.getInstance().getStatistic(firstDay, lastDay);

        List<Entry> entryAllTasks = new ArrayList<>();
        List<Entry> entryDoneTasks = new ArrayList<>();
        List<Entry> entryOverdueTasks = new ArrayList<>();

        for(int i = 0; i < statItems.size(); i++){
            entryAllTasks.add(new Entry(i, statItems.get(i).getAll()));
            entryDoneTasks.add(new Entry(i, statItems.get(i).getDone()));
            entryOverdueTasks.add(new Entry(i, statItems.get(i).getOverdue()));
        }

        LineDataSet dataSetAllTasks = new LineDataSet(entryAllTasks, "All");
        dataSetAllTasks.setColor(Color.BLUE);
        dataSetAllTasks.setCircleColor(Color.BLUE);
        dataSetAllTasks.setCircleColorHole(Color.WHITE);
        dataSetAllTasks.setDrawValues(false);
        LineDataSet dataSetDoneTasks = new LineDataSet(entryDoneTasks, "Done");
        dataSetDoneTasks.setColor(Color.GREEN);
        dataSetDoneTasks.setCircleColor(Color.GREEN);
        dataSetDoneTasks.setCircleColorHole(Color.WHITE);
        dataSetDoneTasks.setDrawValues(false);
        LineDataSet dataSetOverdueTasks = new LineDataSet(entryOverdueTasks, "Overdue");
        dataSetOverdueTasks.setColor(Color.RED);
        dataSetOverdueTasks.setCircleColor(Color.RED);
        dataSetOverdueTasks.setCircleColorHole(Color.WHITE);
        dataSetOverdueTasks.setDrawValues(false);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetAllTasks);
        dataSets.add(dataSetDoneTasks);
        dataSets.add(dataSetOverdueTasks);

        LineData data = new LineData(dataSets);
        weeklyChart.setData(data);

        weeklyChart.invalidate();
    }

    private void initMonthlyChart(){

        loadMonthlyDatas(0);
    }

    private void loadMonthlyDatas(int monthOffset) {

    }
}
