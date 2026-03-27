package com.featurehome;

import android.util.Log;
import android.widget.RadioGroup;

import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featurehome.databinding.LayoutHomeFragmentBinding;
import com.libase.base.BaseFragment;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

import java.util.ArrayList;

@Route(path = ArouterPath.Home.FRAGMENT_HOME)
public class HomeFragment extends BaseFragment<HomeViewModel, LayoutHomeFragmentBinding> {
    private static final String TAG = "HomeFragment";

    @Override
    public HomeViewModel getViewModel() {
        return new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_home_fragment;
    }

    @Override
    public int getDataBindVariableId() {
        return BR.viewmudel;
    }

    @Override
    public void initView() {
        //这里访问不到VideoListFragment,但是创建的确实是VideoListFragment
        //由于是Media下的Fragment,所以用与此相关的配置
        Fragment commendFragment = (Fragment) ARouter
                .getInstance()
                .build(ArouterPath.Video.FRAGMENT_VideoList)
                .withInt(ArouterPath.Video.VIDEO_LIST_FRAGMENT_TYPE_KEY, ArouterPath.Video.VIDEO_LIST_FRAGMENT_RECOMMEND)
                .navigation();

        Fragment dailyFragment = (Fragment) ARouter
                .getInstance()
                .build(ArouterPath.Video.FRAGMENT_VideoList)
                .withInt(ArouterPath.Video.VIDEO_LIST_FRAGMENT_TYPE_KEY, ArouterPath.Video.VIDEO_LIST_FRAGMENT_DAILY)
                .navigation();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(commendFragment);
        fragments.add(dailyFragment);
        ViewPager2 viewPager2 = mDataBinding.viewPager2;
        viewPager2.setSaveEnabled(false);
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                Log.d(TAG, "HomeFragment: " + fragments.get(position));
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {

                return fragments.size();
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {

                    case 0:
                        mDataBinding.recommend.setChecked(true);
                        break;
                    case 1:
                        mDataBinding.daily.setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        mDataBinding.rdIndicator.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.recommend) {
                    viewPager2.setCurrentItem(0);
                }
                if (checkedId == R.id.daily) {
                    viewPager2.setCurrentItem(1);
                }
            }
        });
//之前测试网络错误状态用的
//        mDataBinding.logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commendFragment.setCode(2);
//            }
//        });
        StatusBarUtils.AddStatusHeightToRootView(mDataBinding.getRoot());
    }

    @Override
    public void initData() {

    }
}

