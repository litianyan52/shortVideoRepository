package com.featuremediaplay.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.featuremediaplay.databinding.LayoutDeleteCommentDialogBinding;

public class DeleteCommentDialog extends DialogFragment {
    private IDeleteCommentDialog mCallBack;

    public DeleteCommentDialog(IDeleteCommentDialog mCallBack) {
        this.mCallBack = mCallBack;
    }

    public static DeleteCommentDialog newInstance(IDeleteCommentDialog callback) {

        Bundle args = new Bundle();
        DeleteCommentDialog fragment = new DeleteCommentDialog(callback);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LayoutDeleteCommentDialogBinding binding = LayoutDeleteCommentDialogBinding.inflate(inflater);
        binding.setLifecycleOwner(getActivity());
        binding.deCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.confirmDelete();  //进行回调然后关闭
                dismiss();
            }
        });
        builder.setView(binding.getRoot());
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    public static void showDeleteDialog(FragmentActivity activity,IDeleteCommentDialog callback) {
        DeleteCommentDialog deleteCommentDialog = newInstance(callback);
        deleteCommentDialog.show(activity.getSupportFragmentManager(), "delete_dialog");
    }


    public interface IDeleteCommentDialog {
        void confirmDelete();
    }
}
