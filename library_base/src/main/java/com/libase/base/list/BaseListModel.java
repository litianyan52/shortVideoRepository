package com.libase.base.list;

public abstract class BaseListModel {
    protected int mPage = 1;  //请求的数据页数
    protected final int mLimit = 10; //每次请求的数据条数
    protected IListListener iListListener;
    public abstract void requestDatas(boolean isFirst);

    public void setiListListener(IListListener iListListener) {
        this.iListListener = iListListener;
    }
}
