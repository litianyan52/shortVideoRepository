package com.featurefind.ui.categoryDetail;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.video_data.bean.Category;
import com.featurefind.BR;
import com.featurefind.R;

import com.featurefind.databinding.ActivityCategoryDetailBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;
import com.zhengsr.tablib.view.adapter.TabFlowAdapter;

import java.util.ArrayList;

@Route(path = ArouterPath.Find.ACTIVITY_CATEGORY_DETAIL)
public class CategoryDetailActivity extends BaseActivity<CategoryDetailViewModel, ActivityCategoryDetailBinding> {

    @Autowired(name = ArouterPath.Find.KEY_CATEGORY_DETAIL_DATA)
    public Category mCategoryData;  //分类详情数据

    @Override
    public CategoryDetailViewModel getViewModel() {
        return new ViewModelProvider(this).get(CategoryDetailViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_category_detail;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToKidView(mdataBinding.getRoot(), mdataBinding.backArrow, mdataBinding.shareWh);

        initViewPager();


    }

    private void initViewPager() {
        Fragment hotcommendFragment = (Fragment) ARouter.getInstance().build(ArouterPath.Video.FRAGMENT_CATEGORY_VIDEO_LIST)
                .withInt(ArouterPath.Video.KEY_CHANNEL_ID, mCategoryData.getId())
                .withInt(ArouterPath.Video.KEY_TYPE, 0)  //热门推荐类型
                .navigation();
        Fragment newPublishFragment = (Fragment) ARouter.getInstance().build(ArouterPath.Video.FRAGMENT_CATEGORY_VIDEO_LIST)
                .withInt(ArouterPath.Video.KEY_CHANNEL_ID, mCategoryData.getId())
                .withInt(ArouterPath.Video.KEY_TYPE, 1)  //最新发布类型
                .navigation();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(hotcommendFragment);
        fragments.add(newPublishFragment);
        ViewPager2 viewPager = mdataBinding.viewPager;
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments == null ? 0 : fragments.size();
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    mdataBinding.hotRecommend.setChecked(true);
                    mdataBinding.hotRecommendLine.setBackgroundColor(getColor(R.color.black));
                    mdataBinding.newPublishLine.setBackgroundColor(getColor(R.color.grey));
                }

                if (position == 1) {
                    mdataBinding.newPublish.setChecked(true);
                    mdataBinding.hotRecommendLine.setBackgroundColor(getColor(R.color.grey));
                    mdataBinding.newPublishLine.setBackgroundColor(getColor(R.color.black));
                }
            }
        });

        mdataBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hot_recommend) {
                    viewPager.setCurrentItem(0);
                    mdataBinding.hotRecommendLine.setBackgroundColor(getColor(R.color.black));
                    mdataBinding.newPublishLine.setBackgroundColor(getColor(R.color.grey));
                }

                if (checkedId == R.id.new_publish) {
                    viewPager.setCurrentItem(1);
                    mdataBinding.hotRecommendLine.setBackgroundColor(getColor(R.color.grey));
                    mdataBinding.newPublishLine.setBackgroundColor(getColor(R.color.black));
                }

            }
        });
    }

    @Override
    public void initData() {
        mViewModel.LoadData(mCategoryData);
        mViewModel.getCategoryDetailOne(String.valueOf(mCategoryData.getId()));
    }
}