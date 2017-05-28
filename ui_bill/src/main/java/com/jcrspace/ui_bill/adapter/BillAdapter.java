package com.jcrspace.ui_bill.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrspace.common.utils.DateUtils;
import com.jcrspace.manager_bill.model.BillDO;
import com.jcrspace.ui_bill.R;
import com.jcrspace.ui_bill.model.BillVO;

import java.util.List;

/**
 * Created by Jcr on 2017/5/20 0020.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private Context context;
    public List<BillVO> billVOList;
    private OnItemLongClickListener onItemLongClickListener;

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
        BillVO billVO = billVOList.get(position);
        holder.tvTitle.setText(billVO.title);
        if (billVO.comment.equals("")){
            holder.tvComment.setVisibility(View.GONE);
        } else {
            holder.tvComment.setVisibility(View.VISIBLE);
            holder.tvComment.setText(billVO.comment);
        }
        holder.tvMoney.setText("￥"+billVO.money);

        holder.tvDate.setText(DateUtils.getDateTime(billVO.createTime));
        if (billVO.type== BillDO.TYPE.EXPENDITURE){
            holder.tvType.setText(R.string.expenditure);
            holder.tvType.setBackgroundResource(R.drawable.bg_expenditure_header);
            holder.tvMoney.setTextColor(context.getResources().getColor(R.color.safeRed));
        } else {
            holder.tvType.setText(R.string.income);
            holder.tvType.setBackgroundResource(R.drawable.bg_income_header);
            holder.tvMoney.setTextColor(context.getResources().getColor(R.color.safeGreen));
        }
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemCount() {
        return billVOList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvType;
        TextView tvMoney;
        TextView tvDate;
        TextView tvComment;
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (onItemLongClickListener!=null){
                        onItemLongClickListener.onLongClick(billVOList.get(getAdapterPosition()-1),getAdapterPosition()-1);
                    }
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvComment = (TextView) itemView.findViewById(R.id.tv_comment);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public interface OnItemLongClickListener{
        void onLongClick(BillVO billVO,int position);
    }

    public void setBillVOList(List<BillVO> billVOList) {
        this.billVOList = billVOList;
    }
}
