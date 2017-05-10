package com.jatin.kisansms.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jatin.kisansms.R;
import com.jatin.kisansms.db.SmsDb;
import com.jatin.kisansms.adapter.SmsHistoryAdapter;
import com.jatin.kisansms.model.SMSDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by uw on 9/5/17.
 */

public class SMSHistoryFragment extends Fragment {

    ArrayList<SMSDetail> smsDetails = new ArrayList<>();
    SmsHistoryAdapter adapter;

    @BindView(R.id.btn_clear)
    Button btn_clear;

    @BindView(R.id.btn_refresh)
    Button btn_refresh;

    @BindView(R.id.history_view)
    RecyclerView history_view;

    public static SMSHistoryFragment newInstance(ArrayList<SMSDetail> smsDetails){
        SMSHistoryFragment fragment =  new SMSHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("smsDetail",smsDetails);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        smsDetails = getArguments().getParcelableArrayList("smsDetail");

        retrieveSMSHistory();
    }

    private void retrieveSMSHistory() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = SmsDb.getInstance(getActivity().getApplicationContext()).retrieveSMSDetail();
                if (cursor.moveToFirst()){
                    do{
                        SMSDetail detail = SMSDetail.detail(cursor);
                        smsDetails.add(detail);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        }).start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,view);
        adapter = new SmsHistoryAdapter(smsDetails);
        history_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        history_view.setLayoutManager(mLayoutManager);
        history_view.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        history_view.setItemAnimator(new DefaultItemAnimator());
        history_view.setAdapter(adapter);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsDb db = SmsDb.getInstance(getActivity().getApplicationContext());
                db.deleteSMSDetail();
                smsDetails.clear();
                adapter.notifyDataSetChanged();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrieveSMSHistory();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}
