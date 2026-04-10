package com.featuremediaplay.ui.commend.report;


import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.featuremediaplay.R;
import com.featuremediaplay.databinding.PopupCommandInputBinding;

public class PopupWindowOfCommandList extends PopupWindow {

    private final PopupCommandInputBinding mBinding;
    private OnSendListener mOnSendListener;

    public interface OnSendListener {
        void onSend(String content);
    }

    public PopupWindowOfCommandList(AppCompatActivity activity) {
        super(activity);
        mBinding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.popup_command_input, null, false);
        init(activity);
    }

    private void init(AppCompatActivity activity) {
        setContentView(mBinding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //这行代码的作用是让 PopupWindow 可以获取焦点。当 PopupWindow 可获取焦点时，它能够接收用户的输入事件，
       // setFocusable(true);
        //允许用户在 PopupWindow 外部触摸屏幕，触摸外部后直接关闭弹窗
      //  setOutsideTouchable(true);
       // setTouchable(true);
        //当软键盘弹出时，PopupWindow 的大小会进行调整，以确保 PopupWindow 内的内容能完整显示，避免被软键盘遮挡。
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //避免默认背景导致PopupWindow大小受限，影响占满屏幕效果
        setBackgroundDrawable(null);

        mBinding.etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String content = mBinding.etInput.getText().toString().trim();
                    if (!content.isEmpty() && mOnSendListener != null) {
                        mOnSendListener.onSend(content);
                        mBinding.etInput.getText().clear();
                    }
                //    dismiss();
                    return true;
                }
                return false;
            }
        });
    }

    public void setOnSendListener(OnSendListener listener) {
        mOnSendListener = listener;
    }

    public EditText getEditText() {
        return mBinding.etInput;
    }
}
