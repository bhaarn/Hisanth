package com.padhuga.hishanth.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.padhuga.hishanth.R;

public class WebViewFragment extends DialogFragment {

    private FrameLayout parentFrameLayout;

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_webview, container, false);

        WebView districtMapWebView = rootView.findViewById(R.id.district_map_webview);
        parentFrameLayout = rootView.findViewById(R.id.parent_frame_layout);
        parentFrameLayout.setVisibility(View.GONE);
        ButtonClickJavascriptInterface myJavaScriptInterface = new ButtonClickJavascriptInterface(getActivity());
        districtMapWebView.addJavascriptInterface(myJavaScriptInterface, "MyFunction");
        districtMapWebView.getSettings().setJavaScriptEnabled(true);
        districtMapWebView.loadUrl("file:///android_asset/tn_map.html");
        districtMapWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                parentFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    public class ButtonClickJavascriptInterface {
        Context mContext;

        ButtonClickJavascriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void onButtonClick(String district) {
            dismiss();
            RegistrationFragment.dismissDialog(district);
        }
    }
}
