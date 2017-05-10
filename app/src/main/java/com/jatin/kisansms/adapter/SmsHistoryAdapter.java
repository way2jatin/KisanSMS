package com.jatin.kisansms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jatin.kisansms.R;
import com.jatin.kisansms.model.SMSDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jatinjha on 09/05/17.
 */

public class SmsHistoryAdapter extends RecyclerView.Adapter<SmsHistoryAdapter.ViewHolder> {

    ArrayList<SMSDetail> smsDetails = new ArrayList<>();

    public SmsHistoryAdapter(ArrayList<SMSDetail> smsDetails) {
        this.smsDetails = smsDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_row,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SMSDetail detail = smsDetails.get(position);
        holder.txt_otp.setText(detail.getOtp());
        holder.txt_sentFrom.setText(detail.getSentFrom());
        holder.txt_time.setText(detail.getTime());
    }

    @Override
    public int getItemCount() {
        return smsDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_sentfrom)
        TextView txt_sentFrom;
        @BindView(R.id.txt_time)
        TextView txt_time;
        @BindView(R.id.txt_otp)
        TextView txt_otp;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
