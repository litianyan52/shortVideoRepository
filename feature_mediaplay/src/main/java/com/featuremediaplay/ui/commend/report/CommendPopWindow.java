package com.featuremediaplay.ui.commend.report;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.featuremediaplay.R;
import com.featuremediaplay.databinding.LayoutCommentPopwindowBinding;
import com.featuremediaplay.ui.mediaplay.MediaPlayViewModel;

public class CommendPopWindow extends PopupWindow {
    private IEditTextCallback mCallback;
    private MediaPlayViewModel mViewModel;

    public CommendPopWindow(AppCompatActivity activity) {
        super(activity);
        init(activity);
    }

    public void setCallback(IEditTextCallback mCallback) {
        this.mCallback = mCallback;
    }

    private void init(AppCompatActivity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        //实例化绑定类
        LayoutCommentPopwindowBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.layout_comment_popwindow, null, false);
        mViewModel = new ViewModelProvider(activity).get(MediaPlayViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(activity);
        setContentView(binding.getRoot());
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //这行代码的作用是让 PopupWindow 可以获取焦点。当 PopupWindow 可获取焦点时，它能够接收用户的输入事件，
        setFocusable(true);
        //允许用户在 PopupWindow 外部触摸屏幕，触摸外部后直接关闭弹窗
        setOutsideTouchable(true);
        setTouchable(true);
        //当软键盘弹出时，PopupWindow 的大小会进行调整，以确保 PopupWindow 内的内容能完整显示，避免被软键盘遮挡。
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //避免默认背景导致PopupWindow大小受限，影响占满屏幕效果
        setBackgroundDrawable(null);
        binding.seCmd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String string = binding.seCmd.getText().toString().trim();
                    if (string != null && !string.isEmpty() && mCallback != null) {
                        mCallback.sendMsg(string);  //往外传递发送的评论
                        binding.seCmd.getText().clear();//清空输入框
                    }
                    return true;  //该事件已处理,无需系统处理

                }
                return false; //该事件未处理,系统默认处理
            }
        });


        mViewModel.getIsLike().observe(activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    binding.like.setImageResource(R.mipmap.icon_video_unlike);
                }
                if (integer == 1) {
                    binding.like.setImageResource(R.mipmap.icon_solid_like);
                }
            }
        });

        //更新收藏按钮图片状态
        mViewModel.getIsCollection().observe(activity, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    binding.collect.setImageResource(R.mipmap.icon_video_collect);
                }
                if (integer == 1) {
                    binding.collect.setImageResource(R.mipmap.icon_solid_collect);
                }
            }
        });
    }


    public void showPopupWindow(View view) {
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 输入框信息回调接口
     */
    public interface IEditTextCallback {
        void sendMsg(String msg);
    }

}
