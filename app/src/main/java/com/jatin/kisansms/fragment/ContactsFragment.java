package com.jatin.kisansms.fragment;

import android.content.Intent;
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

import com.jatin.kisansms.R;
import com.jatin.kisansms.activity.ContactDetailActivity;
import com.jatin.kisansms.adapter.ContactsAdapter;
import com.jatin.kisansms.model.Contact;
import com.jatin.kisansms.util.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by uw on 9/5/17.
 */

public class ContactsFragment extends Fragment {

    ArrayList<Contact> contacts = new ArrayList<>();
    ContactsAdapter adapter;

    @BindView(R.id.contact_view)
    RecyclerView contactView;

    public static ContactsFragment newInstance(ArrayList<Contact> contacts){
        ContactsFragment fragment =  new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("contacts",contacts);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contacts = getArguments().getParcelableArrayList("contacts");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this,view);
        adapter = new ContactsAdapter(contacts);
        contactView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        contactView.setLayoutManager(mLayoutManager);
        contactView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        contactView.setItemAnimator(new DefaultItemAnimator());
        contactView.setAdapter(adapter);

        contactView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), contactView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Contact contact = contacts.get(position);

                startActivity(new Intent(getActivity().getApplicationContext(),ContactDetailActivity.class)
                        .putExtra("mobile_no",contact.getMobileNo())
                        .putExtra("otp",generateRandomOtp())
                        .putExtra("name",contact.getFirstName() + " " +contact.getLastName()));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        return view;
    }

    public String generateRandomOtp(){
        Random rand = new Random();
        return String.format("%04d", rand.nextInt(10000));
    }
}
