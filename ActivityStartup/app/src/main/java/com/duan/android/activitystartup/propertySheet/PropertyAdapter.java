package com.duan.android.activitystartup.propertySheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

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
 * desc :   物性表适配器
 * version: 1.0
 * </pre>
 */
public class PropertyAdapter extends RecyclerView.Adapter {
    private String TAG = "PropertyAdapter";
    private Context mContext;
    //private List<PropertyData.PropertyInfo> mData;
    private List<PropertyData.BreedProperty> mData;
    private PropertyEventListener.OnItemClickListener mListener;
    private CheckableAdapter mAdapter;


    public PropertyAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.property_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  ViewHolder){
            final ViewHolder viewHolder = (ViewHolder) holder;
            final PropertyData.BreedProperty model = mData.get(position);
            viewHolder.tv.setText(model.breed);
            initRecyclerView(viewHolder.recyclerView,  model.specs);
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, List<String> list) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getAdapter(list));
    }
    public RecyclerView.Adapter getAdapter(List<String> list) {
        mAdapter = new CheckableAdapter(mContext);
        mAdapter.setDataList(null, list);
        mAdapter.addItemCheckListener(new PropertyEventListener.OnItemCheckListener() {
            @Override
            public void onItemChecked(String string) {
                mListener.onItemClicked(string);
            }
        });
        return mAdapter;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv) TextView tv;
        @BindView(R.id.recycler_view) RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void addItemClickListener(PropertyEventListener.OnItemClickListener listener){
        this.mListener = listener;
        notifyDataSetChanged();
    }

    public void setDataList( List<PropertyData.PropertyInfo> dataList){
        initData(dataList);
        notifyDataSetChanged();
    }

    public void clear(){
        if (mData != null){
            mData.clear();
        }
        notifyDataSetChanged();
    }

    private List<PropertyData.BreedProperty> initData(List<PropertyData.PropertyInfo> dataList){

        if (mData != null){
            mData.clear();
        }else {
            mData = new ArrayList<>();
        }
        if (dataList != null && dataList.size()>0){
            // PropertyData.PropertyInfo 中拿到 List<PropertyData.BreedProperty>
            for (int i = 0; i < dataList.size(); i++){
                mData.addAll(dataList.get(i).breeds);
            }
        }else {
            if (mData != null){
                mData.clear();
            }
        }
        return mData;
    }

    /** 遍历mData 查询指定 item 位置 */
    public int getAlphabetPosition(String alphabet){
        LogUtils.printCloseableInfo(TAG, "alphabet: "+ alphabet);
        if (mData != null && mData.size() > 0){
            for (int i = 0; i < mData.size(); i++){
                PropertyData.BreedProperty model = mData.get(i);
                LogUtils.printCloseableInfo(TAG, "alphabet: "+ alphabet+ "  model.breed: "+ model.breed + " POSITION: " + i );
                String firstAlphabet = model.breed.substring(0,1);
                LogUtils.printCloseableInfo(TAG, "firstAlphabet: "+ firstAlphabet);
                if (TextUtils.equals(firstAlphabet, alphabet)){
                    LogUtils.printCloseableInfo(TAG, "model.breed: "+ model.breed + " POSITION: " + i );
                    return i;
                }
            }
        }
        return 0;
    }
}
