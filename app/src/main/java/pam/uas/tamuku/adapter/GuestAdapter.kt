package pam.uas.tamuku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pam.uas.tamuku.databinding.ItemGuestBinding
import pam.uas.tamuku.model.Guest

class GuestAdapter(
    private val context: Context,
    private val guestList: MutableList<Guest>,
    private val onEditClick: (Guest) -> Unit,
    private val onDeleteClick: (Guest) -> Unit
) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    inner class GuestViewHolder(val binding: ItemGuestBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemGuestBinding.inflate(LayoutInflater.from(context), parent, false)
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val guest = guestList[position]
        holder.binding.tvName.text = guest.name
        holder.binding.tvEmail.text = guest.email
        holder.binding.tvMessage.text = guest.message
        holder.binding.tvDate.text = guest.date.toString()  // Anda bisa format tanggal lebih baik di sini

        // Menangani klik untuk edit
        holder.binding.btnEdit.setOnClickListener {
            onEditClick(guest)
        }

        // Menangani klik untuk delete
        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(guest)
        }
    }

    override fun getItemCount(): Int {
        return guestList.size
    }

    fun updateList(newList: List<Guest>) {
        guestList.clear()
        guestList.addAll(newList)
        notifyDataSetChanged()
    }
}
