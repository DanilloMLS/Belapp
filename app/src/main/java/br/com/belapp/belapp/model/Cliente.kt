package br.com.belapp.belapp.model

import java.util.ArrayList

class Cliente(
    var mEmail: String? = null,
    var mSenha: String? = null,
    var mFoto: String? = null,
    var mTelefone: String? = null,
    var mNome: String? = null,
    var mEnderecoID: String? = null,
    var mAgendamentoID: String? = null,
    var mFavoritos: ArrayList<String>? = null // estabelecimentoID
) {
    fun salvar(id: String) {
        val databaseReference = ConfiguracaoFireBase.firebase
        databaseReference.child("clientes").child(id).setValue(this)
    }
}
