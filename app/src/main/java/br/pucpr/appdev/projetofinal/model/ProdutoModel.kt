package br.pucpr.appdev.projetofinal.model

class ProdutoModel(
    var nome: String,
    var ultimoPreco: Float,
    var id: Long) {

    constructor(nome: String) : this(nome, 0.0f, 0) {}

    constructor(nome: String, ultimoPreco: Float) : this(nome, ultimoPreco, 0) {}

    constructor() : this("")

    init {

    }
}