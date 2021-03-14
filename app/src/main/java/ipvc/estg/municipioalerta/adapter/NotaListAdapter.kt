package ipvc.estg.municipioalerta.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.*
import ipvc.estg.municipioalerta.R
import ipvc.estg.municipioalerta.entities.Nota

class NotaListAdapter : androidx.recyclerview.widget.ListAdapter<Nota, NotaListAdapter.NoteViewHolder>(NotesComparator()) {
    private var notes = emptyList<Nota>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = getItem(position)
        holder.bindTitle(current.titulo)
        holder.bindDescription(current.descricao)
    }

    internal fun setNotes(notes: List<Nota>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteItemViewTitle: TextView = itemView.findViewById(R.id.recyclerTitulo)
        private val noteItemViewDesc: TextView = itemView.findViewById(R.id.recyclerDescricao)

        fun bindTitle(title: String?) {
            noteItemViewTitle.text = title
        }
        fun bindDescription(desc: String?) {
            noteItemViewDesc.text = desc
        }

        companion object {
            fun create(parent: ViewGroup): NoteViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerline, parent, false)
                return NoteViewHolder(view)
            }
        }
    }

    class NotesComparator : DiffUtil.ItemCallback<Nota>() {
        override fun areItemsTheSame(oldItem: Nota, newItem: Nota): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Nota, newItem: Nota): Boolean {
            return oldItem.id == newItem.id
        }
    }
}