package com.featureuser.ui.agreementWeb;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.featureuser.BR;
import com.featureuser.R;
import com.featureuser.databinding.ActivityWebviewAgreementBinding;
import com.featureuser.userConfig.UserConfig;
import com.libase.base.BaseActivity;
import com.libase.base.BaseViewmodel;
import com.libase.config.ArouterPath;
import com.libase.utils.StatusBarUtils;

@Route(path = ArouterPath.User.ACTIVITY_WB_AGREEMENT)
public class AgreeMentActivity extends BaseActivity<BaseViewmodel, ActivityWebviewAgreementBinding> {
    @Autowired(name = UserConfig.AgreementType.KEY_AGREEMENT_TYPE)
    public int mAgreementType;
    private final String BASE_URL = "https://titok.fzqq.fun";
    private final String PRI_AGREEMENT_URL = BASE_URL + "/agreement.html";  //隐私协议及概要
    private final String USER_AGREEMENT_URL = BASE_URL + "/UserAgreement.html";//用户协议
    private final String USER_INFO_URL = BASE_URL + "/userinfomenus.html";//用户个人信息收集清单
    private String DESTINATION_URL;
    private WebView webView;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_agree_ment);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }

    @Override
    public BaseViewmodel getViewModel() {
        return new ViewModelProvider(this).get(BaseViewmodel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview_agreement;
    }

    @Override
    public int getViewModelId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        StatusBarUtils.AddStatusHeightToRootView(mdataBinding.getRoot()); //沉浸式布局设置
    }

    @Override
    public void initData() {
        switch (mAgreementType) {
            case UserConfig.AgreementType.USER_AGREEMENT:
                mdataBinding.agreementTitle.setText("用户协议");
                DESTINATION_URL = USER_AGREEMENT_URL;
                break;
            case UserConfig.AgreementType.PRIVATE_POLICY:
            case UserConfig.AgreementType.SUM_PRIVATE:
                mdataBinding.agreementTitle.setText("隐私政策");
                DESTINATION_URL = PRI_AGREEMENT_URL;
                break;
            case UserConfig.AgreementType.USER_INFO_COLLECT:
                mdataBinding.agreementTitle.setText("个人信息收集清单");
                DESTINATION_URL = USER_INFO_URL;
                break;
        }
        mViewModel.showLoading(true);
        webView = mdataBinding.webView;
        webView.loadUrl(DESTINATION_URL);
        mdataBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mViewModel.showLoading(false);  //加载完取消ProgressBar
            }
        });
    }

    @Override
    protected void onDestroy() {


        if (webView != null) {
          //  webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.removeAllViews();
            ViewGroup parent = (ViewGroup)webView.getParent();
            if (parent!=null) {
                parent.removeView(webView);
            }

            // 3. 停止加载
            webView.stopLoading();

            // 4. 暂停并清空
            webView.onPause();

            webView.clearHistory();
            webView.clearCache(true);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }

        super.onDestroy();
    }
}