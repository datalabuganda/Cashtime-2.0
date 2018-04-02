package com.example.eq62roket.cashtime.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.eq62roket.cashtime.Helper.ParseExpenditureHelper;
import com.example.eq62roket.cashtime.Models.GroupExpenditure;
import com.example.eq62roket.cashtime.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

public class AddGroupExpenditureActivity extends AppCompatActivity {
    private static String TAG = "AddGroupExpenditureActivity";
    EditText mGroupExpenditureCategory,  mGroupExpenditureAmount, mGroupExpenditureDate, mGroupExpenditureNotes;
    Button groupExpenditureCancelBtn, groupExpenditureSaveBtn;
    ImageView addExpenditureCategoryIcon;

    public static String[] expenditureCategories = {"Rent", "Food", "Medical", "Transport", "Leisure", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_expenditure);
        mGroupExpenditureCategory = (EditText) findViewById(R.id.groupExpenditureCategory);
        mGroupExpenditureAmount = (EditText)findViewById(R.id.groupExpenditureAmount);
        mGroupExpenditureDate = (EditText)findViewById(R.id.groupExpenditureDate);
        mGroupExpenditureNotes = (EditText)findViewById(R.id.groupExpenditureNotes);

        addExpenditureCategoryIcon = (ImageView)findViewById(R.id.addExpenditureCategoryIcon);

        addExpenditureCategoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent expenditureCategory = new Intent(AddGroupExpenditureActivity.this, AddExpenditureCategoryActivity.class);
                startActivity(expenditureCategory);
            }
        });

        groupExpenditureSaveBtn = (Button)findViewById(R.id.groupExpenditureSaveBtn);
        groupExpenditureCancelBtn = (Button)findViewById(R.id.groupExpenditureCancelBtn);

        groupExpenditureCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTabbedExpenditureActivity();
            }
        });

        groupExpenditureSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveGroupExpenditure();
            }
        });

        groupExpenditureCategory();
    }

    public void groupExpenditureCategory(){
        ArrayAdapter<String> expenditureCategoryAdapter = new ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                expenditureCategories
        );

        MaterialBetterSpinner materialExpenditureCategorySpinner = (MaterialBetterSpinner) findViewById(R.id.groupExpenditureCategory);
        materialExpenditureCategorySpinner.setAdapter(expenditureCategoryAdapter);
    }


    private void saveGroupExpenditure(){
        if ( !mGroupExpenditureCategory.getText().toString().equals("") &&
                !mGroupExpenditureAmount.getText().toString().equals("")){
            String groupExpenditureCategory = mGroupExpenditureCategory.getText().toString();
            String groupExpenditureAmount = mGroupExpenditureAmount.getText().toString();
            String groupExpenditureDate = mGroupExpenditureDate.getText().toString();
            String groupExpenditureNotes = mGroupExpenditureNotes.getText().toString();
            String currentUser = ParseUser.getCurrentUser().getObjectId();

            GroupExpenditure groupExpenditures = new GroupExpenditure();
            groupExpenditures.setCategory(groupExpenditureCategory);
            groupExpenditures.setNotes(groupExpenditureNotes);
            groupExpenditures.setDueDate(groupExpenditureDate);
            groupExpenditures.setAmount(groupExpenditureAmount);
            groupExpenditures.setUserId(currentUser);

            Log.d(TAG, "saveGroupExpenditure: " + groupExpenditures);

            // TODO: 3/22/18 =====> save object to db
            new ParseExpenditureHelper(this).saveGroupExpenditureToParseDb(groupExpenditures);
            startTabbedExpenditureActivity();

            Toast.makeText(AddGroupExpenditureActivity.this, "Group Expenditure " + groupExpenditures.getAmount() + " saved", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(AddGroupExpenditureActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    public void startTabbedExpenditureActivity(){
        Intent tabbedExpenditureIntent = new Intent(AddGroupExpenditureActivity.this, TabbedExpenditureActivity.class);
        startActivity(tabbedExpenditureIntent);
        finish();
    }
}
