package com.duan.android.activitystartup.propertySheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.duan.android.activitystartup.R;
import com.duan.android.activitystartup.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * author : Duan
 * time : 2019/01/22
 * desc :    可选择item
 * version: 1.0
 * </pre>
 */
public class CheckableAdapter extends RecyclerView.Adapter{

    private String TAG = "CheckableAdapter";
    private Context mContext;
    private List<String> mData;
    private PropertyEventListener.OnItemCheckListener mListener;
    private String selectedItem = "";

    public CheckableAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout.checkable_item, parent, false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.checkable_spec_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder){
            ViewHolder viewHolder = (ViewHolder) holder;
            final String string = mData.get(position);
            viewHolder.tv.setText("· " + string);
            viewHolder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        mListener.onItemChecked(string);
                    }else {
                        LogUtils.printCloseableInfo(TAG, "mListener == null");
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
        @BindView(R.id.tv) TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void addItemCheckListener(PropertyEventListener.OnItemCheckListener listener){
        this.mListener = listener;
    }

    public void setDataList(String selectedItem, List<String> dataList){
        this.selectedItem = selectedItem;
        if (mData != null){
            mData.clear();
            mData.addAll(dataList);
        }else {
            mData = dataList;
        }
        notifyDataSetChanged();
    }

    public void setDataList( List<String> dataList){
        this.selectedItem = null;

        if (mData != null){
            mData.clear();
            mData.addAll(dataList);
        }else {
            mData = dataList;
        }
        notifyDataSetChanged();
    }

    public void setSelectedItem(String item){
        selectedItem = item;
    }

    public void clear(){
        if (mData != null){
            mData.clear();
        }
        notifyDataSetChanged();
    }


}
