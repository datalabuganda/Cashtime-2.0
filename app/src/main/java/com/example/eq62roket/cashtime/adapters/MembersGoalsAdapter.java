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
import com.example.eq62roket.cashtime.Interfaces.OnReturnedMemberSavingsSumListener;
import com.example.eq62roket.cashtime.Models.MembersGoals;
import com.example.eq62roket.cashtime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by eq62roket on 2/28/18.
 * modified by etwin
 */

public class MembersGoalsAdapter extends RecyclerView.Adapter<MembersGoalsAdapter.MembersGoalsViewHolder> {

    public interface OnMemberGoalClickListener {
        void onMemberGoalClick(MembersGoals membersGoals);
    }

    private static String TAG = "MembersGoalsAdapter";
    private List<MembersGoals> membersGoalsList;
    private OnMemberGoalClickListener mOnMemberGoalClickListener;
    private Context mContext;
    private ParseHelper mParseHelper;

    public MembersGoalsAdapter(Context context, List<MembersGoals> membersGoalsList, OnMemberGoalClickListener onMemberGoalClickListener) {
        this.membersGoalsList = membersGoalsList;
        mOnMemberGoalClickListener = onMemberGoalClickListener;
        mContext = context;
        mParseHelper = new ParseHelper(mContext);
    }

    public class MembersGoalsViewHolder extends RecyclerView.ViewHolder {
        public TextView memberName, memberGoalDueDate, memberGoalAmount, memberGoalName;
        ProgressBar progressBar;
        ImageView imgFailed, imgCompleted;

        public MembersGoalsViewHolder(View view) {
            super(view);
            memberName = (TextView) view.findViewById(R.id.memberName);
            memberGoalDueDate = (TextView) view.findViewById(R.id.memberGoalDueDate);
            memberGoalAmount = (TextView) view.findViewById(R.id.memberGoalAmount);
            memberGoalName = (TextView) view.findViewById(R.id.memberGoalName);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            imgFailed = (ImageView) view.findViewById(R.id.imgFailed);
            imgCompleted = (ImageView) view.findViewById(R.id.imgCompleted);
        }

        public void bind(final MembersGoals membersGoal, final OnMemberGoalClickListener memberGoalClickListener){

            String dateToday = new PeriodHelper().getDateToday();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            try {
                Date todayZDate = simpleDateFormat.parse(dateToday);
                Date goalEndDate = simpleDateFormat.parse(membersGoal.getMemberGoalDueDate());

                if (todayZDate.after(goalEndDate) &&
                        membersGoal.getMemberGoalStatus().equals("incomplete")){
                    MembersGoals failedMembersGoal = membersGoal;
                    failedMembersGoal.setCompleteDate(dateToday);
                    failedMembersGoal.setMemberGoalStatus("failed");
                    mParseHelper.updateMemberGoalCompleteStatusInParseDb(failedMembersGoal);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            memberName.setText(membersGoal.getMemberName());
            memberGoalDueDate.setText(membersGoal.getMemberGoalDueDate());
            memberGoalAmount.setText(new CashTimeUtils().currencyFormatter(membersGoal.getMemberGoalAmount()));
            memberGoalName.setText(membersGoal.getMemberGoalName());

            mParseHelper.getTotalMemberSavingsFromParseDb(membersGoal, new OnReturnedMemberSavingsSumListener() {
                @Override
                public void onResponse(int memberGoalTotalSavings) {
                    float startAmount = 0;
                    float goalAmount = (float)Integer.valueOf(membersGoal.getMemberGoalAmount());
                    float goalSaving = startAmount + memberGoalTotalSavings;
                    int progressAmount = (int)  ((goalSaving / goalAmount) * 100);
                    progressBar.setProgress(progressAmount);

                    if (membersGoal.getMemberGoalStatus().equals("completed")){
                        progressBar.setVisibility(View.GONE);
                        imgCompleted.setVisibility(View.VISIBLE);
                    }else if (membersGoal.getMemberGoalStatus().equals("failed")){
                        progressBar.setVisibility(View.GONE);
                        imgFailed.setVisibility(View.VISIBLE);
                    }

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            memberGoalClickListener.onMemberGoalClick(membersGoal);
                        }
                    });


                }

                @Override
                public void onFailure(String error) {

                }
            });
        }
    }

    @Override
    public MembersGoalsAdapter.MembersGoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_goals_row, parent, false);

        return new MembersGoalsAdapter.MembersGoalsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MembersGoalsAdapter.MembersGoalsViewHolder holder, int position) {
        holder.bind(membersGoalsList.get(position), mOnMemberGoalClickListener);
    }

    @Override
    public int getItemCount() {
        return membersGoalsList.size();
    }

    public void setFilter(ArrayList<MembersGoals> newList) {
        membersGoalsList = new ArrayList<>();
        membersGoalsList.addAll(newList);
        notifyDataSetChanged();
    }
}
