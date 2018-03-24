package com.example.eq62roket.cashtime.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eq62roket.cashtime.Activities.AddGroupMembersExpendituresActivity;
import com.example.eq62roket.cashtime.R;


public class MembersExpendituresFragment extends Fragment {
    FloatingActionButton fabMembersExpenditures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_members_expenditures, container, false);
        fabMembersExpenditures = (FloatingActionButton) rootView.findViewById(R.id.fabMemberExpenditures);

        fabMembersExpenditures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MembersExpendituresFragment.this.getContext(),AddGroupMembersExpendituresActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}