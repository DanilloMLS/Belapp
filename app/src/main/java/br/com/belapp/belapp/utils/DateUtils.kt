package br.com.belapp.belapp.utils

import android.annotation.SuppressLint

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    /**
     * Metodo que obtem a data atual do smartphone.
     * @return data no formato dd/mm/YYYY.
     */
    val dataAtual: String
        @SuppressLint("SimpleDateFormat")
        get() = SimpleDateFormat("dd/MM/yyyy").format(Date())

    fun getFormatoHora(hora: Int, minutos: Int): String {
        var sh = hora.toString()
        var sm = minutos.toString()
        if (sh.length == 1) {
            sh = sh.replace(sh.toRegex(), "0$sh")
        }
        if (sm.length == 1) {
            sm = sm.replace(sm.toRegex(), "0$sm")
        }

        return String.format(Locale.getDefault(), "%s:%s", sh, sm)
    }

    /**
     *
     * Converte uma data para String no formato 00/00/0000
     * @param data do tipo Calendar
     * @return String com a data no formato 00/00/0000
     */
    fun converterDataParaString(data: Calendar): String {
        val formato = "dd/MM/yyyy"
        val formatador = SimpleDateFormat(formato, Locale("pt", "BR"))
        return formatador.format(data.time)
    }

    /* Este Método retorna o dia da semana referente a data informado no
     * parâmetro
     *
     * 1º dia da semana retorna 1; Ultimo dias da semana retorna 7;

     * @param data data no formato formato DD/MM/YYYY
     * @return dia da semana referente a data informada
     */
    fun getDiaDaSemanaEmData(data: String): Int {
        return converterDataEmCalendar(data).get(Calendar.DAY_OF_WEEK)
    }

    /**
     * Este Método converte uma data no formato DD/MM/YYYY no formato de data
     * calendar.
     *
     * @param data string data no formato DD/MM/YYYY
     * @return data no formato Calendar
     */
    private fun converterDataEmCalendar(data: String): Calendar {
        val c = Calendar.getInstance()
        val d = data.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        c.set(Integer.parseInt(d[2]), Integer.parseInt(d[1]) - 1, Integer.parseInt(d[0]))
        return c
    }

    /**
     * @param data no formato DD/MM/YYYY a ser verificada
     * @return true se é data futura
     */
    fun isDataFutura(data: String): Boolean {
        return converterDataEmCalendar(data).after(converterDataEmCalendar(dataAtual))
    }

    /**
     * @param data no formato DD/MM/YYYY a ser verificada
     * @return true se é data presente
     */
    fun isDataPresente(data: String): Boolean {
        return getDiferencaEntreDuasDatasEspecificas(dataAtual, data) == 0
    }

    /**
     * @param dias a serem somados na data
     * @param data base no formado DD/MM/YYYY
     * @return Data atualizada data + dias no formado DD/MM/YYYY
     */
    fun getSomaDiasComDataEspecifica(dias: Int, data: String): String {
        val c = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val sd = SimpleDateFormat("dd/MM/yyyy")
        val componentesData = data.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        c.set(
            Integer.parseInt(componentesData[2]),
            Integer.parseInt(componentesData[1]) - 1,
            Integer.parseInt(componentesData[0]) + dias
        )
        return sd.format(c.time)
    }

    /**
     * Gera uma data com base na data atual
     * @return uma data no formato  DD/MM/YYYY
     */
    fun gerarData(): String {
        val numero = (Math.random() * 10).toInt() + 1
        return getSomaDiasComDataEspecifica(numero, dataAtual)
    }

    fun gerarDataValida(diasValidos: Collection<Int>): String {
        val data = gerarData()
        var find = false
        while (!find) {
            if (diasValidos.contains(getDiaDaSemanaEmData(data))) {
                find = true
            }
        }
        return data
    }

    /**
     *
     * @param diasInvalidos onde a data não pode estar
     * @return uma data que não pertença aos diasInvalidos
     */
    fun gerarDataInvalida(diasInvalidos: Collection<Int>): String {
        var data = ""
        var find = false
        if (diasInvalidos.size == 7) {
            return getSomaDiasComDataEspecifica(-1, dataAtual)
        }
        while (!find) {
            data = gerarData()
            if (!diasInvalidos.contains(getDiaDaSemanaEmData(data))) {
                find = true
            }
        }
        return data
    }

    /**
     * @param dias a serem subtraídos da data base
     * @param data base no formado DD/MM/YYYY
     * @return Data atualizada data - dias no formado DD/MM/YYYY
     */
    fun getSubtracaoDiasComDataEspecifica(dias: Int, data: String): String {

        val c = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat") val sd = SimpleDateFormat("dd/MM/yyyy")
        val componentesData = data.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        c.set(
            Integer.parseInt(componentesData[2]),
            Integer.parseInt(componentesData[1]) - 1,
            Integer.parseInt(componentesData[0]) - dias
        )
        return sd.format(c.time)
    }

    fun getDiferencaEntreDuasDatasEspecificas(data1: String, data2: String): Int {
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val componenetesData1 = data1.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val componenetesData2 = data2.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        c1.set(
            Integer.parseInt(componenetesData1[2]),
            Integer.parseInt(componenetesData1[1]) - 1,
            Integer.parseInt(componenetesData1[0])
        )
        c2.set(
            Integer.parseInt(componenetesData2[2]),
            Integer.parseInt(componenetesData2[1]) - 1,
            Integer.parseInt(componenetesData2[0])
        )

        val milles = (c2.timeInMillis - c1.timeInMillis).toDouble()
        val seconds = milles / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return Math.floor(days).toInt()
    }

    /**
     * @param data no formato dd/mm/yyyy
     * @param mes código do mês
     * @return true se a data pertence ao mês
     */
    fun checarSeDataPertenceAoMes(data: String, mes: Int): Boolean {
        return Integer.parseInt(data.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]) == mes
    }
}
