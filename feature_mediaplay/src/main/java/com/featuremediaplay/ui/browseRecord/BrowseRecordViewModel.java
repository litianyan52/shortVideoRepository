package com.featuremediaplay.ui.browseRecord;

import androidx.lifecycle.MutableLiveData;

import com.libase.base.BaseViewmodel;
import com.libase.base.IRequestCallBack;
import com.libase.db.UserLookRecord;

import java.util.List;

public class BrowseRecordViewModel extends BaseViewmodel {

    private final BrowseRecordModel mModel;
    private MutableLiveData<List<UserLookRecord>> mRecords = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsShowCheckBox = new MutableLiveData<>(false);

    public BrowseRecordViewModel() {
        mModel = new BrowseRecordModel();
    }

    public MutableLiveData<List<UserLookRecord>> getRecords() {
        return mRecords;
    }

    public MutableLiveData<Boolean> getIsShowCheckBox() {
        return mIsShowCheckBox;
    }

    public void InsertBrowseRecord(int video_id, String cover, String label, String title, String duration) {
        mModel.InsertData(video_id, cover, label, title, duration);
    }

    /**
     * 取出所有浏览记录
     */
    public void QueryAllRecords() {
        mModel.QueryBrowseAllRecords(new IRequestCallBack<List<UserLookRecord>>() {
            @Override
            public void RequestSuccess(List<UserLookRecord> result) {
                mRecords.setValue(result);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {
//                showToastText(errorMsg);
                mRecords.setValue(null);
            }
        });
    }


    public void DeleteRecord(List<Integer> deleteList)
    {
        mModel.DeleteRecords(deleteList, new IRequestCallBack<String>() {
            @Override
            public void RequestSuccess(String result) {
                QueryAllRecords();
               // mIsShowCheckBox.setValue(false);
            }

            @Override
            public void RequestFailed(int errorCodeValue, String errorMsg) {

            }
        });
    }
}
