package com.ian.yttong

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ian.yttong.databinding.ActivityMainBinding
import com.ian.yttong.viewmodel.MainActivityViewModel
import model.UpdateConfig
import update.UpdateAppUtils

class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityMainBinding

    private lateinit var mainFragment: MainFragment
    private lateinit var mineFragment: MineFragment

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        dataBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        dataBinding.viewModel = viewModel

        mainFragment = MainFragment.newInstance()
        mineFragment = MineFragment.newInstance()

        dataBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.mainBtn) {
                switchFragment(mainFragment, MainFragment.TAG);
            } else if (checkedId == R.id.mineBtn) {
                switchFragment(mineFragment, MineFragment.TAG);
            }
        }

        switchFragment(mainFragment,MainFragment.TAG)


        viewModel.getVersionInfo()

        viewModel.version.observe(this){
            if (BuildConfig.VERSION_CODE < it.versionCode) {

            // 更新配置
            val updateConfig = UpdateConfig().apply {
                force = it.isMust == 1
                checkWifi = true
//                    needCheckMd5 = true
                isShowNotification = true
                alwaysShowDownLoadDialog = true
                notifyImgRes = R.mipmap.logo
//                    apkSavePath = Environment.getExternalStorageDirectory().absolutePath +"/teprinciple"
//                    apkSaveName = "teprinciple"
            }
            UpdateAppUtils
                .getInstance()
                .apkUrl(it.path)
                .updateTitle("发现新版本${it.versionName}")
                .updateContent(it.remark)
                .updateConfig(updateConfig)
                .update()
            }
        }

    }


    var mCurrentFragment: Fragment? = null
    private fun switchFragment(to: Fragment, TAG: String) {
        if (mCurrentFragment != to) {
            if (supportFragmentManager.findFragmentByTag(TAG) == null) {
                if (mCurrentFragment == null) {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.fragmentContainerView, to, TAG).commit()
                } else {
                    supportFragmentManager.beginTransaction().hide(mCurrentFragment!!)
                        .add(R.id.fragmentContainerView, to, TAG).commit()
                }

            } else {
                if (mCurrentFragment == null) {
                    supportFragmentManager.beginTransaction().show(to).commit()
                } else
                    supportFragmentManager.beginTransaction().hide(mCurrentFragment!!).show(to)
                        .commit()
            }

            mCurrentFragment = to
        }
    }
}