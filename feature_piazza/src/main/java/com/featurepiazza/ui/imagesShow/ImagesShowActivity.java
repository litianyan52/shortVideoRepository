package com.featurepiazza.ui.imagesShow;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.featurepiazza.BR;
import com.featurepiazza.R;
import com.featurepiazza.bean.ResPiazza;
import com.featurepiazza.databinding.ActivityImagesShowBinding;
import com.featurepiazza.ui.imagesShow.fragment.ItemViewPagerFragment;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;

import java.util.ArrayList;

@Route(path = ArouterPath.Piazza.ACTIVITY_IMAGES_SHOW)
public class ImagesShowActivity extends BaseActivity<ImagesShowViewModel, ActivityImagesShowBinding> {

    @Autowired(name = ArouterPath.Piazza.KEY_ACTIVITY_IMAGES_SHOW_DATA)
    public ResPiazza.ResPiazzaDetail mResPiazzaDetail;  //图片浏览的数据
    private ArrayList<ItemViewPagerFragment> mList;
    private ViewPager2 mViewPager;
    private ViewPager2.OnPageChangeCallback mOnPageChangeCallback;

    @Override
    public ImagesShowViewModel getViewModel() {
        return new ViewModelProvider(this).get(ImagesShowViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_images_show;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        initViewPager();
        /**
         * 指定当前Activity退出动画
         */
        mViewModel.getIsFinishCurrentImageActivity().observe(ImagesShowActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFinish) {
                if(isFinish){
                    finish();
                    overridePendingTransition(0,R.anim.picture_activity_out);
                }
            }
        });
    }

    /**
     * 初始化viewPager
     */
    private void initViewPager() {
        if (mResPiazzaDetail == null) {
            return;
        }
        mList = new ArrayList<>();
        for (int i = 0; i < mResPiazzaDetail.getImages().size(); i++) {
            ItemViewPagerFragment itemViewPagerFragment = (ItemViewPagerFragment) ARouter.getInstance()
                    .build(ArouterPath.Piazza.FRAGMENT_VIEW_PAGER_ITEM)
                    .withString(ArouterPath.Piazza.KEY_IMAGE_CONTENT, mResPiazzaDetail.getImages().get(i)) //把图片的链接传过去
                    .navigation();
            mList.add(itemViewPagerFragment);
        }
        mViewPager = mdataBinding.viewPager;
        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mList.get(position);
            }

            @Override
            public int getItemCount() {
                return mList == null ? 0 : mList.size();
            }
        });
        mOnPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mdataBinding.imageIndex.setText(position + 1 + "/" + mResPiazzaDetail.getImages().size());
            }
        };
        mViewPager.registerOnPageChangeCallback(mOnPageChangeCallback);

    }

    @Override
    public void initData() {
        mViewModel.LoadData(mResPiazzaDetail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mViewPager != null && mOnPageChangeCallback != null) {
            mViewPager.unregisterOnPageChangeCallback(mOnPageChangeCallback);
        }

        mViewPager = null;
        mOnPageChangeCallback = null;
    }
}