package com.example.eq62roket.cashtime.Helper;


import android.content.Context;
import android.util.Log;

import com.example.eq62roket.cashtime.Models.User;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eq62roket on 3/28/18.
 */

@ParseClassName("GroupMember")
public class ParseUserHelper {
    public interface OnReturnedUserListener{
        void onResponse(List<User> usersList);
        void onFailure(String error);
    }


    private static final String TAG = "ParseUserHelper";
    private final List<User> userList = new ArrayList<>();


    private Context mContext;
    private ParseUserHelper.OnReturnedUserListener mOnReturnedUserListener;


    public ParseUserHelper(Context context){
        mContext = context;
    }

    public void updateUserInParseDb(final User userToUpdate){
        ParseQuery<User> userQuery = ParseQuery.getQuery("User");
        userQuery.fromLocalDatastore();
        userQuery.whereEqualTo("objectId", userToUpdate.getParseId());
        userQuery.getInBackground(userToUpdate.getParseId(), new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    user.put("username", userToUpdate.getUserName());
                    user.put("userPhone", userToUpdate.getPhoneNumber());
                    user.put("userHousehold", userToUpdate.getHousehold());
                    user.put("userBusiness", userToUpdate.getBusiness());
                    user.put("userGender", userToUpdate.getGender());
                    user.put("userEducationLevel", userToUpdate.getEducationLevel());
                    user.put("userNationality", userToUpdate.getNationality());
                    user.put("userLocation", userToUpdate.getLocation());
                    user.saveInBackground();

                }else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }

        });
    }
}
