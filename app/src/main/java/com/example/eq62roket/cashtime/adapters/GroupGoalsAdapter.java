package com.example.eq62roket.cashtime.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.eq62roket.cashtime.Helper.CashTimeUtils;
import com.example.eq62roket.cashtime.Helper.ParseHelper;
import com.example.eq62roket.cashtime.Helper.PeriodHelper;
import com.example.eq62roket.cashtime.Interfaces.OnReturnedGroupSavingsSumListener;
import com.example.eq62roket.cashtime.Models.GroupGoals;
import com.example.eq62roket.cashtime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by eq62roket on 2/28/18.
 */

public class GroupGoalsAdapter extends RecyclerView.Adapter<GroupGoalsAdapter.GroupGoalsViewHolder> {

    public interface OnGoalClickListener {
        void onGoalClick(GroupGoals groupGoals);
    }

    private List<GroupGoals> groupGoalsList;
    private final OnGoalClickListener mOnGoalClickListener;
    private Context mContext;
    private ParseHelper mParseHelper;

    public class GroupGoalsViewHolder extends RecyclerView.ViewHolder {
        public TextView goalName, date, amount, groupName;
        ProgressBar progressBar;
        ImageView imgFailed, imgCompleted;

        public GroupGoalsViewHolder(View view) {
            super(view);
            goalName = (TextView) view.findViewById(R.id.goalName);
            date = (TextView) view.findViewById(R.id.endDate);
            amount = (TextView) view.findViewById(R.id.amount);
            groupName = (TextView) view.findViewById(R.id.groupName);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            imgFailed = (ImageView) view.findViewById(R.id.imgFailed);
            imgCompleted = (ImageView) view.findViewById(R.id.imgCompleted);
        }

        public void bind(final GroupGoals groupGoal, final OnGoalClickListener onGoalClickListener){

            String dateToday = new PeriodHelper().getDateToday();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                Date todayZDate = simpleDateFormat.parse(dateToday);
                Date goalEndDate = simpleDateFormat.parse(groupGoal.getDueDate());

                if (todayZDate.after(goalEndDate) &&
                        groupGoal.getGroupGoalStatus().equals("incomplete")){
                    groupGoal.setCompletedDate(dateToday);
                    groupGoal.setGroupGoalStatus("failed");
                    mParseHelper.updateGroupGoalCompleteStatusInParseDb(groupGoal);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            goalName.setText(groupGoal.getName());
            amount.setText(new CashTimeUtils().currencyFormatter(groupGoal.getAmount()));
            date.setText(groupGoal.getDueDate());
            groupName.setText(groupGoal.getGroupName());

            mParseHelper.getTotalGroupSavingsFromParseDb(groupGoal, new OnReturnedGroupSavingsSumListener() {
                @Override
                public void onResponse(int groupGoalTotalSavings) {
                    float startAmount = 0;
                    float goalAmount = (float)Integer.valueOf(groupGoal.getAmount());
                    float goalSaving = startAmount + groupGoalTotalSavings;
                    int progressAmount = (int)  ((goalSaving / goalAmount) * 100);

                    if (groupGoal.getGroupGoalStatus().equals("completed")){
                        progressBar.setVisibility(View.GONE);
                        imgCompleted.setVisibility(View.VISIBLE);
                    }else if (groupGoal.getGroupGoalStatus().equals("failed")){
                        progressBar.setVisibility(View.GONE);
                        imgFailed.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(progressAmount);
                }

                @Override
                public void onFailure(String error) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGoalClickListener.onGoalClick(groupGoal);
                }
            });
        }
    }


    public GroupGoalsAdapter(Context context, List<GroupGoals> groupGoalsList, OnGoalClickListener listener) {
        mContext = context;
        this.groupGoalsList = groupGoalsList;
        mOnGoalClickListener = listener;
        mParseHelper = new ParseHelper(mContext);

    }

    @Override
    public GroupGoalsAdapter.GroupGoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_goals_row, parent, false);

        return new GroupGoalsAdapter.GroupGoalsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupGoalsAdapter.GroupGoalsViewHolder holder, int position) {
        holder.bind(groupGoalsList.get(position), mOnGoalClickListener);
    }

    @Override
    public int getItemCount() {
        if (groupGoalsList.size() > 0){
            return groupGoalsList.size();
        }
        return 0;
    }

    public void setFilter(ArrayList<GroupGoals> newList) {
        groupGoalsList = new ArrayList<>();
        groupGoalsList.addAll(newList);
        notifyDataSetChanged();
    }
}