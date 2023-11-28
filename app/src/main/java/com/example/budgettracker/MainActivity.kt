package com.example.budgettracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions: List<Transaction>
    private lateinit var transactionAdapter:TransactionAdapter
    private lateinit var linearlayoutManager:LinearLayoutManager
    private lateinit var dataHelper: DataHelper
    private lateinit var oldTransactions : List<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        transactions = arrayListOf()

        transactionAdapter = TransactionAdapter(transactions)
        linearlayoutManager = LinearLayoutManager(this)

        dataHelper = DataHelper(this)


        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)

        recyclerView.apply {
            adapter = transactionAdapter
            layoutManager=linearlayoutManager
        }

        //swipe to remove
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }
        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recyclerView)

        val addBtn: FloatingActionButton= findViewById(R.id.addBtn)

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

    }

    private fun fetchall(){
            var tr = Transaction(0,"adam",100.0,"desc")
            dataHelper.insertData(tr)
            transactions = dataHelper.getAllTransactions()
            updateDashboard()
            transactionAdapter.setData(transactions)
    }

    private fun updateDashboard(){
        val totalAmount = transactions.map { it.amount }.sum()
        val budgetAmount = transactions.filter { it.amount>0 }.map{it.amount}.sum()
        val expenseAmount = totalAmount - budgetAmount
        val balance: TextView = findViewById(R.id.balance)
        val budget: TextView = findViewById(R.id.budget)
        val expense: TextView = findViewById(R.id.expense)
        balance.text = "$ %.2f".format(totalAmount)
        budget.text = "$ %.2f".format(budgetAmount)
        expense.text = "$ %.2f".format(expenseAmount)
    }

    private fun undoDelete(){
            dataHelper.insertData(deletedTransaction)
            transactions = oldTransactions


            transactionAdapter.setData(transactions)
            updateDashboard()

    }


    private fun showSnackbar(){
        val view = findViewById<View>(R.id.coordinator)
        val snackbar = Snackbar.make(view, "Transaction deleted!",Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo"){
            undoDelete()
        }
            .setActionTextColor(ContextCompat.getColor(this, R.color.red))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransactions = transactions
        dataHelper.removeData(transaction.id)

        transactions = transactions.filter { it.id != transaction.id }

        updateDashboard()
        transactionAdapter.setData(transactions)
        showSnackbar()

    }

    override fun onResume() {
        super.onResume()
        fetchall()
    }
}