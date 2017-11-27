package hu.bme.aut.personaltaskmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import hu.bme.aut.personaltaskmanager.R;
import hu.bme.aut.personaltaskmanager.ui.handling_tasks.TasksActivity;
import hu.bme.aut.personaltaskmanager.ui.statistic.StatisticsActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageButton btnTasks = (ImageButton) findViewById(R.id.btnTasks);
        btnTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tasksIntent = new Intent(MainMenuActivity.this, TasksActivity.class);
                startActivity(tasksIntent);
            }
        });

        ImageButton btnStatistics = (ImageButton) findViewById(R.id.btnStatistic);
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statIntent = new Intent(MainMenuActivity.this, StatisticsActivity.class);
                startActivity(statIntent);
            }
        });
    }
}
