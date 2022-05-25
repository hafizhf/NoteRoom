package andlima.hafizhfy.noteroom.adapter

import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.local.room.notetable.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class AdapterNote(
    private var noteList: List<Note>,
    private var onClick: (Note) -> Unit
) : RecyclerView.Adapter<AdapterNote.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNote.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterNote.ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_item_title.text = noteList[position].title
            tv_item_short_desc.text = noteList[position].description?.take(20)
            tv_item_time_created.text = noteList[position].createdAt

            item.setOnClickListener {
                onClick(noteList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

}