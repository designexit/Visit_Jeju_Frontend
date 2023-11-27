package com.example.visit_jeju_app.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.visit_jeju_app.databinding.MainItemBinding

//뷰 홀더는 목록의 요소의 뷰를 설정해 주는 역할
class MyViewHolder(val binding:MainItemBinding) : RecyclerView.ViewHolder(binding.root)
class RecyclerView(val datas : MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemMainTitle.text = datas[position]
    }

}

