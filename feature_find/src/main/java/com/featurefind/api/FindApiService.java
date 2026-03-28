package com.featurefind.api;

import com.featurefind.bean.BodyCategoryDataOne;
import com.featurefind.bean.ResCategoryDataOne;
import com.featurefind.bean.ResFind;
import com.featurefind.netAddress.NetAddress;
import com.network.bean.ResBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FindApiService {

    @GET(NetAddress.FIND_ADDRESS)  //指定路径与请求方式
    Call<ResBase<ResFind>> getFindData ();   //指定具体的返回类型

    @POST(NetAddress.FIND_CATEGORY_ONE_PART_DATA)  //指定路径与请求方式
    Call<ResBase<ResCategoryDataOne>> getCategoryDetailOne (@Body BodyCategoryDataOne body);   //指定具体的返回类型
}
