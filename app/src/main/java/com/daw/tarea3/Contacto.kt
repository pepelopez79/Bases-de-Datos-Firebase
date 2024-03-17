package com.daw.tarea3

import com.google.firebase.database.Exclude

data class Contacto(
    var id: Int = -1,
    var nick: String,
    var movil: String,
    var apellido1: String,
    var apellido2: String,
    var nombre: String,
    var email: String
) {
    constructor() : this(-1, "", "", "", "", "", "")

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "nick" to nick,
            "movil" to movil,
            "apellido1" to apellido1,
            "apellido2" to apellido2,
            "nombre" to nombre,
            "email" to email
        )
    }
}