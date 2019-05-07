package br.com.belapp.belapp.model

import com.google.firebase.database.Exclude

class Promocoes(
    @get:Exclude
    val midestabelecimento: String? = null,

    @get:Exclude
    val midecliente: String? = null,

    val titulo: String? = null,
    val descricao: String? = null,
    val foto: String? = null
) {

    fun salvar() {
        val databaseReference = ConfiguracaoFireBase.firebase
        databaseReference.child("promocoes").child(midecliente!!).child(midestabelecimento!!).setValue(this)
    }
}
