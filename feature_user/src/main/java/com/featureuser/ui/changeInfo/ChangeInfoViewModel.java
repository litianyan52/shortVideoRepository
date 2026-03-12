package com.featureuser.ui.changeInfo;

import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.eventBus.MessageEvent;
import com.libase.manager.UserManager;
import com.network.bean.ResBase;

public class ChangeInfoViewModel extends BaseViewmodel {

    private final ChangeInfoModel mModel;

    public ChangeInfoViewModel() {
        mModel = new ChangeInfoModel();
    }

    private MutableLiveData<String> mAvatar = new MutableLiveData<>();
    private MutableLiveData<String> mBio = new MutableLiveData<>();
    private MutableLiveData<String> mNickName = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsChanged = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> mIsShowChangeAvatarDialog = new MutableLiveData<>(false);//是否显示改变头像的弹窗

    public MutableLiveData<String> getAvatar() {
        return mAvatar;
    }

    public MutableLiveData<String> getBio() {
        return mBio;
    }

    public MutableLiveData<String> getNickName() {
        return mNickName;
    }

    public MutableLiveData<Boolean> getIsChanged() {
        return mIsChanged;
    }

    public MutableLiveData<Boolean> getIsShowChangeAvatarDialog() {
        return mIsShowChangeAvatarDialog;
    }

    /**
     * 加载页面初始数据
     */
    public void setBaseInfo() {
        mAvatar.setValue(mModel.getUserAvatar());
        mNickName.setValue(mModel.getUserNickName());
        mBio.setValue(mModel.getUserBio());
    }

    /**
     * 修改用户信息
     */
    public void changeUserInfo() {
        if (!isChange()) {  //如果没有数据发生改变就不用发起网络请求
            return;
        }
        mModel.changeUserInfo(mAvatar.getValue(),
                UserManager.getInstance().getUserInfo().getUser().getUsername(),
                mNickName.getValue(), mBio.getValue(), new IRequestCallBack<ResBase<Object>>() {
                    @Override
                    public void RequestSuccess(ResBase<Object> result) {
                        showToastText(result.getMsg());
                        MessageEvent.LoginEvent.postSticky(true);   //告诉本页面和其他页面刷新数据
                        mIsChanged.setValue(true);
                    }

                    @Override
                    public void RequestFailed(int errorCodeValue, String errorMsg) {
                        showToastText(errorMsg);
                    }
                });
    }


    /**
     * 判断用户当前是否修改自己的信息
     *
     * @return
     */
    public boolean isChange() {
        if (!UserManager.getInstance().getUserInfo().getUser().getNickname().equals(mNickName.getValue())) {
            return true;
        }

        if (!UserManager.getInstance().getUserInfo().getUser().getAvatar().equals(mAvatar.getValue())) {
            return true;
        }
        if (!UserManager.getInstance().getUserInfo().getUser().getBio().equals(mBio.getValue())) {
            return true;
        }

        return false;
    }



    public void showChangeAvatarDialog(){
        mIsShowChangeAvatarDialog.setValue(true);//告诉Activity显示弹窗
    }

}
