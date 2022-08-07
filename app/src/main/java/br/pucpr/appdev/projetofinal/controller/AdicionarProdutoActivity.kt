package br.pucpr.appdev.projetofinal.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.pucpr.appdev.projetofinal.R
import br.pucpr.appdev.projetofinal.model.DataStore
import br.pucpr.appdev.projetofinal.model.ProdutoModel

class AdicionarProdutoActivity : AppCompatActivity() {

    var posicaoParaEditar = 0
    var produtoSendoEditado = ProdutoModel("", 0.0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_produto)

        val editTxtNomeProduto = findViewById<EditText>(R.id.produtoEditTextNomeProduto)
        val editTxtUltimoPreco = findViewById<EditText>(R.id.produtoEditTextUltimoPreco)
        val btnSalvar = findViewById<Button>(R.id.produtoBtnSalvar)
        val btnCancelar = findViewById<Button>(R.id.produtoBtnCancelar)

        // Recuperar a informacao extra passada para a intent com chave info
        val estaEditando = intent.getBooleanExtra("estaEditandoProduto", false)
        print("Jonas estaEditando: $estaEditando")

        // Neste caso estamos editando uma cidade ja existente
        if (estaEditando) {

            // Editando o titulo da Toolbar para edicao de produto
            supportActionBar?.setTitle("Editando item")

            posicaoParaEditar = intent.getIntExtra("posicaoDoProduto", 0)

            produtoSendoEditado = DataStore.getProduto(posicaoParaEditar)

            editTxtNomeProduto.setText(produtoSendoEditado.nome)
            editTxtUltimoPreco.setText(produtoSendoEditado.ultimoPreco.toString())

        } else {
            // Editando o titulo da Toolbar para criacao de produto
            supportActionBar?.setTitle("Novo item")
        }

        btnSalvar.setOnClickListener {

            // Edita as propriedades do produto de acordo com valores dos campos
            produtoSendoEditado.nome = editTxtNomeProduto.text.toString()
            produtoSendoEditado.ultimoPreco = editTxtUltimoPreco.text.toString().toFloat()

            // Caso esteja editando edita em DataStore senao adiciona em DataStore
            if (estaEditando) {
                DataStore.editProduto(produtoSendoEditado, posicaoParaEditar)
            } else {
                DataStore.addProduto(produtoSendoEditado)
            }

            // Funcao minha para determinar que resultado da atividade foi bom
            terminarComSucesso()
        }

        btnCancelar.setOnClickListener {
            // Funcao minha para determinar que resultado da atividade foi de cancelar
            terminarComCancelado()
        }
    }

    fun terminarComSucesso() {
        setResult(RESULT_OK)
        finish()
    }

    fun terminarComCancelado() {
        setResult(RESULT_CANCELED)
        finish()
    }
}