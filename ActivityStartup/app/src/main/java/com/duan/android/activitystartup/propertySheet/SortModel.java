package com.duan.android.activitystartup.propertySheet;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :
 * version: 1.0
 * </pre>
 */
public class SortModel {

    private String name;     // 显示的数据
    private String pinyin;   // 数据对应的拼音
    private char sortChar;   // 显示数据拼音的首字母

    public SortModel(){}
    public SortModel(String name) {
        this.name = name;
        // this.pinyin = PinyinUtils.getPingYin(name);
        this.sortChar = pinyin.charAt(0);

        if (sortChar<'A'||sortChar>'Z') {
            sortChar = '#';
        }
    }

}
