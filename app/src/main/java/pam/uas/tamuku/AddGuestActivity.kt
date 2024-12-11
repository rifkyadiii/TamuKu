package pam.uas.tamuku

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pam.uas.tamuku.databinding.ActivityAddGuestBinding
import pam.uas.tamuku.model.Guest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddGuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddGuestBinding
    private lateinit var database: DatabaseReference
    private var guestId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase reference
        database = FirebaseDatabase.getInstance().reference.child("guests")

        // Cek apakah kita sedang mengedit guest atau menambah guest baru
        guestId = intent.getStringExtra("guestId")
        if (guestId != null) {
            // Load data tamu yang ada untuk diedit
            loadGuestData(guestId!!)
        }

        // Tombol Save klik
        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val message = binding.etMessage.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && message.isNotEmpty()) {
                if (guestId == null) {
                    // Menambah tamu baru
                    addGuest(Guest(name = name, email = email, message = message))
                } else {
                    // Mengupdate tamu yang ada
                    updateGuest(Guest(id = guestId!!, name = name, email = email, message = message))
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk menambahkan guest baru
    private fun addGuest(guest: Guest) {
        val newGuestRef = database.push() // Auto-generate id
        guest.id = newGuestRef.key ?: ""
        newGuestRef.setValue(guest).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Guest added", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Failed to add guest", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk mengupdate guest
    private fun updateGuest(guest: Guest) {
        database.child(guest.id).setValue(guest).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Guest updated", Toast.LENGTH_SHORT).show()
                finish() // Kembali ke MainActivity
            } else {
                Toast.makeText(this, "Failed to update guest", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Fungsi untuk memuat data guest yang sudah ada
    private fun loadGuestData(guestId: String) {
        database.child(guestId).get().addOnSuccessListener { snapshot ->
            val guest = snapshot.getValue(Guest::class.java)
            guest?.let {
                binding.etName.setText(it.name)
                binding.etEmail.setText(it.email)
                binding.etMessage.setText(it.message)
            }
        }
    }
}
