package ipvc.estg.municipioalerta.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.*
import ipvc.estg.municipioalerta.AlterarNota
import ipvc.estg.municipioalerta.Notas
import ipvc.estg.municipioalerta.R
import ipvc.estg.municipioalerta.entities.Nota

const val TITULO="TITULO"
const val DESCRICAO="DESCRICAO"
const val ID="ID"

class NotaListAdapter internal constructor(
        context: Context, private val callbackInterface: Notas
): RecyclerView.Adapter<NotaListAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    interface CallbackInterface {
        fun passResultCallback(id: Int?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NoteViewHolder(itemView)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemViewTitle: TextView = itemView.findViewById(R.id.recyclerTitulo)
        val noteItemViewDesc: TextView = itemView.findViewById(R.id.recyclerDescricao)
        val edit: ImageButton = itemView.findViewById(R.id.recyclerEdit)
        val delete: ImageButton = itemView.findViewById(R.id.recyclerDelete)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notas[position]
        holder.noteItemViewTitle.text = current.titulo
        holder.noteItemViewDesc.text = current.descricao
        val id: Int? = current.id

        holder.delete.setOnClickListener {
            callbackInterface.delete(current.id)
        }

        holder.edit.setOnClickListener {
            val context = holder.noteItemViewTitle.context
            val titulo = holder.noteItemViewTitle.text.toString()
            val descricao = holder.noteItemViewDesc.text.toString()

            val intent = Intent(context, AlterarNota::class.java).apply {
                putExtra(TITULO, titulo)
                putExtra(DESCRICAO, descricao )
                putExtra( ID,id)
            }
            context.startActivity(intent)
        }
    }

    internal fun setNotas(nota: List<Nota>) {
        this.notas = nota
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size

//    class NotesComparator : DiffUtil.ItemCallback<Nota>() {
//        override fun areItemsTheSame(oldItem: Nota, newItem: Nota): Boolean {
//            return oldItem === newItem
//        }
//
//        override fun areContentsTheSame(oldItem: Nota, newItem: Nota): Boolean {
//            return oldItem.id == newItem.id
//        }
//    }
}