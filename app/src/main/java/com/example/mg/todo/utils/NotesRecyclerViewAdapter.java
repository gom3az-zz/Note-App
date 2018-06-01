package com.example.mg.todo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesRecyclerViewAdapter
        extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {


    private OnItemLongClickListener itemLongClickListener;
    private OnItemClickListener onItemClickListener;
    private List<NoteModel> mValues;
    private Context mContext;

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    public NotesRecyclerViewAdapter(Context context, List<NoteModel> set) {
        itemLongClickListener = (OnItemLongClickListener) context;
        onItemClickListener = (OnItemClickListener) context;
        mValues = set;
        mContext = context;
    }

    public void setAll(List<NoteModel> values) {
        mValues = values;
        notifyDataSetChanged();
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
                if (holder.radioButton.getVisibility() == View.GONE)
                    holder.radioButton.setVisibility(View.VISIBLE);
                else holder.radioButton.setVisibility(View.GONE);

                holder.radioButton.setChecked(!holder.radioButton.isChecked());
                return itemLongClickListener.onItemLongClicked(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(holder.getAdapterPosition());
            }
        });

        holder.radioButton.setChecked(false);
        holder.radioButton.setVisibility(View.GONE);

        holder.textTitle.setText(String.format("%s\n%s\n\n\n%s",
                mValues.get(position).getText(),
                mValues.get(position).getDescription(),
                mValues.get(position).getmDate()));
        // hiding image view if the note doesn't have a image
        // setting visibility to visible again if the user updates a non having image note
        // because we already bound the view of this note to gone
        if (mValues.get(position).getImage() == null) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(BitmapUtil.decodeImage(mValues.get(position).getImage()))
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return mValues.size();
        } catch (NullPointerException ignored) {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.radio)
        RadioButton radioButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}