package com.ian.yttong.util

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.contains
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.RoundedCornersTransformation
import com.ian.yttong.adapter.MainListAdapter
import com.ian.yttong.network.entity.ErrorMessage
import com.ian.yttong.network.entity.GradeList
import com.ian.yttong.viewmodel.ProgressStatus

/**
 * Description
 * Created by jianhongxu on 2022/5/16
 */
@BindingAdapter("requestStatus")
fun bindStatus(progressBar: ProgressBar, status: ProgressStatus?) {

    when (status) {
        ProgressStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        ProgressStatus.DONE,
        ProgressStatus.ERROR -> {
            progressBar.visibility = View.INVISIBLE
        }
    }
}
@BindingAdapter("requestError")
fun bindMsg(textView: TextView,errorMessage: ErrorMessage?){
    errorMessage?.let {
        Log.d("bindMsg",it.message)
        textView.text =it.message
    }
}

@BindingAdapter("mainListData")
fun bindAdapter(recyclerView: RecyclerView,data:List<GradeList>?){
    val adapter =recyclerView.adapter as MainListAdapter
    adapter.submitList(data)
}

@BindingAdapter("mainItemImageView")
fun bindImageView(imageView: ImageView,imgUrl: String?){
    imgUrl?.let {
         imageView.load(it){
             transformations(RoundedCornersTransformation(25f))
         }
    }
}

