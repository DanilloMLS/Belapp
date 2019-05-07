package br.com.belapp.belapp.model

import java.io.Serializable

class Profissional(
    var mId: String? = null,
    var nome: String? = null,
    var descricao: String? = null,
    var atendDomic: String? = null
) : Serializable
