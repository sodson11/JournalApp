package com.adresocial.akosilemyjournalapp.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.adresocial.akosilemyjournalapp.R;
import com.adresocial.akosilemyjournalapp.data.Journal;
import com.adresocial.akosilemyjournalapp.viewmodel.JournalViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;

public class ReadJournalActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String EXTRA_GOOGLE_SIGNIN_ACCOUNT = "gsa";
    public static final String EXTRA_JOURANL_TEXT = "extraJournalTitlae";
    public static final String EXTRA_JOURANL_ENTRY = "extraJournalEntry";
    public static final String EXTRA_JOURANL_DOC_ID = "extraJournalDocId";

    private GoogleSignInAccount googleSignInAccount;
    TextView tvJournalEntry;
    String docId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_journal);
        Intent intent = getIntent();



        googleSignInAccount = intent.getParcelableExtra(EXTRA_GOOGLE_SIGNIN_ACCOUNT);
        docId = intent.getStringExtra(EXTRA_JOURANL_DOC_ID);

        findViewById(R.id.journal_fab).setOnClickListener(this);

        JournalViewModel journalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);
        journalViewModel.getLiveJournalEtry(docId, googleSignInAccount.getDisplayName()).observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                populateUI(dataSnapshot.getValue(Journal.class));
            }
        });
    }

    public void populateUI(Journal journal){
        tvJournalEntry = findViewById(R.id.journal_entry);
        tvJournalEntry.setText(journal.getEntry());
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(journal.getMood()));
        setTitle(journal.getTitle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.journal_fab:
                addJournal();
        }
    }

    private void addJournal(){
        Intent intent = new Intent(ReadJournalActivity.this, AddJournalActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_GOOGLE_SIGNIN_ACCOUNT, googleSignInAccount);
        intent.putExtra(AddJournalActivity.EXTRA_JOURANL_DOC_ID, docId);
        startActivity(intent);
    }
}
