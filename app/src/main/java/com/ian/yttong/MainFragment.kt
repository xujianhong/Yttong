package com.ian.yttong

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.ian.yttong.adapter.MainListAdapter
import com.ian.yttong.databinding.MainFragmentBinding
import com.ian.yttong.network.entity.BannerList
import com.ian.yttong.util.PhoneUtil
import com.ian.yttong.viewmodel.MainViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

class MainFragment : Fragment() {

    private lateinit var dataBinding: MainFragmentBinding

    companion object {
        const val TAG = "MainFragment"
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = MainFragmentBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.recyclerView.adapter = MainListAdapter()
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        dataBinding.viewModel = viewModel

        viewModel.bannerList.observe(viewLifecycleOwner) {
            it.apply {
                dataBinding.banner.apply {
                    addBannerLifecycleObserver(viewLifecycleOwner)
                    setAdapter(object : BannerImageAdapter<BannerList>(it) {
                        override fun onBindView(
                            holder: BannerImageHolder,
                            data: BannerList,
                            position: Int,
                            size: Int
                        ) {
                            holder.imageView.load(data.image)
                        }


                    })
                    indicator = CircleIndicator(activity)
                    setOnBannerListener { data, _ ->
                        val intent = Intent(activity, WebActivity::class.java)
                            .putExtra(WebActivity.TITLE_EXTRA, (data as BannerList).title)

                        if(!data.content.isNullOrEmpty()){
                            intent.putExtra(WebActivity.URL_EXTRA, data.content)
                            startActivity(intent)
                        }else if(!data.url.isNullOrEmpty()){
                            intent.putExtra(WebActivity.URL_EXTRA, data.url)
                            startActivity(intent)
                        }


                    }

                }


            }
        }


        viewModel.getBannerList()
        viewModel.getGrade()

    }


}