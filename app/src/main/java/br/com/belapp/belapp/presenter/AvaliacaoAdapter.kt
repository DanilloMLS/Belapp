package br.com.belapp.belapp.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Avaliacao

class AvaliacaoAdapter(private val mContexto: Context, private val mAvaliacoes: ArrayList<Avaliacao>) : RecyclerView.Adapter<AvaliacaoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AvaliacaoAdapter.MyViewHolder {      //sufixa acrescentado "Avaliacao    "
        val mInflanter = LayoutInflater.from(mContexto)
        val v = mInflanter.inflate(R.layout.item_avaliacao, viewGroup, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(myViewHolder: AvaliacaoAdapter.MyViewHolder, i: Int) {
        myViewHolder.tvNome.text = mAvaliacoes[i].mNome
        myViewHolder.tvData.text = mAvaliacoes[i].mData
        myViewHolder.tvComentario.text = mAvaliacoes[i].mComentario
        myViewHolder.ivNota.setImageResource(carregarAvaliacao(mAvaliacoes[i].mNota))
    }

    override fun getItemCount(): Int {
        return mAvaliacoes.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tvNome: TextView = itemView.findViewById(R.id.tvNomeCliente)
        internal var tvData: TextView = itemView.findViewById(R.id.tvDataAvaliacao)
        internal var tvComentario: TextView = itemView.findViewById(R.id.tvComentario)
        internal var ivNota: ImageView = itemView.findViewById(R.id.ivAvaliacao)
        internal var ivFoto: ImageView = itemView.findViewById(R.id.ivFotoCliente)
    }

    private fun carregarAvaliacao(i: Double): Int {
        return when {
            i > 4.5 -> R.drawable.estrela_5
            i > 4 -> R.drawable.estrela_4_5
            i > 3.5 -> R.drawable.estrela_4
            i > 3 -> R.drawable.estrela_3_5
            i > 2.5 -> R.drawable.estrela_3
            i > 2 -> R.drawable.estrela_2_5
            i > 1.5 -> R.drawable.estrela_2
            i > 1 -> R.drawable.estrela_1_5
            i > 0.5 -> R.drawable.estrela_1
            i > 0 -> R.drawable.estrela_0_5
            else -> R.drawable.estrela_0
        }
    }
}
