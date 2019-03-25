package com.duan.android.activitystartup.propertySheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/22
 * desc :   字母表 - 侧边指示条, 点击单选。
 * version: 1.0
 * </pre>
 */
public class AlphabetAdapter extends RecyclerView.Adapter {

    private String TAG = "AlphabetAdapter";
    private Context mContext;
    private List<PropertyData.CheckableItem> mData;
    private PropertyEventListener.OnItemCheckListener mListener;
    private PropertyData.CheckableItem mSelectedItem ;      // 选中的item

    public AlphabetAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.alphabet_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final PropertyData.CheckableItem model = mData.get(position);

            viewHolder.checkBox.setText(model.name);

            //LogUtils.printCloseableInfo(TAG, "当前字母： " + model.name + "  是否选中："+ model.isChecked);

            if (TextUtils.equals(model.name, mSelectedItem.name)){
                viewHolder.checkBox.setChecked(true);
            }else {
                viewHolder.checkBox.setChecked(false);
            }

            // item click listener
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!model.isChecked){
                        model.isChecked = true;
                        setSelectedItem(model);
                    }

                    if (mListener != null){
                        mListener.onItemChecked(model.name);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void addItemCheckListener(PropertyEventListener.OnItemCheckListener listener){
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setDataList(List<PropertyData.PropertyInfo> dataList){
        initData(dataList);
        notifyDataSetChanged();
    }

    public void setSelectedItem(PropertyData.CheckableItem item){
        if (mSelectedItem != null){
            if (!TextUtils.equals(item.name, mSelectedItem.name)){
                mSelectedItem = item;
                notifyDataSetChanged();
            }
        }


    }

    public void clear(){
        if (mData != null){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    private List<PropertyData.CheckableItem> initData(List<PropertyData.PropertyInfo> dataList){

        if (mData != null){
            mData.clear();
        }else {
            mData = new ArrayList<>();
        }
        if (dataList != null && dataList.size()>0){
            // PropertyData.PropertyInfo 中拿到 所有首字母
            PropertyData.CheckableItem tempItem;
            for (int i = 0; i < dataList.size(); i++){
                tempItem = new PropertyData.CheckableItem();
                tempItem.name = dataList.get(i).key;
                if (i == 0){
                    mSelectedItem = tempItem;
                    tempItem.isChecked = true;
                }
                LogUtils.printCloseableInfo(TAG, "ALPHABET: "+ tempItem.name);
                mData.add(tempItem);
            }
        }else {
            if (mData != null){
                mData.clear();
            }
        }
        return mData;
    }

}
