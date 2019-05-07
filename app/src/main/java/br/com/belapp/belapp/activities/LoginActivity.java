package br.com.belapp.belapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.belapp.belapp.R;
import br.com.belapp.belapp.model.Cliente;
import br.com.belapp.belapp.model.ConfiguracaoFireBase;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnlogar;
    private FirebaseAuth autenticacao;
    private Cliente cliente;
    private TextView tvEsqueciSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnlogar = findViewById(R.id.btnLogar);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);

        btnlogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                    cliente = new Cliente();
                    cliente.setMEmail(edtEmail.getText().toString());
                    cliente.setMSenha(edtSenha.getText().toString());
                    validarLogin();


                } else {
                    Toast.makeText(LoginActivity.this, "preencha os campos de email e senha !", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RecuperaSenhaActivity.class);
                startActivity(intent);
            }
        });


    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFireBase.INSTANCE.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(cliente.getMEmail(), cliente.getMSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, getString(R.string.sucess_login_efetuado), Toast.LENGTH_LONG).show();

                } else {

                    //tratamento de exceções do cadastro
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuario não cadastrado!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "email ou senha não correspndem a um usuario cadastrado !";
                    } catch (Exception e) {
                        excecao = "Erro ao logar usuario" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();


                }
            }
        });


    }

    public void abrirTelaPrincipal() {

        Intent intentAbritPrincipal = new Intent(LoginActivity.this, ClienteLogadoActivity.class);
        startActivity(intentAbritPrincipal);

    }

}