package com.libase.base;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.libase.manager.UserManager;

public class BaseViewmodel extends ViewModel {
    private static final String TAG = "BaseViewmodel";
    protected MutableLiveData<Integer> mErrorCode = new MutableLiveData<>();//错误码
    private MutableLiveData<String> mToastText = new MutableLiveData<>();//弹窗显示地文本
    private MutableLiveData<Boolean> misLoading = new MutableLiveData<>(false);//是否显示加载
    private MutableLiveData<Boolean> mIsFinish = new MutableLiveData<>(false); //是否需要关闭页面


    public MutableLiveData<Integer> getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(int mErrorCode) {
        this.mErrorCode.setValue(mErrorCode);
    }

    public MutableLiveData<String> getToastText() {
        return mToastText;
    }

    public MutableLiveData<Boolean> getIsFinish() {
        return mIsFinish;
    }




    /**
     * 设置加载是否可见
     *
     * @param status
     */
    public void showLoading(boolean status) {
        Log.d(TAG, "showToastText: " + status);
        misLoading.setValue(status);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return misLoading;
    }

    /**
     * 传入参数来实现Toast弹窗
     *
     * @param text
     */
    public void showToastText(String text) {

        if (text != null && !text.equals("")) {
            Log.d(TAG, "showToastText: " + text);
            mToastText.setValue(text);
            mToastText.setValue(null);  //防止二次弹窗
        }
    }

    /**
     * 需要关闭页面时触发设置mIsFinish为true让BaseActivity中的finish被触发
     */
    public void finishPage() {
        Log.d(TAG, "finishPage: ");
        mIsFinish.setValue(true);
    }

}
