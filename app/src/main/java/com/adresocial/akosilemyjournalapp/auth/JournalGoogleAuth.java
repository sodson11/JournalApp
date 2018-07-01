package com.adresocial.akosilemyjournalapp.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class JournalGoogleAuth {
    private static final String TAG = JournalGoogleAuth.class.getSimpleName();
    private GoogleSignInClient googleSignInClient;

    public JournalGoogleAuth(Context context){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, gso);

    }

    public Intent signIn(){
        return googleSignInClient.getSignInIntent();
    }

    public void signOut(final Activity activity){
        googleSignInClient.signOut()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity.getBaseContext(), "You are signed out.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public GoogleSignInAccount signInAccount(Context context){
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public GoogleSignInAccount handleSigninResult(Intent data){
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            return task.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w(TAG, "signin result failed : " + e.getMessage());
            return null;
        }
    }
}
