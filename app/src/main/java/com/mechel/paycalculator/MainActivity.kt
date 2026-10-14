package com.mechel.paycalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.title = "Pay Calculator"

        val hoursInput = findViewById<EditText>(R.id.hoursInput)
        val rateInput = findViewById<EditText>(R.id.rateInput)
        val taxInput = findViewById<EditText>(R.id.taxInput)

        val payOutput = findViewById<TextView>(R.id.payOutput)
        val overtimeOutput = findViewById<TextView>(R.id.overtimeOutput)
        val totalOutput = findViewById<TextView>(R.id.totalOutput)
        val taxOutput = findViewById<TextView>(R.id.taxOutput)

        findViewById<Button>(R.id.calculateButton).setOnClickListener {
            val hours = hoursInput.text.toString().toDoubleOrNull()
            val rate = rateInput.text.toString().toDoubleOrNull()
            val taxPercent = taxInput.text.toString().toDoubleOrNull()

            if (hours == null || rate == null || taxPercent == null) {
                Toast.makeText(this, "Enter a number in every field", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!hours.isFinite() || !rate.isFinite() || !taxPercent.isFinite() ||
                hours < 0 || rate < 0 || taxPercent < 0 || taxPercent > 100
            ) {
                Toast.makeText(
                    this,
                    "Use positive values or zero, and a tax rate from 0 to 100",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val pay: Double
            val overtimePay: Double

            if (hours <= 40) {
                pay = hours * rate
                overtimePay = 0.0
            } else {
                pay = 40 * rate
                overtimePay = (hours - 40) * rate * 1.5
            }

            val totalPay = pay + overtimePay
            val taxRate = taxPercent / 100
            val tax = pay * taxRate

            payOutput.text = String.format(Locale.CANADA, "Pay: $%.2f", pay)
            overtimeOutput.text = String.format(Locale.CANADA, "Overtime pay: $%.2f", overtimePay)
            totalOutput.text = String.format(Locale.CANADA, "Total pay: $%.2f", totalPay)
            taxOutput.text = String.format(Locale.CANADA, "Tax: $%.2f", tax)
        }

        findViewById<Button>(R.id.aboutButton).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}