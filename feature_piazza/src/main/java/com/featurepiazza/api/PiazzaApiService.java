package com.featurepiazza.api;

import com.featurepiazza.bean.ResPiazza;
import com.network.bean.ResBase;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PiazzaApiService {

    @GET(com.featurepiazza.netAddress.NetAddress.PIAZZA_DATA_ADDRESS)  //指定路径与请求方式
    Call<ResBase<List<ResPiazza>>> getDatas ();   //指定具体的返回类型
}
