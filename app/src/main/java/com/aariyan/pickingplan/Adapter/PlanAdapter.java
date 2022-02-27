package com.aariyan.pickingplan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.pickingplan.Model.PlanModel;
import com.aariyan.pickingplan.R;

import java.util.List;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {

    private Context context;
    private List<PlanModel> list;

    public PlanAdapter(Context context, List<PlanModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_plan_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlanModel model = list.get(position);
        holder.storeName.setText(model.getStorename());
        holder.salesOrderNo.setText(model.getSalesOrderNo());
        holder.lineNo.setText(""+model.getLineNos());
        holder.itemName.setText(model.getDescription());
        holder.qty.setText(model.getQuantity());
        holder.weight.setText(model.getWeights());
        holder.toLoad.setText("0");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView storeName, salesOrderNo, lineNo, itemName,qty,weight,toLoad;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storeName = itemView.findViewById(R.id.storeName);
            salesOrderNo = itemView.findViewById(R.id.salesOrderNo);
            lineNo = itemView.findViewById(R.id.lineNo);
            itemName = itemView.findViewById(R.id.itemName);
            qty = itemView.findViewById(R.id.qty);
            weight = itemView.findViewById(R.id.weight);
            toLoad = itemView.findViewById(R.id.toLoad);
        }
    }
}
