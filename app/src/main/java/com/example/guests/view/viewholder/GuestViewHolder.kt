package com.example.guests.view.viewholder

import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.guests.R
import com.example.guests.databinding.RowGuestBinding
import com.example.guests.model.GuestModel
import com.example.guests.view.listener.OnGuestListener

class GuestViewHolder(private val bind: RowGuestBinding, private val listener: OnGuestListener) :
    RecyclerView.ViewHolder(bind.root) {
    fun bind(guest: GuestModel) {
        bind.textName.text = guest.name
        bind.textName.setOnClickListener {
            listener.onClick(guest.id)
        }
        bind.textName.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle("Remover convidado")
                .setMessage("Tem certeza que deseja remover esse convidado?")
                .setPositiveButton("sim") { dialog, which ->
                    listener.onDelete(guest.id)
                }
                .setNegativeButton("Não", null)
                .create()
                .show()
            true
        }
    }
}