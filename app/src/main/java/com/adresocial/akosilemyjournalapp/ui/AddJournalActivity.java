
package com.adresocial.akosilemyjournalapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.adresocial.akosilemyjournalapp.R;
import com.adresocial.akosilemyjournalapp.data.Journal;
import com.adresocial.akosilemyjournalapp.viewmodel.JournalViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddJournalActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG = AddJournalActivity.class.getSimpleName();


    private static final String DATE_FORMAT = "E, MMMM d yyyy, HH:mm";
    private static final String HEX_COLOR_FORMAT = "#%06X";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public static final String EXTRA_GOOGLE_SIGNIN_ACCOUNT = "gso";
    public static final String EXTRA_JOURANL_DOC_ID = "extraJournalDocumentId";


    private EditText etJournalTitle, etJournalEntry;
    private GoogleSignInAccount gsa;
    private Button mButton;
    private RadioButton radioButton;
    private String journalMood = null;


    private String docId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        Intent intent = getIntent();

        if (intent != null) {
            gsa = intent.getParcelableExtra(EXTRA_GOOGLE_SIGNIN_ACCOUNT);
            if (intent.hasExtra(EXTRA_JOURANL_DOC_ID)) {

                mButton = findViewById(R.id.save_button);
                docId = intent.getStringExtra(EXTRA_JOURANL_DOC_ID);

                mButton.setText(R.string.btn_update_journal);
                setupViewModel(docId, gsa);
            }
        }

        etJournalTitle = findViewById(R.id.edit_journal_title);
        etJournalEntry = findViewById(R.id.edit_journal_entry);
        findViewById(R.id.save_button).setOnClickListener(this);
        findViewById(R.id.radio1).setOnClickListener(this);
        findViewById(R.id.radio2).setOnClickListener(this);
        findViewById(R.id.radio3).setOnClickListener(this);
    }

    private void setupViewModel(String docId, GoogleSignInAccount gsa){
        JournalViewModel journalViewModel =
                ViewModelProviders.of(this).get(JournalViewModel.class);

        journalViewModel.getLiveJournalEtry(docId, gsa.getDisplayName())
                .observe(this, new Observer<DataSnapshot>() {
            @Override
            public void onChanged(@Nullable DataSnapshot dataSnapshot) {
                populateUI(dataSnapshot.getValue(Journal.class));
            }
        });
    }

    public void populateUI(Journal journal){
        etJournalTitle = findViewById(R.id.edit_journal_title);
        etJournalEntry = findViewById(R.id.edit_journal_entry);
        etJournalTitle.setText(journal.getTitle());
        etJournalEntry.setText(journal.getEntry());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.save_button:
                String title, entry;
                title = etJournalTitle.getText().toString();
                entry = etJournalEntry.getText().toString();

                if (!title.matches("") && !entry.matches("") && journalMood != null){
                    Date date = new Date();
                    Long unixTime = System.currentTimeMillis()/10000 * -1;

                    writeJournal(
                            gsa.getDisplayName(),
                            title,
                            entry,
                            dateFormat.format(date),
                            unixTime,
                            getIntent(),
                            journalMood
                    );
                }
                finish();

                break;

            case R.id.radio1:
                radioButton = findViewById(R.id.radio1);
                radioButton.isChecked();
                journalMood = toHex(getResources().getColor(R.color.colourOne));

                break;

            case R.id.radio2:
                radioButton = findViewById(R.id.radio2);
                radioButton.isChecked();
                journalMood = toHex(getResources().getColor(R.color.colourTwo));
                break;

            case R.id.radio3:
                radioButton = findViewById(R.id.radio3);
                radioButton.isChecked();
                journalMood = toHex(getResources().getColor(R.color.colourThree));

                break;
        }
    }

    private String toHex(int colorId){
        return String.format(HEX_COLOR_FORMAT, (0xFFFFFF & colorId));
    }

    private void writeJournal(String id, String title, String entry, String dateCreated, long time, Intent intent, String mood) {
        DatabaseReference mDatabase;
        Journal journal = new Journal(title, entry, dateCreated, time, mood);
        if (intent.hasExtra(EXTRA_JOURANL_DOC_ID)) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(id).child(docId).setValue(journal);
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(id).push().setValue(journal);
        }
    }
}
