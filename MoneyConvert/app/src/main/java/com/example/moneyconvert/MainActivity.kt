package com.example.moneyconvert

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etAmountFrom: EditText
    private lateinit var etAmountTo: EditText
    private lateinit var spinnerCurrencyFrom: Spinner
    private lateinit var spinnerCurrencyTo: Spinner

    // Tỷ giá hối đoái cố định (Giả định tất cả đều so với 1 USD)
    private val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.93,
        "JPY" to 153.0,
        "GBP" to 0.80,
        "VND" to 25400.0,
        "AUD" to 1.55,
        "CAD" to 1.37,
        "CHF" to 0.91,
        "CNY" to 7.23,
        "KRW" to 1370.0
    )

    // Danh sách tên tiền tệ
    private val currencyList = exchangeRates.keys.toList()

    // Biến cờ để ngăn chặn vòng lặp vô hạn khi cập nhật TextWatcher
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etAmountFrom = findViewById(R.id.etAmountFrom)
        etAmountTo = findViewById(R.id.etAmountTo)
        spinnerCurrencyFrom = findViewById(R.id.spinnerCurrencyFrom)
        spinnerCurrencyTo = findViewById(R.id.spinnerCurrencyTo)

        setupSpinners()
        setupTextWatchers()
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            currencyList
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerCurrencyFrom.adapter = adapter
        spinnerCurrencyTo.adapter = adapter

        // Thiết lập giá trị mặc định
        spinnerCurrencyFrom.setSelection(currencyList.indexOf("USD"))
        spinnerCurrencyTo.setSelection(currencyList.indexOf("VND"))

        // Thiết lập lắng nghe sự kiện khi chọn Spinner
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Khi thay đổi đồng tiền, cần tính toán lại
                convert(etAmountFrom, etAmountTo, spinnerCurrencyFrom, spinnerCurrencyTo)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerCurrencyFrom.onItemSelectedListener = spinnerListener
        spinnerCurrencyTo.onItemSelectedListener = spinnerListener
    }

    private fun setupTextWatchers() {
        // Lắng nghe thay đổi cho EditText TỪ
        etAmountFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    convert(etAmountFrom, etAmountTo, spinnerCurrencyFrom, spinnerCurrencyTo)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Lắng nghe thay đổi i
        etAmountTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    convert(etAmountTo, etAmountFrom, spinnerCurrencyTo, spinnerCurrencyFrom)
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Ban đầu chỉ cho phép chỉnh sửa etAmountFrom, etAmountTo chỉ hiển thị kết quả
        etAmountTo.isFocusable = false
        etAmountTo.isCursorVisible = false
    }

    private fun convert(
        sourceEt: EditText,
        targetEt: EditText,
        sourceSpinner: Spinner,
        targetSpinner: Spinner
    ) {
        // Lấy dữ liệu
        val amountText = sourceEt.text.toString()
        if (amountText.isEmpty() || amountText == ".") {
            targetEt.setText("")
            return
        }

        // Lấy các tỷ giá và số tiền nhập
        val amount = amountText.toDoubleOrNull() ?: 0.0
        val currencyFrom = sourceSpinner.selectedItem.toString()
        val currencyTo = targetSpinner.selectedItem.toString()

        val rateFrom = exchangeRates[currencyFrom] ?: 1.0
        val rateTo = exchangeRates[currencyTo] ?: 1.0

        // Tính toán chuyển đổi
        // Số tiền chuyển sang USD: amountInUSD = amount / rateFrom (từ USD)
        // Số tiền cuối cùng: finalAmount = amountInUSD * rateTo (so với USD)
        val finalAmount = amount / rateFrom * rateTo

        // 3. Cập nhật kết quả vào EditText còn lại
        isUpdating = true // Bật cờ để TextWatcher kia không bị kích hoạt

        // Định dạng kết quả (làm tròn 2 chữ số thập phân, hoặc không nếu là số nguyên lớn như VND)
        val formattedResult = if (rateTo > 1000) {
            String.format(Locale.US, "%.0f", finalAmount) // VND, JPY không cần thập phân
        } else {
            String.format(Locale.US, "%.2f", finalAmount) // Làm tròn 2 thập phân
        }

        targetEt.setText(formattedResult)
        isUpdating = false // Tắt cờ
    }
}