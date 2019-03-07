package com.floorists.grannyspillbox;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.floorists.grannyspillbox.classes.Medication;
import com.floorists.grannyspillbox.classes.ScheduledEvent;
import com.floorists.grannyspillbox.utilities.BarcodeCaptureActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "MAINACTIVITY";
    private TableLayout eventTable;
    private ArrayList<ScheduledEvent> mockdata = ScheduledEvent.getMockData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        eventTable = (TableLayout)findViewById(R.id.eventTable);
        for(int i=0; i<mockdata.size(); i++) {
            insertEvent(mockdata.get(i), i+1);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                /* Barcode scan

                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, true);
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
                 */

                // add medication
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Medication");

                final EditText medNameInput = new EditText(MainActivity.this);
                medNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(medNameInput);

                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get med name and send to api
                        String medName = medNameInput.getText().toString();

                        Medication medication;

                        try {
                            Future<Medication> future =  FDATransport.getMedInfo(medName);
                            while(!future.isDone()) { }

                            medication = future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                            medication = null;
                        }

                        if(medication != null) {
                            Snackbar.make(view, medication.getDescription(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                    }
                });

                builder.create().show();
            }
       });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
        } else if (id == R.id.nav_medications) {

        } else if (id == R.id.nav_medicationSchedule) {

        } else if (id == R.id.nav_emergencyContacts) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    final Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    // todo: handle barcode

                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
     private void insertEvent(ScheduledEvent event, int index) {
         LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View newEvent = inflator.inflate(R.layout.scheduled_meds, null);
         CheckBox completedBox = (CheckBox) newEvent.findViewById(R.id.completedCheckBox);
         TextView medNameTextView = (TextView) newEvent.findViewById(R.id.pillNameTextView);
         TextView timeTextView = (TextView) newEvent.findViewById(R.id.timeTextView);
         TextView qtyTextView = (TextView) newEvent.findViewById(R.id.qtyTextView);

         if(event.medication != null) {
             medNameTextView.setText(event.medication.getName());
         } else {
             medNameTextView.setText("Advil");
         }

         completedBox.setChecked(event.isCompleted());

         SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
         timeTextView.setText(sdf.format(event.getTime()));
         qtyTextView.setText(String.valueOf(event.getQty()));
         eventTable.addView(newEvent, index);
     }
}
