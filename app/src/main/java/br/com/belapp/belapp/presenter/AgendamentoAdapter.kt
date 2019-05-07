package br.com.belapp.belapp.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList
import java.util.Locale

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Agendamento
import br.com.belapp.belapp.utils.DateUtils

class AgendamentoAdapter(context: Context, private val agendamentos: ArrayList<Agendamento>) :
    RecyclerView.Adapter<AgendamentoAdapter.ViewHolder>() {

    private val activity: ItemClicked

    interface ItemClicked {
        fun onItemClicked(index: Int)
    }

    init {
        activity = context as ItemClicked
    }

    inner class ViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val mTvDataAgendamento: TextView = itemView.findViewById(R.id.tvDataAgendada)
        val mTvHoraAgendamento: TextView = itemView.findViewById(R.id.tvHoraAgendamento)
        val mTvEstabelecimentoAgendamento: TextView = itemView.findViewById(R.id.tvEstabelecimentoAgendado)
        val mTvProfissionalAgendamento: TextView = itemView.findViewById(R.id.tvProfissionalAgendado)
        val mTvServicoAgendamento: TextView = itemView.findViewById(R.id.tvServicoAgendado)
        val mTvStatusAgendamento: TextView = itemView.findViewById(R.id.tvStatusAgendamento)

        init {
            itemView.setOnClickListener { v -> activity.onItemClicked(agendamentos.indexOf(v.tag as Agendamento)) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_agendamento, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val agendamento = agendamentos[i]
        if (agendamento.mId != null && agendamento.mId != "") {
            viewHolder.itemView.tag = agendamentos[i]
            viewHolder.mTvDataAgendamento.text = String.format(
                Locale.getDefault(), "Data: %s",
                agendamento.mData
            )
            viewHolder.mTvHoraAgendamento.text = String.format(
                Locale.getDefault(), "Horario: %s",
                agendamento.mHora
            )
            viewHolder.mTvEstabelecimentoAgendamento.text = agendamento.mEstabelecimento!!.mNome
            viewHolder.mTvServicoAgendamento.text = agendamento.mServico!!.mNome
            viewHolder.mTvProfissionalAgendamento.text =
                String.format(Locale.getDefault(), "Profissional: %s", agendamento.mProfissional!!.nome)
            if (!DateUtils.isDataFutura(agendamento.mData!!) && !DateUtils.isDataPresente(agendamento.mData!!)) {
                viewHolder.mTvStatusAgendamento.text = String.format("Status: %s", "Concluído")
                viewHolder.mTvStatusAgendamento.setTextColor(Color.RED)
            } else if (DateUtils.isDataFutura(agendamento.mData!!) || DateUtils.isDataPresente(agendamento.mData!!)) {
                viewHolder.mTvStatusAgendamento.text = String.format("Status: %s", "Agendado")
                viewHolder.mTvStatusAgendamento.setTextColor(Color.BLUE)
            }
        } else {
            viewHolder.mTvStatusAgendamento.visibility = View.GONE
            viewHolder.mTvProfissionalAgendamento.visibility = View.GONE
            viewHolder.mTvProfissionalAgendamento.visibility = View.GONE
            viewHolder.mTvHoraAgendamento.visibility = View.GONE
            viewHolder.mTvDataAgendamento.visibility = View.GONE
            viewHolder.mTvServicoAgendamento.text = agendamento.mServico!!.mNome
        }
    }

    override fun getItemCount(): Int {
        return agendamentos.size
    }
}
