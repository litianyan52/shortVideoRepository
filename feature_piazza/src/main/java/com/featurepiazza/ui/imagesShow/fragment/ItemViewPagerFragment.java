package com.featurepiazza.ui.imagesShow.fragment;

import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.featurepiazza.R;
import com.featurepiazza.databinding.ActivityImagesShowBinding;
import com.featurepiazza.databinding.LayoutItemViewPagerBinding;
import com.featurepiazza.ui.imagesShow.ImagesShowViewModel;
import com.libase.base.BaseFragment;
import com.libase.base.BaseViewmodel;
import com.libase.config.ArouterPath;

@Route(path = ArouterPath.Piazza.FRAGMENT_VIEW_PAGER_ITEM)
public class ItemViewPagerFragment extends BaseFragment<BaseViewmodel, LayoutItemViewPagerBinding> {
    @Autowired(name = ArouterPath.Piazza.KEY_IMAGE_CONTENT)
    public String mImageUri;

    @Override
    public BaseViewmodel getViewModel() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_item_view_pager;
    }

    @Override
    public int getDataBindVariableId() {
        return 0;
    }

    @Override
    public void initView() {
        mDataBinding.setUri(mImageUri);
    }

    @Override
    public void initData() {

    }
}
