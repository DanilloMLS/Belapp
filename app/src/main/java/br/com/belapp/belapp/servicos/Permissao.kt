package br.com.belapp.belapp.servicos

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

import com.google.firebase.auth.FirebaseAuth

import java.util.ArrayList

import br.com.belapp.belapp.activities.CadastroBasicoActivity
import br.com.belapp.belapp.activities.LoginActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

fun validaPermissoes(requestCode: Int, activity: Activity, permissoes: Array<String>): Boolean {
    if (Build.VERSION.SDK_INT >= 23) {
        val listaPermissoes = ArrayList<String>()

        /*Percorre as permissões passadas, verificando uma a uma
         * se já tem a permissao liberada */
        for (permissao in permissoes) {
            val validaPermissao =
                ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED
            if (!validaPermissao) {
                listaPermissoes.add(permissao)
            }
        }

        /*Caso a lista esteja vazia, não é necessário solicitar permissão*/
        if (listaPermissoes.isEmpty()) {
            return true
        }

        val novasPermissoes = arrayOfNulls<String>(listaPermissoes.size)
        listaPermissoes.toTypedArray()

        //Solicita permissão
        ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode)
    }

    return false
}

fun verificarPermissaoRestritivo(contexto: Context): Boolean {
    val autenticacao = FirebaseAuth.getInstance()
    if (autenticacao.currentUser == null) {
        alertaPrecisaEntrar(contexto)
        return false
    }
    return true
}

private fun alertaPrecisaEntrar(contexto: Context) {
    val builder = AlertDialog.Builder(contexto)
    builder.setTitle("Acesso negado")
    builder.setMessage("É necessário entrar no sistema.\n\nVocê já está cadastrado?")
    builder.setPositiveButton("SIM") { dialogInterface, i ->
        val intentAbritCadastro = Intent(contexto, LoginActivity::class.java)
        startActivity(contexto, intentAbritCadastro, null)
    }
    builder.setNegativeButton("NÃO") { _, _ ->
        val intentAbritCadastro = Intent(contexto, CadastroBasicoActivity::class.java)
        startActivity(contexto, intentAbritCadastro, null)
    }
    val dialog = builder.create()
    dialog.show()
}

fun estaLogado(): Boolean {
    return FirebaseAuth.getInstance().currentUser != null
}

