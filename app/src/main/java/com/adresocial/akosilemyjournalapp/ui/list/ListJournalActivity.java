package com.adresocial.akosilemyjournalapp.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.adresocial.akosilemyjournalapp.R;

import com.adresocial.akosilemyjournalapp.auth.JournalGoogleAuth;
import com.adresocial.akosilemyjournalapp.data.Journal;
import com.adresocial.akosilemyjournalapp.ui.AddJournalActivity;

import com.adresocial.akosilemyjournalapp.ui.ReadJournalActivity;
import com.adresocial.akosilemyjournalapp.viewmodel.JournalViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class ListJournalActivity extends AppCompatActivity
        implements JournalAdapter.ItemClickListener, View.OnClickListener {

    private static final String TAG = ListJournalActivity.class.getSimpleName();
    public static final String EXTRA_GOOGLE_SIGNIN_ACCOUNT = "gsa";

    private GoogleSignInAccount googleSignInAccount;

    private RecyclerView recyclerView;
    private JournalAdapter journalAdapter;

    private JournalGoogleAuth journalGoogleAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_journal);

        journalGoogleAuth = new JournalGoogleAuth(this);

        Intent intent = getIntent();
        googleSignInAccount = intent.getParcelableExtra(EXTRA_GOOGLE_SIGNIN_ACCOUNT);
        setTitle(googleSignInAccount.getGivenName());
        findViewById(R.id.add_journal_fab).setOnClickListener(this);

        recyclerView = findViewById(R.id.journal_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journalAdapter = new JournalAdapter(this, this);
        recyclerView.setAdapter(journalAdapter);

        DividerItemDecoration decoration =
                new DividerItemDecoration(getApplicationContext(), VERTICAL);

        recyclerView.addItemDecoration(decoration);

        final JournalViewModel journalViewModel = ViewModelProviders.of(this)
                .get(JournalViewModel.class);

        // I wish i could show the progress bar here, without using AsyncTask.
        // If it is possible.
        // ProgressBar progressBar = findViewById(R.id.progress_bar);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                journalViewModel.getListOfJournalEntries(googleSignInAccount.getDisplayName())
                        .observe(ListJournalActivity.this, new Observer<List<Journal>>() {
                            @Override
                            public void onChanged(@Nullable List<Journal> journals) {
                                journalAdapter.setJournals(journals);
                            }
                        });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_journal_fab:
                addJournal();
        }
    }

    private void addJournal(){
        Intent intent = new Intent(ListJournalActivity.this, AddJournalActivity.class);
        intent.putExtra(AddJournalActivity.EXTRA_GOOGLE_SIGNIN_ACCOUNT, googleSignInAccount);
        startActivity(intent);
    }

    @Override
    public void onItemClickListener(String docId, String title, String entry, String mood) {
        Intent intent = new Intent(ListJournalActivity.this, ReadJournalActivity.class);
        intent.putExtra(ReadJournalActivity.EXTRA_JOURANL_TEXT, title);
        intent.putExtra(ReadJournalActivity.EXTRA_JOURANL_ENTRY, entry);
        intent.putExtra(ReadJournalActivity.EXTRA_JOURANL_DOC_ID, docId);
        intent.putExtra(ReadJournalActivity.EXTRA_GOOGLE_SIGNIN_ACCOUNT, googleSignInAccount);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        journalGoogleAuth.signOut(this);
        finish();
        return super.onOptionsItemSelected(item);
    }
}
