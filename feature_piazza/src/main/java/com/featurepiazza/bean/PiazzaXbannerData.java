package com.featurepiazza.bean;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

public class PiazzaXbannerData implements BaseBannerInfo {
    private String ImageUrl;
    private String Title;
    private String Description;

    public PiazzaXbannerData(String imageUrl, String description, String title) {
        ImageUrl = imageUrl;
        Description = description;
        Title = title;
    }

    @Override
    public String getXBannerUrl() {
        return ImageUrl;
    }

    @Override
    public String getXBannerTitle() {
        return Title;
    }
    public String getDescription()
    {
        return Description;
    }
}
