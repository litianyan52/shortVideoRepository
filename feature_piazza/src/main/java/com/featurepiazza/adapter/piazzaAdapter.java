package com.featurepiazza.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.video_data.bean.Category;
import com.featurepiazza.R;
import com.featurepiazza.bean.PiazzaXbannerData;
import com.featurepiazza.bean.ResPiazza;
import com.featurepiazza.databinding.LayoutBannerBinding;
import com.featurepiazza.databinding.LayoutImageItemBinding;
import com.libase.base.list.BaseAdapter;
import com.libase.utils.GlideUtils;
import com.stx.xhb.androidx.XBanner;

import java.util.ArrayList;
import java.util.List;

public class piazzaAdapter extends BaseAdapter<ResPiazza, RecyclerView.ViewHolder> {
    private static final String TAG = "piazzaAdapter";
    private final static int BANNER_TYPE = 1;
    private final static int IMAGE_TYPE = 2;
    private List<ResPiazza.ResPiazzaDetail> mImageDataLists;
    private ArrayList<PiazzaXbannerData> mBannerDataLists;
    private PiazzaCallback mPiazzaCallback;
    private List<ResPiazza.ResPiazzaDetail> mOriginalBannerDataLists;

    public void setPiazzaCallback(PiazzaCallback mPiazzaCallback) {
        this.mPiazzaCallback = mPiazzaCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == BANNER_TYPE) {
            LayoutBannerBinding bannerBinding = LayoutBannerBinding.inflate(inflater, parent, false);
            return new BannerViewHolder(bannerBinding);
        } else {
            LayoutImageItemBinding imageItemBinding = LayoutImageItemBinding.inflate(inflater, parent, false);
            return new ImageViewHolder(imageItemBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BANNER_TYPE) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            LayoutBannerBinding binding = bannerViewHolder.bannerBinding;
            binding.xbanner.setIsClipChildrenMode(true);
            binding.xbanner.setBannerData(R.layout.layout_banner_item, mBannerDataLists);
            binding.xbanner.loadImage(new XBanner.XBannerAdapter() {   //这里的position是轮播图的position,和recyclerView的position不同
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    ImageView banner_image = view.findViewById(R.id.banner_image);
                    TextView banner_description = view.findViewById(R.id.banner_description);
                    TextView banner_label = view.findViewById(R.id.banner_label);
                    PiazzaXbannerData piazzaXbannerData = mBannerDataLists.get(position);
                    GlideUtils.LoadImage(piazzaXbannerData.getXBannerUrl(), banner_image);
                    banner_description.setText(piazzaXbannerData.getDescription());
                    banner_label.setText(piazzaXbannerData.getXBannerTitle());
                }
            });
            binding.xbanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    binding.bannerIndicator.setText(String.valueOf(position + 1));//设置下方的指示器的数据
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            ResPiazza.ResPiazzaDetail datas = mImageDataLists.get(position - 1);
            ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
            imageViewHolder.imageItemBinding.setData(datas);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mBannerDataLists != null && mBannerDataLists.size() > 0) {
            count++;
        }
        if (mImageDataLists != null) {
            count += mImageDataLists.size();
        }
        return count;
    }

    /**
     * 判断类型
     *
     * @param position position to query
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? BANNER_TYPE : IMAGE_TYPE;
    }

    @Override
    public void setDatas(List<ResPiazza> data) {
        if (data != null && data.size() >= 2) {
            ResPiazza banner_data = data.get(0);
            ResPiazza image_data = data.get(1);
            mOriginalBannerDataLists = banner_data.getLists();
            mBannerDataLists = convertXbannerData(mOriginalBannerDataLists);  //转成Xbanner需要的数据
            mImageDataLists = image_data.getLists();
            notifyDataSetChanged(); //一定要加这个
        }
    }

    /**
     * 转换为Xbanner能使用的数据
     *
     * @param piazzaDataLists
     * @return
     */
    private ArrayList<PiazzaXbannerData> convertXbannerData(List<ResPiazza.ResPiazzaDetail> piazzaDataLists) {
        if (piazzaDataLists != null && piazzaDataLists.size() > 0) {
            ArrayList<PiazzaXbannerData> list = new ArrayList<>();
            for (int i = 0; i < piazzaDataLists.size(); i++) {
                list.add(new PiazzaXbannerData(piazzaDataLists.get(i).getImage(),
                        piazzaDataLists.get(i).getDescription(),
                        piazzaDataLists.get(i).getName()));
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * Xbanner所用的视图
     */
    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private LayoutBannerBinding bannerBinding;

        public BannerViewHolder(@NonNull LayoutBannerBinding bannerBinding) {
            super(bannerBinding.getRoot());
            this.bannerBinding = bannerBinding;
            bannerBinding.xbanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
                @Override
                public void onItemClick(XBanner banner, Object model, View view, int position) {
                    //把数据提取出来转成Category传出去,因为和发现页分类的字段一样,没必要传两个类过去,这样还要分别处理
                    Category category = new Category();
                    ResPiazza.ResPiazzaDetail resPiazzaDetail = mOriginalBannerDataLists.get(position);
                    category.setId(resPiazzaDetail.getId());
                    category.setDescription(resPiazzaDetail.getDescription());
                    category.setImage(resPiazzaDetail.getImage());
                    category.setIcon(resPiazzaDetail.getIcon());
                    category.setUrl(resPiazzaDetail.getUrl());
                    category.setFullurl(resPiazzaDetail.getFullurl());
                    category.setName(resPiazzaDetail.getName());
                    mPiazzaCallback.onItemBannerClick(category);
                }
            });
        }
    }

    /**
     * 下方图片浏览的ViewHolder
     */
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private LayoutImageItemBinding imageItemBinding;

        public ImageViewHolder(@NonNull LayoutImageItemBinding imageItemBinding) {
            super(imageItemBinding.getRoot());
            this.imageItemBinding = imageItemBinding;
            /**
             * 点击下方图片后把跳转到的浏览页面需要的数据返回过去
             */
            imageItemBinding.getRoot().setOnClickListener(v -> {
                int position = getLayoutPosition();
                ResPiazza.ResPiazzaDetail resPiazzaDetail = mImageDataLists.get(position - 1);
                mPiazzaCallback.onItemImagesClick(resPiazzaDetail);
            });
        }
    }


    public interface PiazzaCallback {
        void onItemBannerClick(Category category);

        void onItemImagesClick(ResPiazza.ResPiazzaDetail resPiazzaDetail);
    }
}
