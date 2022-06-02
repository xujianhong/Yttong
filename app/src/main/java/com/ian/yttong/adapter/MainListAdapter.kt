package com.ian.yttong.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ian.yttong.WebActivity
import com.ian.yttong.databinding.ItemMainListBinding
import com.ian.yttong.network.entity.GradeList
import com.ian.yttong.util.PhoneUtil

/**
 * Description
 * Created by jianhongxu on 2022/5/17
 */
class MainListAdapter : ListAdapter<GradeList, MainListAdapter.MainListViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<GradeList>() {
        override fun areItemsTheSame(oldItem: GradeList, newItem: GradeList): Boolean {
            return oldItem.gradedNamae == newItem.gradedNamae
        }

        override fun areContentsTheSame(oldItem: GradeList, newItem: GradeList): Boolean {
            return oldItem.gradedNamae == newItem.gradedNamae
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        return MainListViewHolder(
            ItemMainListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        val gradeList = getItem(position)

        holder.binding.root.setOnClickListener {
            holder.binding.root.context.startActivity(
                Intent(
                    holder.binding.root.context,
                    WebActivity::class.java
                )
                    .putExtra(WebActivity.TITLE_EXTRA, gradeList.gradedNamae)
                    .putExtra(WebActivity.URL_EXTRA, gradeList.url+"?deviceNo="+ PhoneUtil.getUniqueID(holder.binding.root.context))
            )
        }
        holder.binding(gradeList)
    }


    class MainListViewHolder(var binding: ItemMainListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(grade: GradeList) {
            binding.gradeList = grade
            binding.executePendingBindings()
        }
    }


}