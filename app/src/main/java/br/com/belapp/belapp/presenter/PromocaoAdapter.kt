package br.com.belapp.belapp.presenter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.squareup.picasso.Picasso

import java.util.ArrayList

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Estabelecimento
import br.com.belapp.belapp.model.Promocoes

class PromocaoAdapter(
    context: Context,
    private val mlista: ArrayList<Estabelecimento>,
    private val mlista2: ArrayList<Promocoes>
) : RecyclerView.Adapter<PromocaoAdapter.ViewHolder>() {
    private val activity: ItemClicked

    interface ItemClicked {
        fun onItemClicked(index: Int)
    }

    init {
        activity = context as ItemClicked
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivFotoP: ImageView = itemView.findViewById(R.id.ivFotoP)
        internal var tvNomeSalaoP: TextView = itemView.findViewById(R.id.tvNomeSalaoP)
        internal var tvTituloP: TextView = itemView.findViewById(R.id.tvTituloP)
        internal var tvDescP: TextView = itemView.findViewById(R.id.tvDescP)

        init {
            itemView.setOnClickListener { v -> activity.onItemClicked(mlista.indexOf(v.tag as Estabelecimento)) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_promocao, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        if (mlista.size != 0) {
            val urlPhoto = mlista2[i].foto
            Picasso.get().load(urlPhoto).into(viewHolder.ivFotoP)
            viewHolder.itemView.tag = mlista[i]

            viewHolder.tvNomeSalaoP.text = mlista[i].mNome
            viewHolder.tvTituloP.text = mlista2[i].titulo
            viewHolder.tvDescP.text = mlista2[i].descricao

        }
    }

    override fun getItemCount(): Int {
        return mlista.size
    }
}