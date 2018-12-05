package com.example.eq62roket.cashtime.Interfaces;

/**
 * Created by etwin on 11/26/18.
 */

public interface OnSuccessfulGroupLeaderStatusUpdate {
    void onResponse(String success);
    void onFailure(String error);
}
