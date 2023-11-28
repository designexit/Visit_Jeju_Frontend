package com.example.visit_jeju_app.main.adapter

import android.content.Context
import com.example.visit_jeju_app.R
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.viewpager.widget.PagerAdapter

class ImageSliderAdapter(var mainVisual: ArrayList<Int>) :
    RecyclerView.Adapter<ImageSliderAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.main_slider, parent, false)) {
        val mainVisual = itemView.findViewById<ImageView>(R.id.item_image_slider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = mainVisual.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.mainVisual.setImageResource(mainVisual[position])
    }
}


