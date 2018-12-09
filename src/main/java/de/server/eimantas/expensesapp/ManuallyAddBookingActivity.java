package de.server.eimantas.expensesapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import de.server.eimantas.expensesapp.entities.Location;
import de.server.eimantas.expensesapp.entities.Project;
import de.server.eimantas.expensesapp.helpers.adapters.DropDownLocationAdapter;
import de.server.eimantas.expensesapp.helpers.adapters.DropDownProjectsAdapter;

public class ManuallyAddBookingActivity extends AppCompatActivity {

    Spinner projectSpinner,worklocationSpinner;
    List<Project> projectArrayList;
    List<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuall_add_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectSpinner = (Spinner) findViewById(R.id.man_project_dropdown);
        worklocationSpinner = (Spinner) findViewById(R.id.man_workLocationSpinner);

        populateDefaults();


        ArrayAdapter<Project> dataAdapter = new DropDownProjectsAdapter(this, projectArrayList);
        projectSpinner.setAdapter(dataAdapter);

        ArrayAdapter<Location> locationArrayAdapter = new DropDownLocationAdapter(this,locationList);
        worklocationSpinner.setAdapter(locationArrayAdapter);


    }

    private void populateDefaults() {
        projectArrayList = new ArrayList<Project>();
        Project project = new Project(1, "No Project");
        projectArrayList.add(project);

        locationList = new ArrayList<Location>();
        Location location = new Location();
        location.setId(2);
        location.setName("No Location");
        locationList.add(location);
    }

}
