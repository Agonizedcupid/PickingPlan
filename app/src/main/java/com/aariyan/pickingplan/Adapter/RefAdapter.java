package com.aariyan.pickingplan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aariyan.pickingplan.BarcodeActivity;
import com.aariyan.pickingplan.Model.RefModel;
import com.aariyan.pickingplan.PlanActivity;
import com.aariyan.pickingplan.R;

import java.util.List;

public class RefAdapter extends RecyclerView.Adapter<RefAdapter.ViewHolder> {

    private Context context;
    private List<RefModel> list;

    public RefAdapter(Context context, List<RefModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_ref_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RefModel model = list.get(position);
        holder.referenceCode.setText("Reference Code:      "+model.getStrUnickReference());
        holder.name.setText("Name:      "+model.getStrPickingNickname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlanActivity.class);
                intent.putExtra("qrCode", model.getStrUnickReference());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView referenceCode, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            referenceCode = itemView.findViewById(R.id.reference);
            name = itemView.findViewById(R.id.name);
        }
    }
}
