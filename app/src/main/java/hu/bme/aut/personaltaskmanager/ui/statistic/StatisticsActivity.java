package hu.bme.aut.personaltaskmanager.ui.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.model.DataManager;
import hu.bme.aut.personaltaskmanager.model.StatisticItem;

import static hu.bme.aut.personaltaskmanager.model.DataManager.StatisticType.Monthly;
import static hu.bme.aut.personaltaskmanager.model.DataManager.StatisticType.Weekly;

public class StatisticsActivity extends AppCompatActivity {

    private LineChart weeklyChart;
    private ImageButton btnPrevWeekly;
    private ImageButton btnNextWeekly;

    private BarChart monthlyChart;
    private ImageButton btnPrevMonthly;
    private ImageButton btnNextMonthly;

    private int currentWeekOffset;
    private int currentMonthOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        currentWeekOffset = 0;
        currentMonthOffset = 0;

        weeklyChart = (LineChart) findViewById(R.id.lcWeekly);
        btnPrevWeekly = (ImageButton) findViewById(R.id.btnWeeklyLeft);
        btnPrevWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeekOffset--;
                loadWeeklyDatas(currentWeekOffset);
                Toast.makeText(StatisticsActivity.this, "Prev pressed: " + currentWeekOffset, Toast.LENGTH_SHORT).show();
            }
        });
        btnNextWeekly = (ImageButton) findViewById(R.id.btnWeeklyRight);
        btnNextWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentWeekOffset++;
                loadWeeklyDatas(currentWeekOffset);
                Toast.makeText(StatisticsActivity.this, "Next pressed: " + currentWeekOffset, Toast.LENGTH_SHORT).show();
            }
        });

        monthlyChart = (BarChart) findViewById(R.id.bcMonthly);
        btnPrevMonthly = (ImageButton) findViewById(R.id.btnMonthlyLeft);
        btnPrevMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonthOffset--;
                loadMonthlyDatas(currentMonthOffset);
                Toast.makeText(StatisticsActivity.this, "Prev pressed: " + currentMonthOffset, Toast.LENGTH_SHORT).show();
            }
        });
        btnNextMonthly = (ImageButton) findViewById(R.id.btnMonthlyRight);
        btnNextMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMonthOffset++;
                loadMonthlyDatas(currentMonthOffset);
                Toast.makeText(StatisticsActivity.this, "Next pressed: " + currentMonthOffset, Toast.LENGTH_SHORT).show();
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
        Legend legend = weeklyChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setDrawInside(true);
        legend.setXEntrySpace(15f);

        // DESCRIPTION
        weeklyChart.getDescription().setEnabled(false);

        loadWeeklyDatas(0);
    }

    private void loadWeeklyDatas(int weekOffset) {
        // DATAS
        Calendar c = Calendar.getInstance();

        int today = c.get(Calendar.DAY_OF_WEEK);
        if(today == 1) today = 8;

        c.add(Calendar.DAY_OF_WEEK, (-today+2)+(weekOffset*7));
        Calendar firstDay = new GregorianCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);

        c.add(Calendar.DAY_OF_WEEK, today+(7-today)-1);
        Calendar lastDay = new GregorianCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

        List<StatisticItem> statItems = DataManager.getInstance().getStatistic(Weekly, firstDay.getTimeInMillis(), lastDay.getTimeInMillis());

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
        // X AXIS
        final String[] xLabel = { "All", "Done", "Overdue"};

        XAxis xAxis = monthlyChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setYOffset(10f);
        xAxis.setAxisLineWidth(2f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setLabelRotationAngle((float)45.0);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(10f);
        //xAxis.setLabelCount(3, true);
        xAxis.setDrawLabels(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel[(int)value];
            }
        });

        // Y AXIS
        monthlyChart.getAxisRight().setEnabled(false);
        monthlyChart.getAxisLeft().setEnabled(false);
        YAxis yAxis = monthlyChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        //yAxis.setXOffset(10f);
        //yAxis.setAxisLineWidth(2f);
        //yAxis.setAxisLineColor(Color.BLACK);
        ////yAxis.setDrawGridLines(false);
        //yAxis.setTextColor(Color.BLACK);
        yAxis.setGranularityEnabled(true);
        yAxis.setGranularity(1f);

        monthlyChart.setAutoScaleMinMaxEnabled(true);

        // LEGEND
        Legend legend = monthlyChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setDrawInside(true);
        legend.setXEntrySpace(15f);

        // DESCRIPTION
        monthlyChart.getDescription().setEnabled(false);

        monthlyChart.setFitBars(true);

        loadMonthlyDatas(0);
    }

    private void loadMonthlyDatas(int monthOffset) {
        // DATAS
        Calendar c = Calendar.getInstance();

        c.add(Calendar.MONTH, monthOffset);
        int daysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar firstDay = new GregorianCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), 1, 0, 0, 0);
        Calendar lastDay = new GregorianCalendar(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), daysOfMonth, 23, 59, 59);

        List<StatisticItem> statItems = DataManager.getInstance().getStatistic(Monthly, firstDay.getTimeInMillis(), lastDay.getTimeInMillis());

        List<BarEntry> entryAllTasks = new ArrayList<>();
        List<BarEntry> entryDoneTasks = new ArrayList<>();
        List<BarEntry> entryOverdueTasks = new ArrayList<>();

        entryAllTasks.add(new BarEntry(0, statItems.get(0).getAll()));
        entryDoneTasks.add(new BarEntry(1, statItems.get(0).getDone()));
        entryOverdueTasks.add(new BarEntry(2, statItems.get(0).getOverdue()));


        BarDataSet dataSetAllTasks = new BarDataSet(entryAllTasks, "All");
        dataSetAllTasks.setColor(Color.BLUE);
        dataSetAllTasks.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)value);
            }
        });
        dataSetAllTasks.setBarBorderWidth(1f);
        dataSetAllTasks.setBarShadowColor(Color.GRAY);

        BarDataSet dataSetDoneTasks = new BarDataSet(entryDoneTasks, "Done");
        dataSetDoneTasks.setColor(Color.GREEN);
        dataSetDoneTasks.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)value);
            }
        });
        dataSetDoneTasks.setBarBorderWidth(1f);
        dataSetDoneTasks.setBarShadowColor(Color.GRAY);

        BarDataSet dataSetOverdueTasks = new BarDataSet(entryOverdueTasks, "Overdue");
        dataSetOverdueTasks.setColor(Color.RED);
        dataSetOverdueTasks.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int)value);
            }
        });
        dataSetOverdueTasks.setBarBorderWidth(1f);
        dataSetOverdueTasks.setBarShadowColor(Color.GRAY);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSetAllTasks);
        dataSets.add(dataSetDoneTasks);
        dataSets.add(dataSetOverdueTasks);

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.9f);
        monthlyChart.setData(data);

        monthlyChart.invalidate();
    }
}
