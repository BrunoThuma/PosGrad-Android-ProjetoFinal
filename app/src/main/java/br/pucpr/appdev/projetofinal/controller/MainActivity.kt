package br.pucpr.appdev.projetofinal.controller

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.pucpr.appdev.projetofinal.R
import br.pucpr.appdev.projetofinal.model.DataStore
import br.pucpr.appdev.projetofinal.view.ProdutoAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    /*
    Criamos dentro da controller a adapter para preencher
    a lista mas ela so sabera quem eh o dataStore posteriomente
    para obedecer ao modelo MVC do projeto
     */
    private var adapter: ProdutoAdapter? = null
    private var tableViewProdutos: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataStore.setContext(this)

        val floatingButtonAdicionar = findViewById<FloatingActionButton>(R.id.fabAdicionarItem)

        // Pegando layout de celula (recyclerView) de produtos
        tableViewProdutos = findViewById<RecyclerView>(R.id.tableViewProdutos)

        // Definindo a orientacao e comportamento da lista com manager
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        tableViewProdutos?.layoutManager = layoutManager

        // Atribuir ao recyclerView o adapter como fonte de dados
        adapter = ProdutoAdapter(DataStore.produtos)
        tableViewProdutos?.adapter = adapter

        // Informar ao adapter que as informacoes a serem visualizadas mudaram
        adapter!!.notifyDataSetChanged()

        // Fazer um contrato de registro de finalizacao da atividade
        var lancamentoDeResultadoDeEdicao = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                adapter?.notifyDataSetChanged()

                val layoutMain = findViewById<CoordinatorLayout>(R.id.layoutMain)
                Snackbar.make(layoutMain, "Produto atualizado", Snackbar.LENGTH_LONG).show()
            }
        }

        // Fazer outro contrato de registro para finalizacao da atividade para adicao de produto
        var lancamentoDeResultadoDeCriacao = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                adapter?.notifyDataSetChanged()

                val layoutMain = findViewById<CoordinatorLayout>(R.id.layoutMain)
                Snackbar.make(layoutMain, "Produto adicionado", Snackbar.LENGTH_LONG).show()
            }
        }

        // Adicionar acao caso clicar no floating button
        floatingButtonAdicionar.setOnClickListener {
            val intent = Intent(this@MainActivity, AdicionarProdutoActivity::class.java).apply {
                // No caso do meu projeto mando valores  extra para informar
                // se a operacao é uma criacao ou edicao de produto
                this.putExtra("estaEditandoProduto", false)
            }
            lancamentoDeResultadoDeCriacao.launch(intent)
        }

        // Criar gesture recognizer para saber qual celular interagimos
        val gestureDetector = GestureDetector(this, object: GestureDetector.SimpleOnGestureListener() {

            // Usar o autoComplete para ver os gestos reconheciveis
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                e?.let {

                    val view = tableViewProdutos?.findChildViewUnder(it.x, it.y)
                    view?.let {

                        val posicao = tableViewProdutos?.getChildAdapterPosition(it)
                        posicao?.let {
                            val produto = DataStore.getProduto(posicao)

                            // Criamos a intencao de abrir uma nova atividade ao clicar em item
                            val intent = Intent(this@MainActivity, AdicionarProdutoActivity::class.java).apply {
                                // No caso do meu projeto mando valores  extra para informar
                                // se a operacao é uma criacao ou edicao de produto
                                this.putExtra("estaEditandoProduto", true)
                                this.putExtra("posicaoDoProduto", posicao)
                            }
                            lancamentoDeResultadoDeEdicao.launch(intent)
                        }
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent?) {
                super.onLongPress(e)

                e?.let {
                    val view = tableViewProdutos?.findChildViewUnder(it.x, it.y)
                    view?.let {

                        val posicao = tableViewProdutos?.getChildAdapterPosition(it)
                        posicao?.let {
                            val produto = DataStore.getProduto(posicao)

                            val dialogoDeExclusao = AlertDialog.Builder(this@MainActivity)
                            // Evitar colocar string hard coded, colocar no strings.xml
                            dialogoDeExclusao.setTitle("Lista de Compras")
                            dialogoDeExclusao.setMessage("Gostaria de excluir esse item?")
                            dialogoDeExclusao.setPositiveButton("Excluir", DialogInterface.OnClickListener { dialogInterface, i ->
                                DataStore.removeProduto(posicao)
                                adapter?.notifyDataSetChanged()

                                val layoutMain = findViewById<CoordinatorLayout>(R.id.layoutMain)
                                Snackbar.make(layoutMain, "Produto ${produto.nome} excluido", Snackbar.LENGTH_LONG).show()
                            })
                            dialogoDeExclusao.setNegativeButton("Cancelar", null)
                            dialogoDeExclusao.show()
                        }
                    }
                }
            }
        })

        // Com objeto de geture recognizer criado associar ao recyclerView
        tableViewProdutos?.addOnItemTouchListener(object: RecyclerView.OnItemTouchListener {

            // Ao ocorrer um evento
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                TODO("Not yet implemented")
            }

            // Ao interceptar um evento que quero mandar para outrem
            // Nao sabe o que fazer com o evento mas sabe chamar quem sabe
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

                // Pega view na localizacao do evento
                val child = rv.findChildViewUnder(e.x, e.y)

                // Se celular nao for nula tenta encaixar o evento em algumas das
                // verifcacoes que fizemos
                return (child!=null && gestureDetector.onTouchEvent(e))
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_lista, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.mnuLimpar -> {

                val dialogoDeDelecaoDeBase = AlertDialog.Builder(this@MainActivity)
                // Evitar colocar string hard coded, colocar no strings.xml
                dialogoDeDelecaoDeBase.setTitle("Lista de Compras")
                dialogoDeDelecaoDeBase.setMessage("Gostaria de excluir todos os itens?")
                dialogoDeDelecaoDeBase.setPositiveButton("Excluir", DialogInterface.OnClickListener { dialogInterface, i ->
                    DataStore.clearProdutos()
                    adapter?.notifyDataSetChanged()
                })
                dialogoDeDelecaoDeBase.setNegativeButton("Cancelar", null)
                dialogoDeDelecaoDeBase.show()
            }

            R.id.mnuSobre -> {
                val dialogoDeDelecaoDeBase = AlertDialog.Builder(this@MainActivity)
                // Evitar colocar string hard coded, colocar no strings.xml
                dialogoDeDelecaoDeBase.setTitle("Lista de Compras")
                dialogoDeDelecaoDeBase.setMessage("Aplicativo criado por Bruno Thuma, baseado na aula do Maicris")
                dialogoDeDelecaoDeBase.setPositiveButton("Ok", null)
                dialogoDeDelecaoDeBase.show()
            }
        }

        return true
    }
}