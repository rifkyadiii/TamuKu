package pam.uas.tamuku.model

data class Guest(
    var id: String = "",
    val name: String = "",
    val email: String = "",
    val message: String = "",
    val date: Long = System.currentTimeMillis()
)