package com.example.mg.todo.UI.NoteFragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.mg.todo.BuildConfig
import com.example.mg.todo.R
import com.example.mg.todo.UI.NotesActivity.NotesViewModel
import com.example.mg.todo.UI.NotesActivity.UpdateResult
import com.example.mg.todo.data.model.NoteModel
import com.example.mg.todo.utils.BitmapUtil
import kotlinx.android.synthetic.main.custom_dialog.*
import kotlinx.android.synthetic.main.image_viewer.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class NoteFragment : DialogFragment(), View.OnClickListener {
    private var position = -1
    private var mBitmap: Bitmap? = null
    private var mNote: NoteModel? = null
    private var mFileLocation: String? = null

    private val notesViewModel: NotesViewModel by lazy { ViewModelProviders.of(requireActivity()).get(NotesViewModel::class.java) }

    private val glide: RequestManager by lazy { Glide.with(requireContext()) }

    companion object {
        private const val REQUEST_CODE = 19
    }

    fun setModel(position: Int, model: NoteModel?) {
        this.mNote = model
        this.position = position
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.dialog_slide_animation)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(R.style.dialog_slide_animation)

        btnDone.setOnClickListener(this)
        btnAddImage.setOnClickListener(this)
        btnCloseDialog.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        imageNote.setOnClickListener(this)

        mNote?.let { noteModel ->
            btnDelete.visibility = View.VISIBLE
            noteModel.image?.let {
                val bytes = BitmapUtil.decodeImage(it)
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                updateImage(2f)
            }

            editTextTitle.setText(noteModel.text)
            editTextDescription.setText(noteModel.description)
            tvTitle.text = resources.getString(R.string.update_note)

        } ?: run {
            btnDelete.visibility = View.GONE
            mNote = NoteModel()
        }

    }

    override fun onDetach() {
        super.onDetach()
        mBitmap?.recycle()
        dismiss()
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnDone) {

            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()

            if (title.isBlank()) {
                editTextTitle.error = "required"
                editTextTitle.requestFocus()
                return
            }
            if (description.isBlank()) {
                editTextDescription.error = "required"
                editTextDescription.requestFocus()
                return
            }
            mNote?.let { note: NoteModel ->
                note.text = title
                note.description = description

                mBitmap?.let {
                    note.image = BitmapUtil.encodedImage(it)
                } ?: kotlin.run {
                    note.image = null
                }

                if (position != -1) {
                    note.date = String.format(
                            "Edited on: %s",
                            SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                                    Locale.getDefault()).format(Date()))

                    notesViewModel.updateNote(note)
                } else {
                    note.date = SimpleDateFormat("EEE, MMM d, ''yy hh:mm aaa",
                            Locale.getDefault()).format(Date())
                    notesViewModel.addNote(note)
                }
            }

            dialog?.dismiss()

        } else if (view.id == R.id.imageNote) {
            val popup = PopupMenu(requireContext(), view)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            popup.setOnMenuItemClickListener { item: MenuItem ->
                if (item.itemId == R.id.delete) {
                    imageNote.visibility = View.GONE
                    imageNote.setImageResource(0)
                    mBitmap = null
                } else if (item.itemId == R.id.view) {
                    val builder = Dialog(requireContext())
                    builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    builder.setContentView(R.layout.image_viewer)
                    mBitmap?.let { builder.image_preview.setImageBitmap(BitmapUtil.resize(it, 2f)) }
                    builder.show()
                }
                true
            }
            popup.show()

        } else if (view.id == R.id.btnCloseDialog) dismiss()
        else if (view.id == R.id.btnDelete) {
            mNote?.let {
                notesViewModel.removeNote(it)
                dismiss()
            }
        } else {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (i.resolveActivity(requireActivity().packageManager) != null) {
                var file: File? = null
                try {
                    file = BitmapUtil.createTempImageFile(requireActivity())
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                file?.let {
                    mFileLocation = BitmapUtil.mCurrentPhotoPath
                    val uri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID, it)
                    i.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(i, REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mBitmap = BitmapUtil.resamplePic(mFileLocation)
            updateImage(1f)
        }
    }

    private fun updateImage(factor: Float) {
        imageNote.visibility = View.VISIBLE

        mBitmap?.let {
            val resize = BitmapUtil.resize(it, factor)
            glide.asBitmap()
                    .load(resize)
                    .into(imageNote!!)
        }
    }
}