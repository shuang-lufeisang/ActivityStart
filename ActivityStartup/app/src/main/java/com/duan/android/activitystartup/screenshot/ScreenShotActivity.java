package com.duan.android.activitystartup.screenshot;

import android.Manifest;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.screenshot.base.BaseActivity;
import com.duan.android.activitystartup.util.PermissionMediator;

public class ScreenShotActivity extends BaseActivity implements View.OnClickListener {

    private Button mScreenshotBtn;

    // 动态权限
    private static final String[] PERMISSIONS_REQUEST = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private PermissionMediator.OnPermissionRequestListener mPermissionListener = new PermissionMediator.DefaultPermissionRequest() {
        @Override
        public void onPermissionRequest(boolean granted, String permission) {
            onPermissionRequest();
        }

        @Override
        public void onPermissionRequest(boolean isAllAuthorized, @NonNull String[] permissions, int[] grantResults) {
            onPermissionRequest();
        }

        private void onPermissionRequest() {
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_screen_shot;
    }

    @Override
    protected void initPresenter() {
        mScreenshotBtn =  findViewById(R.id.btn_screenshot);
        mScreenshotBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //权限申请
        PermissionMediator.checkPermission(this, PERMISSIONS_REQUEST, mPermissionListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_screenshot:
                //展示分享对话框
                SocialShareDialog dialog = new SocialShareDialog(this, this);
                dialog.show();
                break;
            default:
                break;
        }
    }


}
