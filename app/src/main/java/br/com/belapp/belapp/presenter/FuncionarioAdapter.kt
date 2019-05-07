package br.com.belapp.belapp.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import java.text.DecimalFormat
import java.util.ArrayList

import br.com.belapp.belapp.R
import br.com.belapp.belapp.model.Profissional

class FuncionarioAdapter(context: Context, private val profissionais: ArrayList<Profissional>) :
    RecyclerView.Adapter<FuncionarioAdapter.ViewHolder>() {
    private val activity: ItemClicked
    private val agendarButtonClicked: AgendarButtonClicked

    interface ItemClicked {
        fun onItemClicked(index: Int)
    }

    interface AgendarButtonClicked {
        fun onAgendarButtonClicked(index: Int)
    }

    init {
        //servicos = list_servicos;
        activity = context as ItemClicked
        agendarButtonClicked = context as AgendarButtonClicked
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var ivProfissional: ImageView = itemView.findViewById(R.id.ivProfissional)
        internal var tvNome: TextView = itemView.findViewById(R.id.tvNome)
        internal var tvPreco: TextView = itemView.findViewById(R.id.tvPreco)

        init {
            val btn = itemView.findViewById<Button>(R.id.btnAgendar)
            btn.setOnClickListener { agendarButtonClicked.onAgendarButtonClicked(profissionais.indexOf(itemView.tag as Profissional)) }

            itemView.setOnClickListener { v -> activity.onItemClicked(profissionais.indexOf(v.tag as Profissional)) }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FuncionarioAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_profisisonal, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: FuncionarioAdapter.ViewHolder, i: Int) {
        val df2 = DecimalFormat(".##")
        viewHolder.itemView.tag = profissionais[i]

        /*for (int j = 0; j < profissionais.size(); j++){
            if (servicos.get(i).getmProfissionais().equals(profissionais.get(j).getMId())){
                viewHolder.tvPreco.setText("R$ "+df2.format(servicos.get(i).getmPreco()));
                viewHolder.tvNome.setText("Nome: "+profissionais.get(i).getNome());
            }
        }*/
        viewHolder.tvNome.text = "Nome: ${profissionais[i].nome}"
        viewHolder.tvPreco.text = profissionais[i].descricao
        viewHolder.ivProfissional.setImageResource(R.drawable.profissional_teste)
    }

    override fun getItemCount(): Int {
        return profissionais.size
    }
}
