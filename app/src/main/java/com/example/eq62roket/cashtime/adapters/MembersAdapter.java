package com.example.eq62roket.cashtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eq62roket.cashtime.Models.User;
import com.example.eq62roket.cashtime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eq62roket on 2/28/18.
 * modified by etwin on 3/22/18
 */


public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder>{

    public interface OnGroupMemberClickListener {
        void onGroupMemberClick(User groupMemberUser);
    }

    private List<User> mGroupMemberUsers;
    private final OnGroupMemberClickListener mOnGroupMemberClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView memberName, memberPhoneNumber, memberLocation, memberBusiness, memberGender,
            memberEducationLevel, memberNationality, memberHousehold;

        public MyViewHolder(View view) {
            super(view);
            memberName = (TextView) view.findViewById(R.id.memberName);
            memberPhoneNumber = (TextView) view.findViewById(R.id. memberPhoneNumber);

        }

        public void bind(final User groupMemberUser, final OnGroupMemberClickListener onGroupMemberClickListener){
            memberName.setText(groupMemberUser.getUserName());
            memberPhoneNumber.setText(groupMemberUser.getPhoneNumber());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onGroupMemberClickListener.onGroupMemberClick(groupMemberUser);
                }
            });
        }

    }


    public MembersAdapter(List<User> groupMemberUsers, OnGroupMemberClickListener onGroupMemberClickListener) {
        this.mGroupMemberUsers = groupMemberUsers;
        this.mOnGroupMemberClickListener = onGroupMemberClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       holder.bind(mGroupMemberUsers.get(position), mOnGroupMemberClickListener);
    }

    @Override
    public int getItemCount() {
        if (mGroupMemberUsers.size() > 0){
            return mGroupMemberUsers.size();
        }
        return 0;
    }

    public void setFilter(ArrayList<User> groupMemberUsers){
        mGroupMemberUsers = new ArrayList<>();
        mGroupMemberUsers.addAll(groupMemberUsers);
        notifyDataSetChanged();
    }
}

