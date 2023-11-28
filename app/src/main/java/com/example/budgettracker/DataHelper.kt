package com.example.budgettracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataHelper(context: Context) : SQLiteOpenHelper(context, "Transactions.db", null, 1) {

    override fun onCreate(MyDatabase: SQLiteDatabase) {
        MyDatabase.execSQL(
            "CREATE TABLE transactions(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "label TEXT," +
                    "amount REAL," +
                    "description TEXT)"
        )
    }

    override fun onUpgrade(MyDB: SQLiteDatabase, i: Int, i1: Int) {
        MyDB.execSQL("DROP TABLE IF EXISTS transactions")
        onCreate(MyDB)
    }

    fun insertData(transaction: Transaction): Boolean {
        val MyDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("label", transaction.label)
        contentValues.put("amount", transaction.amount)
        contentValues.put("description", transaction.description)
        val result = MyDatabase.insert("transactions", null, contentValues)
        return result != -1L
    }

    fun removeData(id: Int): Boolean {
        val MyDatabase = writableDatabase
        val whereClause = "id = ?"
        val whereArgs = arrayOf(id.toString())
        val rowsDeleted = MyDatabase.delete("transactions", whereClause, whereArgs)
        return rowsDeleted > 0
    }

    fun updateTransaction(transaction: Transaction): Boolean {
        val MyDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put("label", transaction.label)
        contentValues.put("amount", transaction.amount)
        contentValues.put("description", transaction.description)

        val whereClause = "id = ?"
        val whereArgs = arrayOf(transaction.id.toString())

        val rowsUpdated = MyDatabase.update("transactions", contentValues, whereClause, whereArgs)

        return rowsUpdated > 0
    }

    fun getAllTransactions(): List<Transaction> {
        val MyDatabase = readableDatabase
        val transactions = mutableListOf<Transaction>()

        val cursor = MyDatabase.rawQuery("SELECT * FROM transactions", null)

        try {
            while (cursor.moveToNext()) {
                val idIndex = cursor.getColumnIndex("id")
                val labelIndex = cursor.getColumnIndex("label")
                val amountIndex = cursor.getColumnIndex("amount")
                val descriptionIndex = cursor.getColumnIndex("description")

                if (idIndex >= 0 && labelIndex >= 0 && amountIndex >= 0 && descriptionIndex >= 0) {
                    val id = cursor.getInt(idIndex)
                    val label = cursor.getString(labelIndex)
                    val amount = cursor.getDouble(amountIndex)
                    val description = cursor.getString(descriptionIndex)

                    val transaction = Transaction(id, label, amount, description)
                    transactions.add(transaction)
                } else {

                }
            }
        } finally {
            cursor.close()
        }

        return transactions
    }



}
