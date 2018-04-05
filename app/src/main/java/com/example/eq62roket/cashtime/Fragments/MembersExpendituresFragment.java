package com.example.eq62roket.cashtime.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.eq62roket.cashtime.Activities.AddGroupMembersExpendituresActivity;
import com.example.eq62roket.cashtime.Activities.EditGroupGoalActivity;
import com.example.eq62roket.cashtime.Activities.EditGroupMemberExpenditureActivity;
import com.example.eq62roket.cashtime.Activities.GroupMembersExpenditureListActivity;
import com.example.eq62roket.cashtime.Helper.ParseExpenditureHelper;
import com.example.eq62roket.cashtime.Helper.ParseHelper;
import com.example.eq62roket.cashtime.Models.GroupGoals;
import com.example.eq62roket.cashtime.Models.GroupIncome;
import com.example.eq62roket.cashtime.Models.GroupMemberExpenditure;
import com.example.eq62roket.cashtime.R;
import com.example.eq62roket.cashtime.adapters.GroupGoalsAdapter;
import com.example.eq62roket.cashtime.adapters.GroupMemberExpenditureAdapter;
import com.example.eq62roket.cashtime.adapters.GroupMembersIncomeAdapter;

import java.util.ArrayList;
import java.util.List;


public class MembersExpendituresFragment extends Fragment implements SearchView.OnQueryTextListener{
    FloatingActionButton fabMembersExpenditures;
    private static final String TAG = "GroupGoalsFragment";

    List<GroupMemberExpenditure> groupMemberExpenditures = null;
    private RecyclerView recyclerView;
    private GroupMemberExpenditureAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_members_expenditures, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.members_expenditures_recycler_view);

        fabMembersExpenditures = (FloatingActionButton) rootView.findViewById(R.id.fabMemberExpenditures);

        fabMembersExpenditures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MembersExpendituresFragment.this.getContext(),GroupMembersExpenditureListActivity.class);
                startActivity(intent);
            }
        });

        new ParseExpenditureHelper(getActivity()).getGroupMembersExpenditureFromParseDb(new ParseExpenditureHelper.OnReturnedGroupMembersExpenditureListener() {
            @Override
            public void onResponse(List<GroupMemberExpenditure> groupMemberExpenditureList) {
                groupMemberExpenditures = groupMemberExpenditureList;

                mAdapter = new GroupMemberExpenditureAdapter(groupMemberExpenditureList, new GroupMemberExpenditureAdapter.OnGoalClickListener() {
                    @Override
                    public void onGoalClick(GroupMemberExpenditure groupMemberExpenditure) {
                        Intent editGroupMemberExpenditureIntent = new Intent(getActivity(), EditGroupMemberExpenditureActivity.class);
                        editGroupMemberExpenditureIntent.putExtra("groupMembersExpenditureCategory", groupMemberExpenditure.getCategory());
                        editGroupMemberExpenditureIntent.putExtra("groupMembersExpenditureAmount", groupMemberExpenditure.getAmount());
                        editGroupMemberExpenditureIntent.putExtra("groupMembersExpenditureNotes",groupMemberExpenditure.getDueDate());
                        editGroupMemberExpenditureIntent.putExtra("groupMembersExpenditureDate", groupMemberExpenditure.getNotes());
                        editGroupMemberExpenditureIntent.putExtra("groupMembersExpenditureParseId", groupMemberExpenditure.getParseId());
                        startActivity(editGroupMemberExpenditureIntent);
                        getActivity().finish();
                    }


                });

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                mAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(String error) {
                Log.d(TAG, "onFailure: " + error);
            }
        });



        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.member_expenditure, menu);

        MenuItem menuItem = menu.findItem(R.id.member_expenditure_search);

        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setBackgroundColor(Color.WHITE);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.BLACK);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.GRAY);

        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<GroupMemberExpenditure> newList = new ArrayList<>();
        for (GroupMemberExpenditure groupMemberExpenditures : groupMemberExpenditures){
            String category = groupMemberExpenditures.getCategory().toLowerCase();
            String notes = groupMemberExpenditures.getNotes().toLowerCase();
            String memberName = groupMemberExpenditures.getMemberUserName().toLowerCase();
            String date = groupMemberExpenditures.getDueDate().toLowerCase();
            if (category.contains(newText)){
                newList.add(groupMemberExpenditures);
            }else if (notes.contains(newText)){
                newList.add(groupMemberExpenditures);
            }else if (memberName.contains(newText)){
                newList.add(groupMemberExpenditures);
            }else if (date.contains(newText)){
                newList.add(groupMemberExpenditures);
            }
        }
        mAdapter.setFilter(newList);
        return true;
    }
}
