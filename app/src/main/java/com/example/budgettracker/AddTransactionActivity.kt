package com.example.budgettracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val addTransactionBtn:Button = findViewById(R.id.addTransactionBtn)
        addTransactionBtn.setOnClickListener {
            val labelInput: TextInputEditText = findViewById(R.id.labelInput)
            val descriptionInput:TextInputEditText= findViewById(R.id.descriptionInput)
            val amountInput:TextInputEditText= findViewById(R.id.amountInput)


            val label = labelInput.text.toString()
            val description = descriptionInput.text.toString()
            val amount = amountInput.text.toString().toDoubleOrNull()

            val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
            val amountLayout: TextInputLayout = findViewById(R.id.labelLayout)
            if(label.isEmpty())
                labelLayout.error = "Please enter a valid label"
            else if(amount == null)
                amountLayout.error = "Please enter a valid amount"
            else {
                val transaction1  = Transaction(0,label,amount,description)
                insert(transaction1)
            }
        }
        val amountInput:TextInputEditText= findViewById(R.id.amountInput)
        val labelInput: TextInputEditText = findViewById(R.id.labelInput)
        val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
        val amountLayout: TextInputLayout = findViewById(R.id.labelLayout)
        labelInput.addTextChangedListener {
            if(it!!.isNotEmpty())
                labelLayout.error = null
        }

        amountInput.addTextChangedListener {
            if(it!!.isNotEmpty())
                amountLayout.error = null
        }


        val closeBtn: ImageButton = findViewById(R.id.closeBtn)
        closeBtn.setOnClickListener {
            finish()
        }

    }

    private fun insert(transaction: Transaction){
        val db = DataHelper(this)
            db.insertData(transaction)
            finish()

    }

    
}