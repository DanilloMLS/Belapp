package br.com.belapp.belapp.presenter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.text.DecimalFormat
import java.util.ArrayList

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Estabelecimento
import br.com.belapp.belapp.utils.ImageDownloaderTask

class SalaoAdapter(context: Context, private val lista: ArrayList<Estabelecimento>) :
    RecyclerView.Adapter<SalaoAdapter.ViewHolder>() {
    private val activity: ItemClicked

    interface ItemClicked {
        fun onItemClicked(index: Int)
    }

    init {
        activity = context as ItemClicked
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imagem: ImageView
        var tvNomeSalao: TextView
        var tvEnderecoSalao: TextView
        var tvDistancia: TextView

        init {

            //ivFotoSalao = itemView.findViewById(R.id.ivFotoSalao);
            tvNomeSalao = itemView.findViewById(R.id.tvNomeSalao)
            tvEnderecoSalao = itemView.findViewById(R.id.tvEnderecoSalao)
            tvDistancia = itemView.findViewById(R.id.tvDistancia)
            imagem = itemView.findViewById<View>(R.id.ivFotoSalao) as ImageView

            itemView.setOnClickListener { v -> activity.onItemClicked(lista.indexOf(v.tag as Estabelecimento)) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_salao, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val df2 = DecimalFormat("#.##")
        if (lista.size != 0) {
            viewHolder.itemView.tag = lista[i]

            viewHolder.tvNomeSalao.text = lista[i].mNome
            viewHolder.tvEnderecoSalao.text =
                "${lista[i].mRua}, ${lista[i].mNumero}, ${lista[i].mBairro}, ${lista[i].mCidade}"

            viewHolder.tvDistancia.text = "Distância: " + df2.format(lista[i].mDistancia) + " Km"

            val caminhourl = lista[i].img
            if (caminhourl != null) {
                Log.d("caminho img:", caminhourl)
                try {
                    ImageDownloaderTask(viewHolder.imagem).execute(caminhourl)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}