package com.duan.android.activitystartup.propertySheet;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/22
 * desc :   物性表 事件监听
 * version: 1.0
 * </pre>
 */
public interface PropertyEventListener {

    // 选中侧边栏的某个字母
    interface OnItemCheckListener{
        void onItemChecked(String string);
    }

    // 点击物性表概观某个 spec
    interface OnItemClickListener{
        void onItemClicked(String string);
    }

}
