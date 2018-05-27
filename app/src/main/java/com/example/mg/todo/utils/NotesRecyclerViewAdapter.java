package com.example.mg.todo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mg.todo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesRecyclerViewAdapter
        extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private OnItemLongClickListener listener;
    private List<String> mValues;

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public void setAll(List<String> values) {
        mValues = values;
    }


    public NotesRecyclerViewAdapter(Context context, List<String> set) {
        listener = (OnItemLongClickListener) context;
        mValues = set;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_content,
                                parent,
                                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return listener.onItemLongClicked(holder.getAdapterPosition());
            }
        });

        holder.textTitle.setText(mValues.get(position));
        holder.textDetails.setText("");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.text_datails)
        TextView textDetails;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}