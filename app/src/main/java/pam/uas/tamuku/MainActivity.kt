package pam.uas.tamuku

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pam.uas.tamuku.adapter.GuestAdapter
import pam.uas.tamuku.databinding.ActivityMainBinding
import pam.uas.tamuku.model.Guest
import pam.uas.tamuku.ui.theme.TamuKuTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var guestList: MutableList<Guest>
    private lateinit var guestAdapter: GuestAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Firebase Database reference
        database = FirebaseDatabase.getInstance().reference.child("guests")

        // Initialize RecyclerView
        guestList = mutableListOf()
        guestAdapter = GuestAdapter(this, guestList, ::onEditClick, ::onDeleteClick)
        binding.rvGuests.layoutManager = LinearLayoutManager(this)
        binding.rvGuests.adapter = guestAdapter

        // Load data from Firebase
        loadGuests()

        // Handle FAB Add click
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddGuestActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadGuests() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                guestList.clear()
                for (guestSnapshot in snapshot.children) {
                    val guest = guestSnapshot.getValue(Guest::class.java)
                    guest?.let { guestList.add(it) }
                }
                guestAdapter.updateList(guestList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun onEditClick(guest: Guest) {
        val intent = Intent(this, AddGuestActivity::class.java)
        intent.putExtra("guestId", guest.id) // Pass guest ID to edit
        startActivity(intent)
    }


    private fun onDeleteClick(guest: Guest) {
        database.child(guest.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Guest deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete guest", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TamuKuTheme {
        Greeting("Android")
    }
}