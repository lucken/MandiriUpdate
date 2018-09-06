package com.cs_indonesia.mandiri_update.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Halte;
import com.cs_indonesia.mandiri_update.model.HalteGate;

import java.util.List;

/**
 * Created by luqman on 5/10/2016.
 */
public class AdapterHalteGate extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<HalteGate> listItems;
    private OnStartListener onStartListener;
    private OnStopListener onStopListener;

    public AdapterHalteGate(Context context, List<HalteGate> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    public void setOnStartListener(OnStartListener onStartListener) {
        this.onStartListener = onStartListener;
    }

    public void setOnStopListener(OnStopListener onStopListener) {
        this.onStopListener = onStopListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_halte_gate, parent, false);
        return new VHItem(v);
    }

    private HalteGate getItem(int position) {
        return listItems.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HalteGate i = getItem(position);
        VHItem item = (VHItem) holder;
        item.TvGateName.setText(i.getNm_gate());
        item.TvGateMerek.setText(i.getMerek());
        item.TvGateType.setText(i.getGate_type());
        item.TvGateInfo.setText(i.getGate_info());
        item.TvGateStatus.setText(i.getSt_update() == 0 ? "available" : "updated");
        if (i.getSt_update() == 0)
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.green_light));
        else
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.red_light));
        switch (i.getSt_update()){
            case 0:
                item.Layout.setBackgroundColor(context.getResources().getColor(R.color.blue));
                item.TvGateStatus.setText("available" );
                item.BtMulai.setEnabled(true);
                item.BtSelesai.setEnabled(false);
                break;
            case 1:
                item.Layout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
                item.TvGateStatus.setText("on update" );
                item.BtMulai.setEnabled(false);
                item.BtSelesai.setEnabled(true);
                break;
            case 2:
                item.Layout.setBackgroundColor(context.getResources().getColor(R.color.green));
                item.TvGateStatus.setText("updated" );
                item.BtMulai.setEnabled(false);
                item.BtSelesai.setEnabled(false);
                break;
            case 3:
                item.Layout.setBackgroundColor(context.getResources().getColor(R.color.red));
                item.TvGateStatus.setText("trouble" );
                item.BtMulai.setEnabled(false);
                item.BtSelesai.setEnabled(false);
                break;
        }

        item.BtMulai.setTag(i);
        item.BtSelesai.setTag(i);
    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }


    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TvGateName, TvGateMerek, TvGateType, TvGateInfo, TvGateStatus, TvGateAddress;
        ConstraintLayout Layout;
        Button BtMulai, BtSelesai;

        public VHItem(View itemView) {
            super(itemView);
            this.TvGateName = (TextView) itemView.findViewById(R.id.TvGateName);
            this.TvGateMerek = (TextView) itemView.findViewById(R.id.TvGateMerek);
            this.TvGateType = (TextView) itemView.findViewById(R.id.TvGateType);
            this.TvGateInfo = (TextView) itemView.findViewById(R.id.TvGateInfo);
            this.TvGateStatus = (TextView) itemView.findViewById(R.id.TvGateStatus);
            this.TvGateAddress = (TextView) itemView.findViewById(R.id.TvGateAddress);
            this.BtMulai = (Button) itemView.findViewById(R.id.BtMulai);
            this.BtSelesai = (Button) itemView.findViewById(R.id.BtSelesai);
            this.Layout = (ConstraintLayout) itemView.findViewById(R.id.Layout);
            BtMulai.setOnClickListener(this);
            BtSelesai.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.BtMulai:
                    onStartListener.onStart(v, (HalteGate) v.getTag());
                    break;
                case R.id.BtSelesai:
                    onStopListener.onStop(v, (HalteGate) v.getTag());
                    break;
            }
        }
    }


    public interface OnStartListener {
        void onStart(View view, HalteGate item);
    }

    public interface OnStopListener {
        void onStop(View view, HalteGate item);
    }
}