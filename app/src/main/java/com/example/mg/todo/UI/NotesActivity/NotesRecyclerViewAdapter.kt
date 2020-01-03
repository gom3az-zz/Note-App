package com.example.mg.todo.UI.NotesActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.mg.todo.R
import com.example.mg.todo.data.model.NoteModel
import com.example.mg.todo.utils.BitmapUtil
import kotlinx.android.synthetic.main.note_content.view.*

class NotesRecyclerViewAdapter(
        context: NotesActivity,
        glide: RequestManager,
        mValues: MutableList<NoteModel>
) : RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    private val glide: RequestManager
    private val onItemClickListener: OnItemClickListener
    private var mValues: MutableList<NoteModel>

    init {
        this.onItemClickListener = context
        this.glide = glide
        this.mValues = mValues
    }

    fun swapData(values: MutableList<NoteModel>) {
        mValues = values
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        mValues.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_content,
                        parent,
                        false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.unbind()
    }
    override fun getItemCount(): Int = mValues.size

    interface OnItemClickListener {
        fun onItemClicked(position: Int, model: NoteModel)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            itemView.setOnClickListener { onItemClickListener.onItemClicked(position, mValues[position]) }

            itemView.text_title.text = String.format("%s\n%s\n\n\n%s",
                    mValues[position].text,
                    mValues[position].description,
                    mValues[position].date)

            mValues[position].image?.let {
                itemView.imageView.visibility = View.VISIBLE
                glide.load(BitmapUtil.decodeImage(it))
                        .into(itemView.imageView)
            }
        }

        fun unbind() {
            itemView.imageView.visibility = View.GONE
        }
    }

}