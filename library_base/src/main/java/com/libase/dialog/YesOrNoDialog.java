package com.libase.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.libase.databinding.LayoutDialogYseOrNoBinding;

public class YesOrNoDialog extends DialogFragment {

    private final DialogCallBack mCallBack;
    public static void showDialog(FragmentActivity activity, String title, String content, DialogCallBack callBack)
    {
        YesOrNoDialog dialog = newInstance(title, content, callBack);
        dialog.show(activity.getSupportFragmentManager(),"yesOrNo"); //直接在这里完成显示操作,调用时只需传参
    }

    private static YesOrNoDialog newInstance(String title, String content, DialogCallBack callBack) {

        Bundle args = new Bundle();
        args.putString("KEY_TITLE", title);
        args.putString("KEY_CONTENT", content);

        YesOrNoDialog dialog = new YesOrNoDialog(callBack);
        dialog.setArguments(args);
        return dialog;
    }

    public YesOrNoDialog(DialogCallBack callBack) {
        mCallBack = callBack;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle arguments = getArguments();
        String title = arguments.getString("KEY_TITLE");
        String content = arguments.getString("KEY_CONTENT");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LayoutDialogYseOrNoBinding binding = LayoutDialogYseOrNoBinding.inflate(inflater);
        binding.setLifecycleOwner(getActivity());
        binding.dialogTitle.setText(title);  //设置弹窗标题
        binding.dialogContent.setText(content); //设置内容
        binding.dialogConfirm.setOnClickListener(v -> {
            mCallBack.confirm();  //回调通知Activity然后关闭
            dismiss();
        });
        binding.dialogCancel.setOnClickListener(v ->
        {
            dismiss();//直接关闭弹窗
        });
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();
                attributes.gravity = Gravity.CENTER;
                attributes.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);//获取到屏幕宽度再乘以0.9
                alertDialog.getWindow().setAttributes(attributes);
            }
        });
        return alertDialog;
    }

    public interface DialogCallBack
    {
        void confirm();
    }
}
