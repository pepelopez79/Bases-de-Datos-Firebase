import com.daw.tarea3.Contacto
import com.google.firebase.database.*

class DatabaseHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("contactos")

    fun crearColeccionContactos(callback: (Boolean) -> Unit) {
        database.setValue("contactos").addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    // Agregar un contacto
    fun agregarContacto(contacto: Contacto, callback: (Contacto?) -> Unit) {
        val contactoId = database.push().key
        if (contactoId != null) {
            database.child(contactoId).setValue(contacto)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(contacto)
                    } else {
                        callback(null)
                    }
                }
        } else {
            callback(null)
        }
    }

    // Obtener todos los contactos
    fun obtenerContactos(callback: (List<Contacto>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contactos = mutableListOf<Contacto>()
                for (data in snapshot.children) {
                    val contacto = data.getValue(Contacto::class.java)
                    contacto?.let { contactos.add(it) }
                }
                callback(contactos)
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Obtener contacto por nick
    fun obtenerContactoPorNick(nick: String, callback: (Contacto?) -> Unit) {
        val query: Query = database.orderByChild("nick").equalTo(nick)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val contacto = snapshot.children.first().getValue(Contacto::class.java)
                    callback(contacto)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Obtener contacto por móvil
    fun obtenerContactoPorMovil(movil: String, callback: (Contacto?) -> Unit) {
        val query: Query = database.orderByChild("movil").equalTo(movil)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val contacto = snapshot.children.first().getValue(Contacto::class.java)
                    callback(contacto)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Eliminar contacto por nick
    fun eliminarContactoPorNick(nick: String, callback: (Boolean) -> Unit) {
        val query: Query = database.orderByChild("nick").equalTo(nick)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val contactoId = snapshot.children.first().key
                    if (contactoId != null) {
                        database.child(contactoId).removeValue()
                            .addOnCompleteListener { task ->
                                callback(task.isSuccessful)
                            }
                    }
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(error: DatabaseError) { }
        })
    }

    // Actualizar contacto
    fun editarMovilYEmail(contactoId: Contacto?, nuevoMovil: String, nuevoEmail: String, callback: (Boolean) -> Unit) {
        val contactoUpdates = mapOf<String, Any>(
            "$contactoId/movil" to nuevoMovil,
            "$contactoId/email" to nuevoEmail
        )
        database.updateChildren(contactoUpdates)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}