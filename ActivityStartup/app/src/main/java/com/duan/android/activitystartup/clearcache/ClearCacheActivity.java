package com.duan.android.activitystartup.clearcache;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ClearCacheActivity extends BaseActivity {

    @BindView(R.id.iv_back) ImageView backBt;
    @BindView(R.id.tv_title) TextView title;

    @BindView(R.id.linear_clear) LinearLayout clearLinear; // 清理缓存
    @BindView(R.id.tv_clear) TextView clearTv;             // 缓存数据


    @Override
    public int getContentView() {
        return R.layout.activity_clear_cache;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.linear_clean)
    public void onClearClicked(){
        showDialog();
    }

    private void showDialog(){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.ic_launcher_foreground);
        normalDialog.setTitle("NormalDialog 清除缓存");
        normalDialog.setMessage("清除缓存会导致下载的内容被删除，是否确定?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        showPromptMessage("点击了确定");
                    }
                });
        normalDialog.setNegativeButton("点击了确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        showPromptMessage("点击了关闭");
                    }
                });
        // 显示
        normalDialog.show();
    }

    public void showPromptMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}
