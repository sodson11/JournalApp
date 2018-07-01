package com.adresocial.akosilemyjournalapp.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.adresocial.akosilemyjournalapp.data.FirebaseQueryLiveData;
import com.adresocial.akosilemyjournalapp.data.Journal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JournalViewModel extends ViewModel {

    private static final DatabaseReference dataRef =
            FirebaseDatabase.getInstance().getReference();

    private List<Journal> journals = new ArrayList<>();

    public LiveData<DataSnapshot> getLiveJournalEtry(String docId, String dataBase){
        FirebaseQueryLiveData journalLiveData = new FirebaseQueryLiveData(dataRef.child(dataBase)
                .child(docId));
        return journalLiveData;
    }

    @NonNull
    public LiveData<List<Journal>> getListOfJournalEntries(String dataBase){
        FirebaseQueryLiveData journalsLiveData = new FirebaseQueryLiveData(dataRef.child(dataBase).orderByChild("time"));
        return Transformations.map(journalsLiveData, new Deserializer());
    }

    private class Deserializer implements Function<DataSnapshot, List<Journal>> {
        @Override
        public List<Journal> apply(DataSnapshot dataSnapshot) {
            journals.clear();
            for(DataSnapshot snap : dataSnapshot.getChildren()){
                Journal journal = snap.getValue(Journal.class);
                journal.setId(snap.getKey());
                journals.add(journal);
            }
            return journals;
        }
    }
}
