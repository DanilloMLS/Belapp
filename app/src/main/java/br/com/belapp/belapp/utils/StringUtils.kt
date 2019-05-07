package br.com.belapp.belapp.utils

import android.text.TextUtils

import java.util.Locale

object StringUtils {

    fun isEmailValido(email: CharSequence): Boolean {
        return if (TextUtils.isEmpty(email)) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    fun isVerificarSenhaInvalida(senha: String): Boolean {
        var retorno = false
        if (senha.length < 6) {
            retorno = true
        }
        return retorno
    }

    fun gerarEmail(): String {
        val numero = (Math.random() * 10000).toInt() + 1
        return String.format(Locale.getDefault(), "teste%d@teste.com", numero)
    }

    fun getDinheiro(valor: Double): String {
        return String.format(Locale.getDefault(), "R$ %.2f", valor)
    }

}
