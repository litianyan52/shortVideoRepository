package com.example.demo20shvideoproject;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.demo20shvideoproject.databinding.ActivityMainBinding;
import com.libase.base.BaseActivity;
import com.libase.config.ArouterPath;

public class MainActivity extends BaseActivity<MainViewmodel, ActivityMainBinding> {

    private static final String TAG = "MainActivity";

    @Override
    public MainViewmodel getViewModel() {

        return new ViewModelProvider(this).get(MainViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getViewModelId() {
        return BR.viewmodel;
    }
    @Override
    public void initView() {
        Log.d(TAG, "initView: ");
        Fragment homeFragment = (Fragment) ARouter.getInstance().build(ArouterPath.Home.FRAGMENT_HOME).navigation();
        Fragment userFragment = (Fragment) ARouter.getInstance().build(ArouterPath.User.FRAGMENT_USER).navigation();
        Fragment piazzaFragment = (Fragment) ARouter.getInstance().build(ArouterPath.Piazza.FRAGMENT_PIAZZA).navigation();
        Fragment findFragment = (Fragment) ARouter.getInstance().build(ArouterPath.Find.FRAGMENT_FIND).navigation();
        Log.d(TAG, "initView: " + homeFragment);

//        Object navigation = ARouter.getInstance().build("/user/login").navigation();
//        Log.d(TAG, "initView: " + navigation);
        mdataBinding.mainBottomNavigation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.home) {
                    replaceFragment(homeFragment);
                } else if (checkedId == R.id.find) {
                    replaceFragment(findFragment);
                } else if (checkedId == R.id.piazza) {
                    replaceFragment(piazzaFragment);
                } else if (checkedId == R.id.user) {
                    replaceFragment(userFragment);
                }
            }
        });

    }

    private void replaceFragment(Fragment fragment) {
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fcv,fragment).commit();
        } catch (Exception e) {
            Log.d(TAG, "replaceFragment: "+ e.getMessage());
        }
    }

    @Override
    public void initData() {
        mdataBinding.home.setChecked(true);   //主动设置一下让启动时处于home页面

    }


}