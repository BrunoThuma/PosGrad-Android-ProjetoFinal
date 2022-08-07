package br.pucpr.appdev.projetofinal.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "produtos.db"

        const val DB_TABLE_PRODUTOS = "produtos"

        const val DB_FIELD_ID = "id"
        const val DB_FIELD_NAME = "nome"
        const val DB_FIELD_PRICE = "preco"

        const val sqlCreateProdutos = "CREATE TABLE IF NOT EXISTS $DB_TABLE_PRODUTOS (" +
                "$DB_FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DB_FIELD_NAME TEXT, " +
                "$DB_FIELD_PRICE FLOAT);"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val db = db ?: return

        db.beginTransaction()
        try {

            db.execSQL(sqlCreateProdutos)
            db.setTransactionSuccessful()
        }
        finally {

            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        val db = db ?: return

        if (oldVersion == 1) {

            if (newVersion == 2) {

                db.beginTransaction()
                try {

                    db.setTransactionSuccessful()
                }
                finally {

                    db.endTransaction()
                }
            }
        }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        val db = db ?: return

        if (oldVersion == 2) {

            if (newVersion == 1) {

                db.beginTransaction()
                try {

                    db.setTransactionSuccessful()
                }
                finally {

                    db.endTransaction()
                }
            }
        }
    }

    fun getAllProdutos(): MutableList<ProdutoModel> {

        var produtos = mutableListOf<ProdutoModel>()

        val selectQuery = "SELECT * FROM $DB_TABLE_PRODUTOS"
        val db = readableDatabase

        val cursor : Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            db.execSQL(selectQuery)
            e.printStackTrace()

            return ArrayList()
        }

        var id: Long
        var nome: String
        var ultimoPreco: Float

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(cursor.getColumnIndex(DB_FIELD_ID))
                nome = cursor.getString(cursor.getColumnIndex(DB_FIELD_NAME))
                ultimoPreco = cursor.getFloat(cursor.getColumnIndex(DB_FIELD_PRICE))

                val produto = ProdutoModel(nome, ultimoPreco, id)
                produtos.add(produto)
            } while (cursor.moveToNext())
        }

        return produtos
    }

    fun addProduto(produto: ProdutoModel): Long {

        val db = writableDatabase

        val values = ContentValues().apply {

            put(DB_FIELD_NAME, produto.nome)
            put(DB_FIELD_PRICE, produto.ultimoPreco)
        }

        var id: Long = 0
        db.beginTransaction()
        try {

            id = db.insert(DB_TABLE_PRODUTOS, "", values)
            db.setTransactionSuccessful()
        }
        finally {

            db.endTransaction()
        }

        db.close()

        return id
    }

    fun editProduto(produto: ProdutoModel): Int {

        val db = writableDatabase

        val values = ContentValues().apply {

            put(DB_FIELD_NAME, produto.nome)
            put(DB_FIELD_PRICE, produto.ultimoPreco)
        }
        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(produto.id.toString())
        var count = 0
        db.beginTransaction()
        try {

            count = db.update(DB_TABLE_PRODUTOS, values, selection, selectionArgs)
            db.setTransactionSuccessful()
        }
        finally {

            db.endTransaction()
        }
        db.close()

        return count
    }

    fun removeProduto(produto: ProdutoModel): Int {

        val db = writableDatabase

        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(produto.id.toString())

        var count = 0
        db.beginTransaction()
        try {

            count = db.delete(DB_TABLE_PRODUTOS, selection, selectionArgs)
            db.setTransactionSuccessful()
        }
        finally {

            db.endTransaction()
        }

        db.close()

        return count
    }

    fun clearProdutos(): Int {

        val db = writableDatabase

        var count = 0

        db.beginTransaction()
        try {

            count = db.delete(DB_TABLE_PRODUTOS, null, null)
            db.setTransactionSuccessful()
        }
        finally {

            db.endTransaction()
        }

        db.close()

        return count
    }
}