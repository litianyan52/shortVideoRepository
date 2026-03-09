package com.libase.base.list;
import com.network.bean.ResList;

public interface IListListener<T> {
    void GetSuccess(ResList<T> data ,boolean isFirst);
    void GetFailed(int status_code);
}
