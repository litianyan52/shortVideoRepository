package com.network;

import android.util.Log;

import com.network.bean.ResBase;
import com.network.bean.ResList;
import com.network.config.NetworkStatusConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCall {
    public interface ApiCallback<T> {
        void GetLiseSuccess(T result);

        void GetLiseFailed(int errorCode, String message);
    }

    private static final String TAG = "ApiCall";

    public interface ApiListCallback<T> {
        void GetLiseSuccess(ResList<T> result);

        void GetLiseFailed(int errorCode, String message);
    }


    /**
     * 发起分页型请求
     *
     * @param call
     * @param apiListCallback
     * @param <T>
     */
    public static <T> void enqueueList(Call<ResBase<ResList<T>>> call, ApiListCallback apiListCallback) { //接口泛型放入作用是在传入的时候决定类型,在这里声明泛型就无法进行回调操作

        call.enqueue(new Callback<ResBase<ResList<T>>>() {
            @Override
            public void onResponse(Call<ResBase<ResList<T>>> call, Response<ResBase<ResList<T>>> response) {
                if (response.isSuccessful()) //body网络正常一定有值
                {
                    if (response.body().getCode() == 1 && response.body() != null) //判断服务器是否正常
                    {
                        if (response.body().getData().getList().size() > 0) {
                            apiListCallback.GetLiseSuccess(response.body().getData());
                        } else {
                            apiListCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_EMPTY, response.body().getMsg());
                        }
                    } else {
                        apiListCallback.GetLiseFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, response.body().getMsg());
                    }
                } else {
                    apiListCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResBase<ResList<T>>> call, Throwable throwable) {
                apiListCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, "网络有问题");
            }
        });
    }


    /**
     * 发起非分页型请求的方法,错误信息能采用getMsg就采用这个,错误信息更准确
     *
     * @param call
     * @param apiCallback
     * @param <T>
     */

    public static <T> void enqueue(Call<ResBase<T>> call, ApiCallback<ResBase<T>> apiCallback) { //接口泛型放入作用是在传入的时候决定类型,在这里声明泛型就无法进行回调操作

        call.enqueue(new Callback<ResBase<T>>() {
            @Override
            public void onResponse(Call<ResBase<T>> call, Response<ResBase<T>> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {

                    if (response.body().getCode() == 1 && response.body() != null) //判断服务器是否正常
                    {
                        if (response.body().getData() != null) {
                            apiCallback.GetLiseSuccess(response.body());  //这里不要返回getData,因为回调设置的是ResBase的初始状态
                        } else {
                            apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_EMPTY, response.body().getMsg());
                        }
                    } else {
                        apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, response.body().getMsg());
                    }
                } else {
                    apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResBase<T>> call, Throwable throwable) {

                apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, "网络有问题");
            }
        });
    }


    /**
     * 发起非分页型请求的方法,错误信息能采用getMsg就采用这个,错误信息更准确
     *
     * @param call
     * @param apiCallback
     * @param <T>         允许data为null的请求
     */

    public static <T> void enqueueAllowDataNull(Call<ResBase<T>> call, ApiCallback<ResBase<T>> apiCallback) { //接口泛型放入作用是在传入的时候决定类型,在这里声明泛型就无法进行回调操作

        call.enqueue(new Callback<ResBase<T>>() {
            @Override
            public void onResponse(Call<ResBase<T>> call, Response<ResBase<T>> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {

                    if (response.body().getCode() == 1 && response.body() != null) //判断服务器是否正常
                    {
                        if (response.body().getMsg() != null) {
                            apiCallback.GetLiseSuccess(response.body());
                        } else {
                            apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_EMPTY, response.body().getMsg());
                        }
                    } else {
                        apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_SERVER_EXCEPTION, response.body().getMsg());
                    }
                } else {
                    apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ResBase<T>> call, Throwable throwable) {

                apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, "网络有问题");
            }
        });
    }


    /**
     * 发起非常规请求
     *
     * @param call
     * @param apiCallback
     * @param <T>
     */
    public static <T> void enqueueCommon(Call<T> call, ApiCallback<T> apiCallback) { //接口泛型放入作用是在传入的时候决定类型,在这里声明泛型就无法进行回调操作

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {

                    apiCallback.GetLiseSuccess(response.body());
                } else {
                    apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, "网络异常");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable throwable) {

                apiCallback.GetLiseFailed(NetworkStatusConfig.ERROR_STATUS_NETWORK_FAIL, "网络有问题");
            }
        });
    }
}
