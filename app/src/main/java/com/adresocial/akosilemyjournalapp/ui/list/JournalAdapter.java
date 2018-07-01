package com.adresocial.akosilemyjournalapp.ui.list;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adresocial.akosilemyjournalapp.R;
import com.adresocial.akosilemyjournalapp.data.Journal;

import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder>{

    private static final String TAG = JournalAdapter.class.getSimpleName();

    private Context context;
    private final ItemClickListener mItemClickListener;
    private List<Journal> mJournals;

    public JournalAdapter (Context context, ItemClickListener itemClickListener){
        this.context = context;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context)
                .inflate(R.layout.journal_layout, parent, false);

        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journalEntry = this.mJournals.get(position);
        String jTitle, jCreated;

        jTitle = journalEntry.getTitle();
        jCreated = journalEntry.getCreated();

        holder.itemView.findViewById(R.id.parent_view_holder)
                .setBackgroundColor(Color.parseColor(journalEntry.getMood()));


        //Log.d(TAG, "Color is: " + Color.);

        holder.tvJournalTitle.setText(jTitle);
        holder.tvJournalEntry.setText(jCreated);
    }

    @Override
    public int getItemCount() {
        if (mJournals == null) return 0;
        else return mJournals.size();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvJournalTitle, tvJournalEntry;

        public JournalViewHolder(View itemView) {
            super(itemView);
            tvJournalTitle = itemView.findViewById(R.id.journal_title);
            tvJournalEntry = itemView.findViewById(R.id.journal_updated_at);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String docId, title, entry, mood;
            docId = mJournals.get(getAdapterPosition()).getId();
            title = mJournals.get(getAdapterPosition()).getTitle();
            entry = mJournals.get(getAdapterPosition()).getEntry();
            mood = mJournals.get(getAdapterPosition()).getMood();

            mItemClickListener.onItemClickListener(docId, title, entry, mood);
        }
    }

    public interface ItemClickListener {
        void onItemClickListener(String docId, String title, String entry, String mood);
    }

    public List<Journal> getJournals(){
        return mJournals;
    }

    public void setJournals(List<Journal> journals){
        mJournals = journals;
        notifyDataSetChanged();
    }
}
