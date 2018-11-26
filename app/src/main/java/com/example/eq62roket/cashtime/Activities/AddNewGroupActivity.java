package com.example.eq62roket.cashtime.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eq62roket.cashtime.Helper.CashTimeUtils;
import com.example.eq62roket.cashtime.Helper.ParseGroupHelper;
import com.example.eq62roket.cashtime.Helper.ParseRegistrationHelper;
import com.example.eq62roket.cashtime.Helper.PeriodHelper;
import com.example.eq62roket.cashtime.Interfaces.OnSuccessfulGroupLeaderStatusUpdate;
import com.example.eq62roket.cashtime.Models.Group;
import com.example.eq62roket.cashtime.Models.GroupMember;
import com.example.eq62roket.cashtime.Models.User;
import com.example.eq62roket.cashtime.R;
import com.parse.ParseUser;

public class AddNewGroupActivity extends AppCompatActivity {
    private static final String TAG = "NewGroupActivity";
    private ParseGroupHelper mParseGroupHelper;

    EditText groupName, groupLocation, groupCenter;
    Button groupCancelBtn, groupSaveBtn;
    String groupLocalUniqueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        mParseGroupHelper = new ParseGroupHelper(AddNewGroupActivity.this);

        groupLocalUniqueId = new CashTimeUtils().getUUID();


        groupName = (EditText)findViewById(R.id.groupName);
        groupLocation = (EditText)findViewById(R.id.groupLocation);
        groupCenter = (EditText)findViewById(R.id.groupCenter);

        groupCancelBtn = (Button)findViewById(R.id.groupCancelBtn);
        groupSaveBtn = (Button)findViewById(R.id.groupSaveBtn);

        groupSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!groupName.getText().toString().trim().equals("") &&
                        !groupLocation.getText().toString().trim().equals("") &&
                        !groupCenter.getText().toString().trim().equals("")){

                    String nameOfGroup = groupName.getText().toString().trim();
                    String locationOfGroup = groupLocation.getText().toString().trim();
                    String groupCentreName = groupCenter.getText().toString().trim();

                    final String currentUserId = ParseUser.getCurrentUser().getObjectId();
                    String dateToday = new PeriodHelper().getDateToday();

                    Group groupTosave = new Group();
                    groupTosave.setGroupCreatorId(currentUserId);
                    groupTosave.setGroupLeaderId(currentUserId);
                    groupTosave.setDateCreated(dateToday);
                    groupTosave.setGroupName(nameOfGroup);
                    groupTosave.setGroupCentreName(groupCentreName);
                    groupTosave.setLocationOfGroup(locationOfGroup);
                    groupTosave.setGroupMemberCount(1);
                    groupTosave.setLocalUniqueID(groupLocalUniqueId);
                    new ParseGroupHelper(AddNewGroupActivity.this).saveNewGroupToParseDb(groupTosave);

                    User groupLeader = new User();
                    groupLeader.setParseId(currentUserId);
                    groupLeader.setIsLeader(true);
                    new ParseRegistrationHelper(AddNewGroupActivity.this)
                            .updateIsLeaderFlagInParseDb(groupLeader, new OnSuccessfulGroupLeaderStatusUpdate() {
                                @Override
                                public void onResponse(String success) {
                                    addGroupLeaderToGroup();

                                    Intent groupIntent = new Intent(AddNewGroupActivity.this, GroupsActivity.class);
                                    startActivity(groupIntent);
                                    finish();
                                    Toast.makeText(AddNewGroupActivity.this, "Your group has been created", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(String error) {
                                    Toast.makeText(AddNewGroupActivity.this, "Failed to create group", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else {
                    Toast.makeText(AddNewGroupActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }

            }
        });

        groupCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeActivityIntent = new Intent(AddNewGroupActivity.this, GroupsActivity.class);
                startActivity(homeActivityIntent);
                finish();
            }
        });

    }

    public void addGroupLeaderToGroup(){
        GroupMember newGroupMember = new GroupMember();
        ParseUser groupLeader = ParseUser.getCurrentUser();

        newGroupMember.setMemberUsername(groupLeader.getUsername());
        newGroupMember.setMemberPhoneNumber(groupLeader.getString("userPhone"));
        newGroupMember.setMemberHousehold(groupLeader.getString("userHousehold"));
        newGroupMember.setMemberBusiness(groupLeader.getString("userBusiness"));
        newGroupMember.setMemberGender(groupLeader.getString("userGender"));
        newGroupMember.setMemberEducationLevel(groupLeader.getString("userEducationLevel"));
        newGroupMember.setMemberNationality(groupLeader.getString("userNationality"));
        newGroupMember.setMemberLocation(groupLeader.getString("userLocation"));
        newGroupMember.setMemberGroupLocalUniqueId(groupLocalUniqueId);
        newGroupMember.setGroupName(groupName.getText().toString().trim());
        newGroupMember.setIsLeader(groupLeader.getBoolean("isLeader"));
        newGroupMember.setMemberPoints(groupLeader.getInt("userPoints"));
        mParseGroupHelper.saveGroupMemberUserToParseDb(newGroupMember);
    }
}
