package br.com.belapp.belapp.enums

import java.util.Calendar

enum class DiasSemanaEnum constructor(val dia: String, val codigo: Int) {
    DOMINGO("Domingo", Calendar.SUNDAY),
    SEGUNDA("Segunda", Calendar.MONDAY),
    TERCA("Terça", Calendar.TUESDAY),
    QUARTA("Quarta", Calendar.WEDNESDAY),
    QUINTA("Quinta", Calendar.THURSDAY),
    SEXTA("Sexta", Calendar.FRIDAY),
    SABADO("Sábado", Calendar.SATURDAY);


    companion object {
        fun getDia(codigo: Int): String {
            for (e in values()) {
                if (e.codigo == codigo) {
                    return e.dia
                }
            }
            return ""
        }
    }
}
