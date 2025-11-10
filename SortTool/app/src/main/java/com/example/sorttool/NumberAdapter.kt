package com.example.sorttool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NumberAdapter(private var numbers: List<Int>) :
    RecyclerView.Adapter<NumberAdapter.NumberViewHolder>() {

    class NumberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Ánh xạ TextView từ layout item_number.xml
        val textView: TextView = view.findViewById(R.id.textViewNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_number, parent, false)
        return NumberViewHolder(view)
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        // Đặt văn bản cho TextView là số tương ứng
        holder.textView.text = numbers[position].toString()
    }

    override fun getItemCount() = numbers.size

    fun updateData(newNumbers: List<Int>) {
        numbers = newNumbers
        notifyDataSetChanged() // Cập nhật lại RecyclerView
    }
}