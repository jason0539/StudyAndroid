package com.jason.workdemo.demo.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.jason.common.utils.MLog;
import com.jason.common.utils.MToast;
import com.jason.workdemo.R;

import java.io.ByteArrayOutputStream;

/**
 * liuzhenhui 16/6/29.下午5:07
 */
public class WebviewActivity extends Activity {
    private static final int REQ_CHOOSE = 112;

    WebView mWebview;
    Button mBtnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebview = (WebView) findViewById(R.id.wv_webview);
        mBtnConfirm = (Button)findViewById(R.id.btn_webview_confirm);


        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebview.setWebViewClient(new MyWebviewClient());
        mWebview.loadUrl("http://192.168.2.90:8080/#/");

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebview != null) {
                    mWebview.loadUrl("javascript:submitFeedback()");
                }
            }
        });
    }

    class MyWebviewClient extends WebViewClient {
        @Override
        public synchronized boolean shouldOverrideUrlLoading(WebView view, String url) {
            MLog.d(MLog.TAG_WEBVIEW,"MyWebviewClient->"+"shouldOverrideUrlLoading ");
            if (url.startsWith("finger://faq.upload.picture")) {
                launchGallery();
                return true;
            }else if (url.startsWith("finger://faq.upload.test")) {
                MLog.d(MLog.TAG_WEBVIEW,url);
                MToast.show("收到JS调用");
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * 启动相册
     */
    private void launchGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent =
                    new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQ_CHOOSE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQ_CHOOSE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            if (REQ_CHOOSE == requestCode) {
                mWebview.loadUrl("javascript:appUploadCancel()");
            }
        } else if (resultCode == Activity.RESULT_OK) {
            try {
                if (REQ_CHOOSE == requestCode) {
                    String path = ImageDecoder.getImagePath(this, data.getData());
                    if (TextUtils.isEmpty(path)) {
                        MToast.show("您选择的不是有效的图片");
                    } else if (path.toLowerCase().endsWith("jpg") || path.toLowerCase().endsWith("jpeg")
                            || path.toLowerCase().endsWith("png") || path.toLowerCase().endsWith("gif")) {
                        String fileBase64 = "";
                        Bitmap bitmap = ImageDecoder.decodeImage(path);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (path.toLowerCase().endsWith("jpg") || path.toLowerCase().endsWith("jpeg")) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, ImageDecoder.IMG_QUALITY, baos);
                            fileBase64 = "data:image/jpeg;base64,";
                        } else if (path.toLowerCase().endsWith("png")) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, ImageDecoder.IMG_QUALITY, baos);
                            fileBase64 = "data:image/png;base64,";
                        } else {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, ImageDecoder.IMG_QUALITY, baos);
                            fileBase64 = "data:image/jpeg;base64,";
                        }
                        byte[] bytes = baos.toByteArray();
                        baos.flush();
                        baos.close();
                        fileBase64 = fileBase64 + Base64.encodeToString(bytes, Base64.NO_WRAP);

//                        int picWidth = bitmap.getWidth();
//                        int picHeight = bitmap.getHeight();
//                        mWebview.loadUrl("javascript:appUploadImg('" + fileBase64 + "'," + picWidth + "," + picHeight + ")");

                        mWebview.loadUrl("javascript:appUploadImg('" + fileBase64 + "')");
                    } else {
                        MToast.show("您选择的不是有效的图片");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()) {
            mWebview.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
