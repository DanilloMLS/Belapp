package br.com.belapp.belapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

import br.com.belapp.belapp.R

class RecuperaSenhaActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recupera_senha)

        val toolbar = findViewById<View>(R.id.tbRecSenha) as Toolbar
        toolbar.title = "Recuperar Senha"
        setSupportActionBar(toolbar)

        etEmail = findViewById(R.id.etEmailRec)
        val btnSolicitarRec = findViewById<Button>(R.id.btnSolicitarRec)

        firebaseAuth = FirebaseAuth.getInstance()

        btnSolicitarRec.setOnClickListener { v ->
            email = etEmail.text.toString()
            if (email.isNotEmpty()) {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        etEmail.setText("")
                        Toast.makeText(
                            this@RecuperaSenhaActivity,
                            getString(R.string.recuperar_senha_iniciada),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(
                            this@RecuperaSenhaActivity,
                            LoginActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@RecuperaSenhaActivity,
                            getString(R.string.falha_recuperar_senha_invalido),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@RecuperaSenhaActivity,
                    getString(R.string.recuperar_digite_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
