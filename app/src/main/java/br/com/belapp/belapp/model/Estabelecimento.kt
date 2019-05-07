package br.com.belapp.belapp.model

import java.io.Serializable
import java.util.ArrayList

import br.com.belapp.belapp.enums.DiasSemanaEnum

class Estabelecimento : Serializable {

    var mEid: String? = null
    var mNome: String? = null
    var mDescricao: String? = null
    var mIdEndereco: String? = null
    var mDistancia: Double = 0.toDouble()
    var mLatitude: Double = 0.toDouble()
    var mLongitude: Double = 0.toDouble()
    var mRua: String? = null
    var mNumero: String? = null
    var mBairro: String? = null
    var mCidade: String? = null
    var mComplemento: String? = null
    var mCep: String? = null
    var img: String? = null
    var foto: String? = null
    var mHorariosAtendimento: List<HorarioAtendimento?> = mutableListOf()

    val mTelefone: String? = null
    val mHorarios: String? = null
    val mLinkFacebook: String? = null
    val mLinkInstagram: String? = null
    val mLinkSite: String? = null
    val mLinkEmail: String? = null

    val diasFuncionamento: String
        get() {
            var dias = ""
            for (horarioAtendimento in mHorariosAtendimento) {
                if (horarioAtendimento != null) {
                    dias += String.format(
                        " %s,",
                        DiasSemanaEnum.getDia(horarioAtendimento.mDiaFuncionamento).substring(0, 3)
                    )
                }
            }

            dias = dias.substring(0, dias.length - 1)
            return dias
        }

    constructor()

    constructor(mEid: String, mNome: String, mDescricao: String, mDistancia: Double, img: String) {
        this.mEid = mEid
        this.mNome = mNome
        this.mDescricao = mDescricao
        this.mIdEndereco = mIdEndereco
        this.mDistancia = mDistancia
        this.mHorariosAtendimento = ArrayList()
        this.img = img
        this.foto = foto
    }
}
