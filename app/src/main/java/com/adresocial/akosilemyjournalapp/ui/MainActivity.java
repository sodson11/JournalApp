package com.adresocial.akosilemyjournalapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adresocial.akosilemyjournalapp.R;
import com.adresocial.akosilemyjournalapp.auth.JournalGoogleAuth;
import com.adresocial.akosilemyjournalapp.ui.list.ListJournalActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private JournalGoogleAuth journalGoogleAuth;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        journalGoogleAuth = new JournalGoogleAuth(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount googleSignInAccount =
                journalGoogleAuth.signInAccount(this);
        if (googleSignInAccount != null) {
            startJournal(googleSignInAccount);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && journalGoogleAuth.signInAccount(this) != null){
            startJournal(journalGoogleAuth.handleSigninResult(data));
        }
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(journalGoogleAuth.signIn(), RC_SIGN_IN);
    }

    public void startJournal(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(MainActivity.this, ListJournalActivity.class);
        intent.putExtra(ListJournalActivity.EXTRA_GOOGLE_SIGNIN_ACCOUNT, googleSignInAccount);
        startActivity(intent);
    }
}


