package com.example.gestiondetelephone

data class User(
    val prenom : String? = null,
    val nomUtilisateur : String? = null,
    val email : String? = null,
    var numTel : Int? = null
){

}

data class Tel(
                 val nomTel: String? = null,
                 val sysEx: String? = null,
                 val ram : String? = null,
                 val cap : String? = null

){

}
data class User1(
    val prenom : String? = null,
    val nomUtilisateur : String? = null,
    val email : String? = null,
    val numTel : String? = null
){

}
