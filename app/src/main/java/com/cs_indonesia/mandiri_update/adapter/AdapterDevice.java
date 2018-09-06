package com.cs_indonesia.mandiri_update.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs_indonesia.mandiri_update.R;
import com.cs_indonesia.mandiri_update.model.Device;

import java.util.List;

/**
 * Created by luqman on 5/10/2016.
 */
public class AdapterDevice extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<Device> listItems;
    private OnItemClickListener onItemClickListener;

    public AdapterDevice(Context context, List<Device> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
        return new VHItem(v);
    }

    private Device getItem(int position) {
        return listItems.get(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Device i = getItem(position);
        VHItem item = (VHItem) holder;
        item.TvPlatNomor.setText(i.getPlat_nomor());
        item.TvDeviceMac.setText(i.getDevicemac());
        item.TvKoridor.setText(i.getNama_koridor());
        item.TvBerangkat.setText(i.getBerangkat());
        item.TvTujuan.setText(i.getTujuan());
        item.TvJumlahPenumpang.setText(i.getJumlah_penumpang() + "");
        if (i.getSehat()==0)
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.red));
        else
            item.Layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.itemView.setTag(i);
    }


    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size();
    }


    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TvPlatNomor, TvDeviceMac, TvKoridor, TvBerangkat, TvTujuan, TvJumlahPenumpang;
        ConstraintLayout Layout;

        public VHItem(View itemView) {
            super(itemView);
            this.TvPlatNomor = (TextView) itemView.findViewById(R.id.TvPlatNomor);
            this.TvDeviceMac = (TextView) itemView.findViewById(R.id.TvDeviceMac);
            this.TvKoridor = (TextView) itemView.findViewById(R.id.TvKoridor);
            this.TvBerangkat = (TextView) itemView.findViewById(R.id.TvBerangkat);
            this.TvTujuan = (TextView) itemView.findViewById(R.id.TvTujuan);
            this.TvJumlahPenumpang = (TextView) itemView.findViewById(R.id.TvJumlahPenumpang);
            this.Layout = (ConstraintLayout) itemView.findViewById(R.id.Layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, (Device) v.getTag());
        }
    }


    public interface OnItemClickListener {

        void onItemClick(View view, Device item);

    }
}