package br.com.belapp.belapp.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object ConfiguracaoFireBase {

    //aqui configuração com banco firebase
    private var sDataBaseReferece: DatabaseReference? = null
    private var sAutenticacao: FirebaseAuth? = null

    val firebase: DatabaseReference
        get() {

            if (sDataBaseReferece == null) {
                sDataBaseReferece = FirebaseDatabase.getInstance().reference
            }
            return sDataBaseReferece!!
        }

    val firebaseAutenticacao: FirebaseAuth
        get() {

            if (sAutenticacao == null) {
                sAutenticacao = FirebaseAuth.getInstance()
            }
            return sAutenticacao!!
        }
}
