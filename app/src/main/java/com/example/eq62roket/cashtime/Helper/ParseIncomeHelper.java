package com.example.eq62roket.cashtime.Helper;

import android.content.Context;
import android.util.Log;

import com.example.eq62roket.cashtime.Models.GroupExpenditure;
import com.example.eq62roket.cashtime.Models.GroupIncome;
import com.example.eq62roket.cashtime.Models.MembersIncome;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eq62roket on 3/28/18.
 */

public class ParseIncomeHelper {


    public interface OnReturnedGroupMemberIncomeListener{
        void onResponse(List<MembersIncome> groupMemberIncomeList);
        void onFailure(String error);
    }

    public interface OnReturnedGroupIncomeListener{
        void onResponse(List<GroupIncome> groupIncomeList);
        void onFailure(String error);
    }

    public interface OnReturnedGroupSumOfIncomeListener{
        void onResponse(int sumOfGroupIncome);
        void onFailure(String error);
    }

    public interface OnReturnedMemberSumOfIncomeListener{
        void onResponse(int sumOfMemberIncome);
        void onFailure(String error);
    }


    private static final String TAG = "ParseHelper";
    private final List<MembersIncome> groupMemberIncomeList = new ArrayList<>();
    private final List<GroupIncome> groupIncomeList = new ArrayList<>();


    private Context mContext;
    private ParseIncomeHelper.OnReturnedGroupMemberIncomeListener mOnReturnedGroupMemberIncomeListener;
    private ParseIncomeHelper.OnReturnedGroupIncomeListener mOnReturnedGroupIncomeListener;

    public ParseIncomeHelper(Context context){
        mContext = context;
    }

    public void saveGroupMemberIncomeToParseDb(MembersIncome groupMemberIncome){
        MembersIncome newGroupMemberIncome = new MembersIncome();
        newGroupMemberIncome.put("groupMemberIncomeSource", groupMemberIncome.getSource());
        newGroupMemberIncome.put("groupMemberIncomeAmount", groupMemberIncome.getAmount());
        newGroupMemberIncome.put("groupMemberIncomeNotes", groupMemberIncome.getNotes());
        newGroupMemberIncome.put("groupMemberIncomePeriod", groupMemberIncome.getDueDate());
        newGroupMemberIncome.put("groupMemberUsername", groupMemberIncome.getMemberUserName());
        newGroupMemberIncome.put("groupMemberParseId", groupMemberIncome.getMemberParseId());
        newGroupMemberIncome.put("createdById", groupMemberIncome.getUserId());
        newGroupMemberIncome.saveInBackground();

    }

    public void getGroupMemberIncomeMemberFromParseDb(final ParseIncomeHelper.OnReturnedGroupMemberIncomeListener onReturnedGroupMemberIncomeListener){
        ParseQuery<MembersIncome> groupMemberIncomeQuery = ParseQuery.getQuery("GroupMembersIncome");
        String currentUser = ParseUser.getCurrentUser().getObjectId();
        groupMemberIncomeQuery.addDescendingOrder("updatedAt");
        groupMemberIncomeQuery.whereEqualTo("createdById", currentUser);
        groupMemberIncomeQuery.findInBackground(new FindCallback<MembersIncome>() {
            @Override
            public void done(List<MembersIncome> parseGroupMemberIncome, ParseException e) {
                if (e == null){
                    for (MembersIncome retrievedGroupMemberIncome: parseGroupMemberIncome){
                        MembersIncome newGroupMemberIncome = new MembersIncome();
                        newGroupMemberIncome.setSource(retrievedGroupMemberIncome.get("groupMemberIncomeSource").toString());
                        newGroupMemberIncome.setAmount(retrievedGroupMemberIncome.get("groupMemberIncomeAmount").toString());
                        newGroupMemberIncome.setNotes(retrievedGroupMemberIncome.get("groupMemberIncomeNotes").toString());
                        newGroupMemberIncome.setDueDate(retrievedGroupMemberIncome.get("groupMemberIncomePeriod").toString());
                        newGroupMemberIncome.setMemberUserName(retrievedGroupMemberIncome.get("groupMemberUsername").toString());
                        newGroupMemberIncome.setUserId(retrievedGroupMemberIncome.get("createdById").toString());
                        newGroupMemberIncome.setParseId(retrievedGroupMemberIncome.getObjectId());

                        groupMemberIncomeList.add(newGroupMemberIncome);
                    }
                    if (onReturnedGroupMemberIncomeListener != null){
                        onReturnedGroupMemberIncomeListener.onResponse(groupMemberIncomeList);
                    }
                }else {
                    onReturnedGroupMemberIncomeListener.onFailure(e.getMessage());
                }
            }

        });

    }

    public void updateGroupMemberIncomeInParseDb(final MembersIncome groupMemberIncomeToUpdate){
        ParseQuery<MembersIncome> groupMemberIncomeQuery = ParseQuery.getQuery("GroupMembersIncome");
        groupMemberIncomeQuery.getInBackground(groupMemberIncomeToUpdate.getParseId(), new GetCallback<MembersIncome>() {
            @Override
            public void done(MembersIncome groupMemberIncome, ParseException e) {
                if (e == null) {
                    groupMemberIncome.put("groupMemberIncomeSource", groupMemberIncomeToUpdate.getSource());
                    groupMemberIncome.put("groupMemberIncomeAmount", groupMemberIncomeToUpdate.getAmount());
                    groupMemberIncome.put("groupMemberIncomeNotes", groupMemberIncomeToUpdate.getNotes());
                    groupMemberIncome.put("groupMemberIncomedueDate", groupMemberIncomeToUpdate.getDueDate());
                    groupMemberIncome.saveInBackground();

                }else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }

        });
    }

