package com.example.demo20shvideoproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.demo20shvideoproject.databinding.ActivityMainBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.RunTimeCheck;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity<MainViewmodel, ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    /**
     * 底部导航按钮id -> 该模块Fragment的ARouter路径,路径同时用来当Fragment的tag
     * 用tag做索引,Activity重建后还能把FragmentManager里恢复出来的实例找回来
     */
    private final Map<Integer, String> mTabFragmentPaths = new HashMap<>();

    /**
     * 是否是配置变更(旋转屏幕等)或进程被杀后的重建
     * 重建时FragmentManager已经把之前添加过的Fragment连隐藏状态一起恢复了,不需要再指定默认页
     */
    private boolean mIsRecreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //必须在super之前赋值,因为BaseActivity.onCreate中会回调initView/initData
        mIsRecreated = savedInstanceState != null;
        super.onCreate(savedInstanceState);
    }

    @Override
    public MainViewmodel getViewModel() {

        return new ViewModelProvider(this).get(MainViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getViewModelId() {
        return BR.viewmodel;
    }
    @Override
    public void initView() {
        Log.d(TAG, "initView: ");

        mTabFragmentPaths.put(R.id.home, ArouterPath.Home.FRAGMENT_HOME);
        mTabFragmentPaths.put(R.id.piazza, ArouterPath.Piazza.FRAGMENT_PIAZZA);
        mTabFragmentPaths.put(R.id.find, ArouterPath.Find.FRAGMENT_FIND);
        mTabFragmentPaths.put(R.id.user, ArouterPath.User.FRAGMENT_USER);

        mdataBinding.mainBottomNavigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchTab(checkedId);
            }
        });

    }

    /**
     * 切换底部导航对应的模块Fragment
     * 用 add + show/hide 代替 replace:已经创建过的Fragment只是被隐藏,不会被销毁,
     * 所以它的实例、ViewModel、列表数据、RecyclerView滚动位置都会保留,
     * 首页切回来时ViewPager2里那两个视频列表也不会重新请求数据
     * 每个模块第一次被点击时才创建,没点过的模块不会白白占用内存
     */
    private void switchTab(int checkedId) {
        String path = mTabFragmentPaths.get(checkedId);
        if (path == null) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment target = fragmentManager.findFragmentByTag(path);   //已经创建过就直接拿来用
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setReorderingAllowed(true);

        if (target == null) {
            target = (Fragment) ARouter.getInstance().build(path).navigation();
            if (target == null) {
                Log.e(TAG, "switchTab: 没有找到 " + path + " 对应的Fragment");
                return;
            }
            transaction.add(R.id.fcv, target, path);   //第一次点击,创建并添加
        } else {
            transaction.show(target);   //之后只显示出来
        }

        //把其它模块隐藏掉,保证同一时刻只有一个模块可见
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment != target && !fragment.isHidden()) {
                transaction.hide(fragment);
            }
        }

        //用commitNow同步提交,避免快速连续点击时事务异步执行导致页面重叠
        transaction.commitNow();
    }

    @Override
    public void initData() {
        if (!mIsRecreated) {
            mdataBinding.home.setChecked(true);   //主动设置一下让启动时处于home页面,会触发上面的监听
        }

        RunTimeCheck.INSTANCE.getMemoryInfo();

    }


}
