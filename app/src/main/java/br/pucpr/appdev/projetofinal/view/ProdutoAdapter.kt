package br.pucpr.appdev.projetofinal.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.projetofinal.model.ProdutoModel
import br.pucpr.appdev.projetofinal.R

/*
Adapter do nosso model. Possui um holder que ao receber uma view, es
 */
class ProdutoAdapter(var produtos: MutableList<ProdutoModel>) : RecyclerView.Adapter<ProdutoAdapter.ProdutoHolder>() {

    inner class ProdutoHolder(view: View) : RecyclerView.ViewHolder(view) {

        var txtNome: TextView
        var txtUltimoPreco: TextView
        var layoutCelula: LinearLayout

        init {
            txtNome = view.findViewById(R.id.txtProduto)
            txtUltimoPreco = view.findViewById(R.id.txtUltimoPreco)
            layoutCelula = view.findViewById(R.id.layoutCelula)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.rcv_produtos, parent, false)

        return ProdutoHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoHolder, position: Int) {

        val produto = produtos.get(position)

        holder.txtNome.text = produto.nome
        holder.txtUltimoPreco.text = "Ultimo preço praticado: R$${produto.ultimoPreco}"
    }

    override fun getItemCount(): Int {
        return produtos.size
    }
}