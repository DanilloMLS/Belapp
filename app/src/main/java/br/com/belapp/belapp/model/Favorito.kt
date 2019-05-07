package br.com.belapp.belapp.model

import com.google.firebase.database.Exclude

class Favorito(
    @get:Exclude
    var idEstabelecimento: String? = null,

    var curtida: Int = 0,

    @get:Exclude
    var idCliente: String? = null
) {

    fun salvar() {
        val databaseReference = ConfiguracaoFireBase.firebase
        databaseReference.child("favoritos").child(idCliente!!).child(idEstabelecimento!!).setValue(this)
    }

    fun remove() {
        val databaseReference = ConfiguracaoFireBase.firebase
        databaseReference.child("favoritos").child(idCliente!!).child(idEstabelecimento!!).removeValue()
    }
}
