package com.cs_indonesia.mandiri_update.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Device;
import com.cs_indonesia.mandiri_update.model.Halte;

import java.util.List;

/**
 * Created by luqman on 5/10/2016.
 */
public class AdapterHalte extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<Halte> listItems;
    private OnItemClickListener onItemClickListener;

    public AdapterHalte(Context context, List<Halte> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_halte, parent, false);
        return new VHItem(v);
    }

    private Halte getItem(int position) {
        return listItems.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Halte i = getItem(position);
        VHItem item = (VHItem) holder;
        item.TvHalteName.setText(i.getNm_halte());
        item.TvHalteGate.setText(i.getJumlah_gate() + " gate");
        item.TvHalteStatus.setText(i.getStatus() == 0 ? "available" : "updated");
        if (i.getStatus() == 0) {
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.blue));
            item.TvHalteStatus.setText("available" );
        }
        else if (i.getStatus() < 2) {
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            item.TvHalteStatus.setText("on update" );
        }
        else if (i.getStatus() == 2) {
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.green));
            item.TvHalteStatus.setText("updated" );
        }
        else if (i.getStatus() > 2) {
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.red));
            item.TvHalteStatus.setText("trouble" );
        }
        holder.itemView.setTag(i);
    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }


    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TvHalteName, TvHalteGate, TvHalteStatus;
        ConstraintLayout Layout;

        public VHItem(View itemView) {
            super(itemView);
            this.TvHalteName = (TextView) itemView.findViewById(R.id.TvHalteName);
            this.TvHalteGate = (TextView) itemView.findViewById(R.id.TvHalteGate);
            this.TvHalteStatus = (TextView) itemView.findViewById(R.id.TvHalteStatus);
            this.Layout = (ConstraintLayout) itemView.findViewById(R.id.Layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, (Halte) v.getTag());
        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, Halte item);

    }
}