package br.com.belapp.belapp.model

import java.io.Serializable

class Servico(
    var mId: String? = null,
    var mEstabId: String? = null,
    var mNome: String? = null,
    var mPreco: Double = 0.toDouble(),
    var mDuracao: Int = 0,
    var mProfissionais: String? = null,
    var mCategoria: String? = null
) : Serializable
