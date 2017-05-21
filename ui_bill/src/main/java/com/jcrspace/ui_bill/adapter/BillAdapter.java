package com.jcrspace.ui_bill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcrspace.ui_bill.R;
import com.jcrspace.ui_bill.model.BillVO;

import java.util.List;

/**
 * Created by Jcr on 2017/5/20 0020.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private Context context;
    public List<BillVO> billVOList;

    public BillAdapter(Context context,List<BillVO> billVOList) {
        this.context = context;
        this.billVOList = billVOList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bill,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        renderItem(holder,position);
    }

    private void renderItem(ViewHolder holder, int position){

    }



    @Override
    public int getItemCount() {
        return billVOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
