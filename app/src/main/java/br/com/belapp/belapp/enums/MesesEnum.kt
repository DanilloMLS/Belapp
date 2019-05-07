package br.com.belapp.belapp.enums

import java.util.ArrayList

enum class MesesEnum constructor(val mes: String, val valor: Int) {
    TodosMeses("Todos os me.", -1),
    Janeiro("Janeiro", 1),
    Fevereiro("Fevereiro", 2),
    Marco("Março", 3),
    Abril("Abril", 4),
    Maio("Maio", 5),
    Junho("Junho", 6),
    Julho("Março", 7),
    Agosto("Agosto", 8),
    Setembro("Setembro", 9),
    Outubro("Outubro", 10),
    Novembro("Novembro", 11),
    Dezembro("Dezembro", 12);


    companion object {
        fun getValor(avaliacao: String): Int {
            for (e in values()) {
                if (e.mes.equals(avaliacao, ignoreCase = true)) {
                    return e.valor
                }
            }
            return 0
        }

        val listaMeses: ArrayList<String>
            get() {
                val lista = ArrayList<String>()
                for (e in values()) {
                    lista.add(e.mes)
                }
                return lista
            }
    }
}