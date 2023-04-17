package com.example.notepad_x.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad_x.R
import com.example.notepad_x.roomdb.NotesEntities

class NotesAdapter(private val mContext: Context): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
     val allNotes = ArrayList<NotesEntities>()

    inner class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val note: TextView = itemView.findViewById(R.id.note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val note = allNotes[position]
        holder.title.text = note.title
        holder.note.text = note.note
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList : List<NotesEntities>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
    fun getNote(position: Int): NotesEntities {
        return NotesEntities(allNotes[position].title, allNotes[position].note)
    }
}