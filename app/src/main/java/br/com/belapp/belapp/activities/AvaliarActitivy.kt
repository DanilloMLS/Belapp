package br.com.belapp.belapp.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import java.text.SimpleDateFormat
import java.util.Date

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Avaliacao
import br.com.belapp.belapp.model.Cliente
import br.com.belapp.belapp.model.ConfiguracaoFireBase

class AvaliarActitivy : AppCompatActivity() {

    private var tvNota: TextView? = null
    private var tvComentario: EditText? = null
    private var ivEstrela1: ImageView? = null
    private var ivEstrela2: ImageView? = null
    private var ivEstrela3: ImageView? = null
    private var ivEstrela4: ImageView? = null
    private var ivEstrela5: ImageView? = null
    private var mNota = 0.0
    private var mIdSalao: String? = null
    private var mIdCliente: String? = null
    private var mNomeCliente: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avaliar_actitivy)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_avaliar)
        toolbar.setTitle(R.string.title_activity_avaliar)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        setSupportActionBar(toolbar)

        val tvNome = findViewById<TextView>(R.id.tv_nome_avaliar)
        tvNota = findViewById(R.id.tv_nota_avaliar)
        tvComentario = findViewById(R.id.et_comentario_avaliar)
        ivEstrela1 = findViewById(R.id.iv_estrela1)
        ivEstrela2 = findViewById(R.id.iv_estrela2)
        ivEstrela3 = findViewById(R.id.iv_estrela3)
        ivEstrela4 = findViewById(R.id.iv_estrela4)
        ivEstrela5 = findViewById(R.id.iv_estrela5)
        val btEnviar = findViewById<Button>(R.id.bt_enviar_avaliar)

        mIdCliente = intent.extras!!.getString("idCliente")
        mIdSalao = intent.extras!!.getString("idEstabelecimento")
        val mNomeSalao = intent.extras!!.getString("nome")

        tvNome.text = mNomeSalao

        buscarNomeCliente(mIdCliente)

        ivEstrela1!!.setOnClickListener {
            preencherEstrelas(1)
            tvNota!!.text = "Desapontado"
            mNota = 1.0
        }

        ivEstrela2!!.setOnClickListener {
            preencherEstrelas(2)
            tvNota!!.text = "Não tão bom"
            mNota = 2.0
        }

        ivEstrela3!!.setOnClickListener {
            preencherEstrelas(3)
            tvNota!!.text = "Bom"
            mNota = 3.0
        }

        ivEstrela4!!.setOnClickListener {
            preencherEstrelas(4)
            tvNota!!.text = "Gostei"
            mNota = 4.0
        }

        ivEstrela5!!.setOnClickListener {
            preencherEstrelas(5)
            tvNota!!.text = "Excelente"
            mNota = 5.0
        }

        btEnviar.setOnClickListener {
            if (mNota != 0.0) {
                val avaliar = Avaliacao()
                avaliar.mComentario = tvComentario!!.text.toString()
                avaliar.mControle = gerarControle()
                avaliar.mData = pegarData()
                avaliar.mFoto = buscarFoto()
                avaliar.mNome = mNomeCliente
                avaliar.mNota = mNota
                salvarAvaliacao(avaliar, mIdSalao, mIdCliente)
                //Log.d("CLIENTE", "comentario: "+avaliar.getMComentario()+" data: "+ avaliar.getMData()+ " nome: "+avaliar.getMNome()+ " controle: "+ avaliar.getMControle()+" nota: "+String.valueOf(avaliar.getMNota()));
                Toast.makeText(this@AvaliarActitivy, "Avaliação foi salva!", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this@AvaliarActitivy, "Dê uma nota!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun preencherEstrelas(nota: Int) {
        val cheia = R.drawable.estrela_cheia
        val vazia = R.drawable.estrela_vazia
        val estrelas = listOf(ivEstrela1, ivEstrela2, ivEstrela3, ivEstrela4, ivEstrela5)
        estrelas.forEachIndexed { index, imageView ->
            if (nota >= index + 1) {
                imageView!!.setImageResource(cheia)
            } else {
                imageView!!.setImageResource(vazia)
            }
        }
    }

    private fun pegarData(): String {
        val date = Date()
        val formatador = SimpleDateFormat("dd/MM/yyyy")

        return formatador.format(date)
    }

    private fun gerarControle(): Int {
        var data = ""
        var controle = 0
        val date = Date()
        data += (date.year + 1900).toString()
        data += (date.month + 1).toString()
        data += date.date.toString()
        controle = Integer.parseInt(data)
        return controle
    }

    private fun buscarNomeCliente(id: String?) {
        val raiz = FirebaseDatabase.getInstance().reference
        val cliente = raiz.child("clientes").child(id!!)

        cliente.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val cliente1 = dataSnapshot.getValue(Cliente::class.java)
                mNomeCliente = cliente1!!.mNome
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun buscarFoto(): String {
        return "null"
    }

    private fun salvarAvaliacao(avaliacao: Avaliacao, idSalao: String?, idCliente: String?) {
        val databaseReference = ConfiguracaoFireBase.firebase
        databaseReference.child("avaliacoes").child(idSalao!!).child(idCliente!!).setValue(avaliacao)
    }

    override fun onSupportNavigateUp(): Boolean {
        // TODO: Verificar se ha alteracoes antes de voltar
        onBackPressed()
        return true
    }
}
