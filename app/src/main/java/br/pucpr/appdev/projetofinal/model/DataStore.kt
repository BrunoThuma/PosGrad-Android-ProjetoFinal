package br.pucpr.appdev.projetofinal.model

import android.content.Context
import android.util.Log

object DataStore {
    var produtos: MutableList<ProdutoModel> = arrayListOf()
        private set

//    private var myContext: Context? = null
    private var database: Database? = null

//    init {
//        addProduto(ProdutoModel("Chocolate", 7.5f))
//        addProduto(ProdutoModel("Leite", 2.5f))
//    }

    fun setContext(value: Context) {
        database = Database(value)
        database?.let {
            produtos = it.getAllProdutos()
        }
    }

    fun getProduto(posicao: Int): ProdutoModel {
        return produtos.get(posicao)
    }

    fun addProduto(produto: ProdutoModel) {

        val id = database?.addProduto(produto) ?: return

        if (id > 0) {
            produto.id = id
            produtos.add(produto)
            Log.d("SQLite!", "1 registro incluído com sucesso!!!")
        }
    }

    fun editProduto(produto: ProdutoModel, posicao: Int) {

        produto.id = produtos[posicao].id

        val count = database?.editProduto(produto) ?: return

        if (count > 0) {
            produtos.set(posicao, produto)
            Log.d("SQLite!", "$count registros alterados com sucesso!!!")
        }
    }

    fun removeProduto(posicao: Int) {

        val count = database?.removeProduto(produtos[posicao]) ?: return

        if (count > 0) {
            produtos.removeAt(posicao)
            Log.d("SQLite!", "$count registros excluídos com sucesso!!!")
        }
    }

    fun clearProdutos() {

        val count = database?.clearProdutos() ?: return

        if (count > 0) {
            produtos.clear()
            Log.d("SQLite!", "$count registros excluídos com sucesso!!!")
        }
    }

}