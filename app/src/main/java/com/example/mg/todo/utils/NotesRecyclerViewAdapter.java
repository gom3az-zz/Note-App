package com.example.mg.todo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mg.todo.R;
import com.example.mg.todo.data.model.DataModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesRecyclerViewAdapter
        extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {


    private OnItemLongClickListener listener;
    private List<DataModel> mValues;
    private Context mContext;

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(int position);
    }

    public void setAll(List<DataModel> values) {
        mValues = values;
    }


    public NotesRecyclerViewAdapter(Context context, List<DataModel> set) {
        listener = (OnItemLongClickListener) context;
        mValues = set;
        mContext = context;
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

        holder.textTitle.setText(String.format("%s\n%s",
                mValues.get(position).getText(),
                mValues.get(position).getDescription()));
        if (mValues.get(position).getImage() == null) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            byte[] imageAsBytes = Base64.decode(mValues.get(position).getImage().getBytes(), Base64.DEFAULT);

            Glide.with(mContext)
                    .load(imageAsBytes)
                    .into(holder.imageView);
            // holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}