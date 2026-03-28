package com.libase.base;

public interface IRequestCallBack<T> {
    public void RequestSuccess(T result);
    public  void RequestFailed(int errorCodeValue,String errorMsg);
}
