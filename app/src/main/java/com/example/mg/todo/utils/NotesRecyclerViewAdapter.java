package com.example.mg.todo.utils;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NotesActivity.DI.INoteActivityScope;
import com.example.mg.todo.ui.NotesActivity.NotesActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@INoteActivityScope
public class NotesRecyclerViewAdapter
        extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {

    private OnItemLongClickListener itemLongClickListener;
    private OnItemClickListener onItemClickListener;
    private List<NoteModel> mValues;
    private final RequestManager glide;

    @Inject
    NotesRecyclerViewAdapter(NotesActivity context, RequestManager glide) {
        itemLongClickListener = context;
        onItemClickListener = context;
        this.glide = glide;
    }

    public void setAll(List<NoteModel> values) {
        mValues = values;
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
                holder.radioButton.setVisibility(
                        holder.radioButton.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                holder.radioButton.setChecked(!holder.radioButton.isChecked());
                doBounceAnimation(holder.itemView);
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
        if (mValues.get(position).getImage() == null) holder.imageView.setVisibility(View.GONE);
        else {
            holder.imageView.setVisibility(View.VISIBLE);
            glide.load(BitmapUtil.decodeImage(mValues.get(position).getImage()))
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

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public interface OnItemClickListener {
        void onItemClicked(int position);
    }

    private void doBounceAnimation(View targetView) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 25, 0);
        animator.setStartDelay(0);
        animator.setDuration(300);
        animator.start();
    }

}