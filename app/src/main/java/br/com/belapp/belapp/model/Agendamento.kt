package br.com.belapp.belapp.model

import java.io.Serializable

class Agendamento : Serializable {

    var mServico: Servico? = null
    var mEstabelecimento: Estabelecimento? = null
    var mId: String? = null
    var mProfissional: Profissional? = null
    var mCliente: String? = null
    var mData: String? = null
    var mHora: String? = null
    var mStatus: String? = null
}
