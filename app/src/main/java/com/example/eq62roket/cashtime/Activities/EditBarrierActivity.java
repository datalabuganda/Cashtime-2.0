package com.example.eq62roket.cashtime.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eq62roket.cashtime.Models.Barrier;
import com.example.eq62roket.cashtime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditBarrierActivity extends AppCompatActivity {

    private Button barrierDeleteBtn, barrierUpdateBtn;
    private EditText barrierNotes, goalName, barrierName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barrier);

        Intent editBarrierIntent = getIntent();
        String nameOfGoal = editBarrierIntent.getStringExtra("barrierGoalName");
        final String nameOfBarrier = editBarrierIntent.getStringExtra("barrierName");
        String barrierText = editBarrierIntent.getStringExtra("barrierNotes");

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goalName = (EditText) findViewById(R.id.goalName);
        barrierName = (EditText) findViewById(R.id.barrierName);
        barrierNotes = (EditText) findViewById(R.id.barrierNotes);
        barrierDeleteBtn = (Button) findViewById(R.id.barrierDeleteBtn);
        barrierUpdateBtn = (Button) findViewById(R.id.barrierUpdateBtn);

        // prepopulate goalName field
        goalName.setText(nameOfGoal);
        barrierName.setText(nameOfBarrier);
        barrierNotes.setText(barrierText);

        barrierUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBarrier();
            }
        });

        barrierDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start a dialog fragment
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                // Add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete Saving, redirect to group goals fragment
                        // TODO: 3/22/18 ====> delete barrier record

                        // start TabbedSavingActivity
                        startTabbedBarriersTipsctivity();
                        Toast.makeText(EditBarrierActivity.this, "Barrier deleted successfully", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancel
                    }
                });

                builder.setMessage(
                        "Deleting Barrier '" + nameOfBarrier + "' Can not be undone." + "Are You Sure You want to delete this Barrier?").setTitle("Delete Barrier   ");


                // Create the AlertDialog
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void saveBarrier(){
        if ( !goalName.getText().toString().equals("") &&
                !barrierName.getText().toString().equals("") &&
                !barrierNotes.getText().toString().equals("")){

            Date today = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            String dateToday = simpleDateFormat.format(today);

            Barrier newBarrier = new Barrier();
            newBarrier.setGoalName(goalName.getText().toString());
            newBarrier.setBarrierName(barrierName.getText().toString());
            newBarrier.setBarrierText(barrierNotes.getText().toString());
            newBarrier.setDateAdded(dateToday);
            newBarrier.setTipGiven(false);

            // TODO: 3/23/18 ===>>> save barrier to database

            startTabbedBarriersTipsctivity();
            Toast.makeText(this, "Good to save " + newBarrier.getBarrierName(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startTabbedBarriersTipsctivity(){
        Intent tabbedBarriersTipsActivity = new Intent(EditBarrierActivity.this, TabbedBarriersTipsActivity.class);
        startActivity(tabbedBarriersTipsActivity);
        finish();
    }
}
