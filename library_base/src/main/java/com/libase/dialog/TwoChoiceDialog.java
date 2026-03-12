package com.libase.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.libase.databinding.LayoutTwoChoiceDialogBinding;

public class TwoChoiceDialog extends DialogFragment {
    private TwoChoiceDialogCallBack mCallBack;

    public TwoChoiceDialog(TwoChoiceDialogCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    public static void showDialog(FragmentActivity  activity,  String title, String firstChoice, String secondChoice, TwoChoiceDialogCallBack callBack) {
        TwoChoiceDialog dialog = TwoChoiceDialog.newInstance(title, firstChoice, secondChoice, callBack);
        dialog.show(activity.getSupportFragmentManager(),"AvatarChangeWayChoice");
    }

    public static TwoChoiceDialog newInstance(String title, String firstChoice, String secondChoice, TwoChoiceDialogCallBack callBack) {

        Bundle args = new Bundle();
        args.putString("KEY_FIRST_CONTENT", firstChoice);
        args.putString("KEY_SECOND_CONTENT", secondChoice);
        args.putString("KEY_TITLE", title);
        TwoChoiceDialog dialog = new TwoChoiceDialog(callBack);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle arguments = getArguments();
        String firstContent = arguments.getString("KEY_FIRST_CONTENT");
        String secondContent = arguments.getString("KEY_SECOND_CONTENT");
        String title = arguments.getString("KEY_TITLE");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LayoutTwoChoiceDialogBinding binding = LayoutTwoChoiceDialogBinding.inflate(inflater, null, false);
        binding.title.setText(title);
        binding.firstChoice.setText(firstContent);
        binding.secondChoice.setText(secondContent);
        binding.setLifecycleOwner(getActivity());
        binding.firstChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.chooseFirst();
            }
        });

        binding.secondChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.chooseSecond();
            }
        });

        builder.setView(binding.getRoot());

        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }


    public interface TwoChoiceDialogCallBack {
        void chooseFirst(); //选择第一个选项

        void chooseSecond(); //选择第二个选项
    }
}
