package br.com.belapp.belapp.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.text.DecimalFormat
import java.util.ArrayList
import java.util.Locale

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Servico

class ServicoAdapter(context: Context, private val testes: ArrayList<Servico>) :
    RecyclerView.Adapter<ServicoAdapter.ViewHolder>() {
    private val activity: ItemClicked

    init {
        activity = context as ItemClicked
    }

    interface ItemClicked {
        fun onItemClicked(index: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvServico: TextView
        var tvPreco: TextView
        var tvDuracao: TextView

        init {

            tvServico = itemView.findViewById(R.id.tvServico)
            tvPreco = itemView.findViewById(R.id.tvPreco)
            tvDuracao = itemView.findViewById(R.id.tvDuracao)

            itemView.setOnClickListener { v -> activity.onItemClicked(testes.indexOf(v.tag as Servico)) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_servico, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val df2 = DecimalFormat(".##")
        viewHolder.itemView.tag = testes[i]

        viewHolder.tvServico.text = testes[i].mNome

        viewHolder.tvPreco.text = String.format(Locale.getDefault(), "R$ %s", df2.format(testes[i].mPreco))
        val horas = testes[i].mDuracao / 60
        val minutos = testes[i].mDuracao % 60

        if (horas == 0) {
            viewHolder.tvDuracao.text = String.format(Locale.getDefault(), "Duração: %d min", minutos)
        } else if (minutos == 0) {
            viewHolder.tvDuracao.text = String.format(Locale.getDefault(), "Duração: %d hora(s)", horas)
        } else {
            viewHolder.tvDuracao.text =
                String.format(Locale.getDefault(), "Duração: %d hora(s) e %d min", horas, minutos)
        }
    }

    override fun getItemCount(): Int {
        return testes.size
    }
}
