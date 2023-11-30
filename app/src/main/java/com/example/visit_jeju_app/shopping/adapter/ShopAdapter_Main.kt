package com.example.visit_jeju_app.shopping.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.visit_jeju_app.R
import com.example.visit_jeju_app.databinding.ItemShopBinding
import com.example.visit_jeju_app.databinding.MainItemBinding
import com.example.visit_jeju_app.shopping.ShopActivity
import com.example.visit_jeju_app.shopping.ShopDetailActivity
import com.example.visit_jeju_app.shopping.model.ShopList

class ShopViewHolder2(val binding: MainItemBinding): RecyclerView.ViewHolder(binding.root)
class ShopAdapter_Main(val context: Context, val datas:MutableList<ShopList>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun getItemCount(): Int{
        return datas?.size ?: 0
        Log.d("ljs","datas?.size: ${datas?.size}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = ShopViewHolder2(MainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as ShopViewHolder2).binding
        /*val animation = AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.list_item_ani)
        holder.binding.root.animation = animation*/

        //add......................................
        val model = datas?.get(position)
        binding.name.text = model?.itemsTitle
        binding.addr2.text = model?.itemsRoadAddress
        binding.tel.text = model?.itemsPhoneNo
        Log.d("ljs","model?.itemsTitle: ${model?.itemsTitle}")

        // glide 통해서, 이미지 를 직접 가져와서 처리하는 부분.
        //방법2)
        Glide.with(context)
            //load 실제 URL 주소 직접 넣기.
            .load(model?.itemsRepPhotoPhotoidImgPath)
            .override(150,150)
            .into(binding.thumbNailPhoto)


        //클릭시 관광지 상세정보 페이지에 정보넘기기
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.binding.root?.context, ShopDetailActivity::class.java)
            intent.putExtra("itemsLatitude", model?.itemsLatitude)
            intent.putExtra("itemsLongitude", model?.itemsLongitude)
            intent.putExtra("itemsTitle", model?.itemsTitle)
            intent.putExtra("itemsContentsCdLabel", model?.itemsContentsCdLabel)
            intent.putExtra("itemsRegion1CdLabel", model?.itemsRegion1CdLabel)
            intent.putExtra("itemsRegion2CdLabel", model?.itemsRegion2CdLabel)
            intent.putExtra("itemsRegion2CdValue", model?.itemsRegion2CdValue)
            intent.putExtra("itemsAddress", model?.itemsAddress)
            intent.putExtra("itemsRoadAddress", model?.itemsRoadAddress)
            intent.putExtra("itemsIntroduction", model?.itemsIntroduction)
            intent.putExtra("itemsAllTag", model?.itemsAllTag)
            intent.putExtra("itemsPhoneNo", model?.itemsPhoneNo)
            intent.putExtra("itemsRepPhotoPhotoidImgPath", model?.itemsRepPhotoPhotoidImgPath)

            ContextCompat.startActivity(holder.binding.root.context, intent, null)
        }

    }

}