    public void deleteGroupMemberIncomeFromParseDb(MembersIncome groupMemberIncomeToDelete){
        ParseQuery<MembersIncome> groupMemberIncomeQuery = ParseQuery.getQuery("GroupMembersIncome");
        groupMemberIncomeQuery.getInBackground(groupMemberIncomeToDelete.getParseId(), new GetCallback<MembersIncome>() {
            @Override
            public void done(MembersIncome groupMemberIncome, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Should delete now: ");
                    groupMemberIncome.deleteInBackground();
                }else {
                    Log.d(TAG, "Error Occured: " + e.getMessage());
                }
            }
        });
    }

    /******************************** Group Income **********************************/

    public void saveGroupIncomeToParseDb(GroupIncome groupIncome){
        GroupIncome newGroupIncome = new GroupIncome();
        newGroupIncome.put("groupIncomeSource", groupIncome.getSource());
        newGroupIncome.put("groupIncomeAmount", groupIncome.getAmount());
        newGroupIncome.put("groupIncomeNotes", groupIncome.getNotes());
        newGroupIncome.put("groupIncomePeriod", groupIncome.getPeriod());
        newGroupIncome.put("groupName", groupIncome.getGroupName());
        newGroupIncome.put("groupParseId", groupIncome.getGroupParseId());
        newGroupIncome.put("createdById", groupIncome.getUserId());
        newGroupIncome.saveInBackground();

    }


    public void updateGroupIncomeInParseDb(final GroupIncome groupIncomeToUpdate){
        ParseQuery<GroupIncome> groupIncomeQuery = ParseQuery.getQuery("GroupIncome");
        groupIncomeQuery.getInBackground(groupIncomeToUpdate.getParseId(), new GetCallback<GroupIncome>() {
            @Override
            public void done(GroupIncome groupMemberIncome, ParseException e) {
                if (e == null) {
                    groupMemberIncome.put("groupIncomeSource", groupIncomeToUpdate.getSource());
                    groupMemberIncome.put("groupIncomeAmount", groupIncomeToUpdate.getAmount());
                    groupMemberIncome.put("groupIncomeNotes", groupIncomeToUpdate.getNotes());
                    groupMemberIncome.put("groupIncomePeriod", groupIncomeToUpdate.getPeriod());
                    groupMemberIncome.put("createdById", groupIncomeToUpdate.getUserId());
                    groupMemberIncome.saveInBackground();

                }else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }

        });
    }

    public void getGroupIncomeFromParseDb(final ParseIncomeHelper.OnReturnedGroupIncomeListener onReturnedGroupIncomeListener){
        ParseQuery<GroupIncome> groupIncomeQuery = ParseQuery.getQuery("GroupIncome");
        String currentUser = ParseUser.getCurrentUser().getObjectId();
        groupIncomeQuery.addDescendingOrder("updatedAt");
        groupIncomeQuery.whereEqualTo("createdById", currentUser);
        groupIncomeQuery.findInBackground(new FindCallback<GroupIncome>() {
            @Override
            public void done(List<GroupIncome> parseGroupIncome, ParseException e) {
                if (e == null){
                    for (GroupIncome retrievedGroupIncome: parseGroupIncome){
                        GroupIncome newGroupIncome = new GroupIncome();
                        newGroupIncome.setSource(retrievedGroupIncome.get("groupIncomeSource").toString());
                        newGroupIncome.setAmount(retrievedGroupIncome.get("groupIncomeAmount").toString());
                        newGroupIncome.setNotes(retrievedGroupIncome.get("groupIncomeNotes").toString());
                        newGroupIncome.setPeriod(retrievedGroupIncome.get("groupIncomePeriod").toString());
                        newGroupIncome.setGroupName(retrievedGroupIncome.get("groupName").toString());
                        newGroupIncome.setParseId(retrievedGroupIncome.getObjectId());

                        groupIncomeList.add(newGroupIncome);
                    }
                    if (onReturnedGroupIncomeListener != null){
                        onReturnedGroupIncomeListener.onResponse(groupIncomeList);
                    }
                }else {
                    onReturnedGroupIncomeListener.onFailure(e.getMessage());
                }
            }

        });

    }

    public void deleteGroupIncomeFromParseDb(GroupIncome groupIncomeToDelete){
        ParseQuery<GroupIncome> groupIncomeQuery = ParseQuery.getQuery("GroupIncome");
        groupIncomeQuery.getInBackground(groupIncomeToDelete.getParseId(), new GetCallback<GroupIncome>() {
            @Override
            public void done(GroupIncome groupIncome, ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Should delete now: ");
                    groupIncome.deleteInBackground();
                }else {
                    Log.d(TAG, "Error Occured: " + e.getMessage());
                }
            }
        });
    }

}