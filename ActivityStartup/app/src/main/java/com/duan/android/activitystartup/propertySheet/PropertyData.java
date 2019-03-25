package com.duan.android.activitystartup.propertySheet;

import java.util.List;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/21
 * desc :    物性表数据
 * version: 1.0
 * </pre>
 */
public class PropertyData {

    public String code;
    public String msg;
    public PropertyEntity data;

    static class PropertyEntity{
        public String totalCount;
        public String keyword;
        public List<String> keywords;            // 关键字集合
        public List<PropertyInfo> letterBreeds;  // 轮询获取所有的 key
    }

    // 首字母 及 对应 Breed 数据  Key - [breeds]
    static class PropertyInfo{
        public String key;                  // 轮询获取所有的 key
        public List<BreedProperty> breeds;
    }

    // Breed数据详情  Breed - [specs]
    static class BreedProperty{
        public String breed;
        public List<String> specs;
    }

    // Breed数据详情
    static class PropertyDetail{
        public String breed;
        public List<String> specs;
    }

    // 可选中的item
    public static class CheckableItem{
        public String name;
        public boolean isChecked = false;
    }

}
