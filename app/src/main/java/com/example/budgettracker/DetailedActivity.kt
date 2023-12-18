package com.example.budgettracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope

class DetailedActivity : AppCompatActivity() {
    private lateinit var transaction : Transaction
    private lateinit var dataHelper: DataHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed)
        dataHelper = DataHelper(this)
        transaction = intent.getSerializableExtra("transaction") as Transaction
        val labelInput: TextInputEditText =findViewById(R.id.labelInput)
        val amountInput: TextInputEditText =findViewById(R.id.amountInput)
        val descriptionInput: TextInputEditText =findViewById(R.id.descriptionInput)
        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descriptionInput.setText(transaction.description)


        val rootView: ConstraintLayout=findViewById(R.id.rootView)
        rootView.setOnClickListener {
            this.window.decorView.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val updateBtn:Button=findViewById(R.id.updateBtn);
        labelInput.addTextChangedListener {
            updateBtn.visibility=View.VISIBLE
            if(it!!.count() > 0){
                val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
                labelLayout.error = null
            }

        }

        amountInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if(it!!.count() > 0){
                val amountLayout: TextInputLayout = findViewById(R.id.amountLayout)
                amountLayout.error = null
            }

        }

        descriptionInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
        }

        updateBtn.setOnClickListener {
            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            if(label.isEmpty()){
                val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
                labelLayout.error = "Please neter a valid label"
            }

            else if(amount == null){
                val amountLayout: TextInputLayout = findViewById(R.id.amountLayout)
                amountLayout.error = "Please enter a valid amount"
            }

            else {
                val transaction  = Transaction(transaction.id, label, amount, description)
                update(transaction)
            }
        }
        val closeBtn: ImageButton = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun update(transaction: Transaction){
        dataHelper.updateTransaction(transaction);
        finish();
    }

}