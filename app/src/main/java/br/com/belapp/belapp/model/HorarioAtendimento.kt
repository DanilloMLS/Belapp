package br.com.belapp.belapp.model

import java.io.Serializable

import br.com.belapp.belapp.enums.DiasSemanaEnum

class HorarioAtendimento(var mAbertura: Int = 0, var mFechamento: Int = 0, var mDiaFuncionamento: Int = 0) : Serializable
