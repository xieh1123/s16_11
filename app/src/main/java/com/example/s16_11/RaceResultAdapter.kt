package com.example.s16_11

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RaceResultAdapter(private val results: List<RaceResult>) :
    RecyclerView.Adapter<RaceResultAdapter.ResultViewHolder>() {
//這
    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivWinner: ImageView = itemView.findViewById(R.id.ivWinner)
        val tvInfo: TextView = itemView.findViewById(R.id.tvInfo)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }//對應card的元件

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false)
        return ResultViewHolder(view)
    }//連接recyclerView到card

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.tvInfo.text = "兔子: ${result.rabbit}, 烏龜: ${result.turtle}, 勝者: ${result.winner}"
        holder.tvTime.text = result.timestamps

        val winnerImage = if (result.winner == "兔子") R.drawable.rabbit else R.drawable.turtle
        holder.ivWinner.setImageResource(winnerImage)
    }//將資料對應上card的元件
//到這內容都要更改
    override fun getItemCount(): Int = results.size//顯示幾筆資料
}
