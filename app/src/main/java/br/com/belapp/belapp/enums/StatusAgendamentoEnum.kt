package br.com.belapp.belapp.enums

import java.util.ArrayList

enum class StatusAgendamentoEnum constructor(val status: String, val codigo: Int) {

    AGENDADO("Serviço(s) Agendado(s)", 1),
    CONCLUIDO("Serviço(s) Concluído(s)", 2),

    TODOS("Todos", 0);

    companion object {
        val listaStatus: ArrayList<String>
            get() {
                val lista = ArrayList<String>()
                for (e in values()) {
                    lista.add(e.status)
                }
                return lista
            }
    }
}
