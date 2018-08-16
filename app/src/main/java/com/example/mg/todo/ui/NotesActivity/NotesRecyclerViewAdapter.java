package com.example.mg.todo.ui.NotesActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.NoteModel;
import com.example.mg.todo.ui.NotesActivity.DI.INoteActivityScope;
import com.example.mg.todo.utils.BitmapUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

@INoteActivityScope
public class NotesRecyclerViewAdapter
        extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {


    private OnItemClickListener onItemClickListener;
    private List<NoteModel> mValues;
    private final RequestManager glide;

    @Inject
    NotesRecyclerViewAdapter(NotesActivity context, RequestManager glide) {
        onItemClickListener = context;
        this.glide = glide;
    }

    public void setData(List<NoteModel> values) {
        mValues = values;
    }


    void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
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

        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClicked(holder.getAdapterPosition()));

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.radio)
        RadioButton radioButton;
        @BindView(R.id.viewForeground)
        public
        LinearLayout viewForeground;
        @BindView(R.id.view_background)
        RelativeLayout viewBackground;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
}