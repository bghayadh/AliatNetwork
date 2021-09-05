package com.example.aliatnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends ArrayAdapter<ItemAuto> {

    private  List<ItemAuto> itemFull ;
    public ItemAdapter(@NonNull Context context, List<ItemAuto> itemList) {
        super(context, 0, itemList);

        itemFull = itemList;


    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_auto_complete,parent,false
            );
        }

        TextView item_name= convertView.findViewById(R.id.item_name);
        TextView item_code= convertView.findViewById(R.id.item_code);
        TextView item_model= convertView.findViewById(R.id.item_model);
        TextView item_part_number= convertView.findViewById(R.id.item_part_number);
        TextView serial_Number= convertView.findViewById(R.id.serial_Number);
        TextView barcode_number= convertView.findViewById(R.id.barcode_number);
        TextView quantityyy = convertView.findViewById(R.id.quatityyyy);

        ItemAuto itemIn = getItem(position);
        if (itemIn != null) {

            item_name.setText(itemIn.getItemName());
            item_code.setText(itemIn.getItemCode());
            item_model.setText(itemIn.getItemModel());
            item_part_number.setText(itemIn.getItemPartNumber());
            serial_Number.setText(itemIn.getSerialNumber());
            barcode_number.setText(itemIn.getBarcodeNumber());
            quantityyy.setText(itemIn.getQuantity());

        }
        return convertView;
    }

    private Filter itemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ItemAuto> sugg = new ArrayList<>();

            if (constraint == null || constraint.length()==0){
                sugg.addAll(itemFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemAuto itemIs : itemFull){

                    if (itemIs.getItemName().toLowerCase().contains(filterPattern) || itemIs.getItemCode().toLowerCase().contains(filterPattern) ||
                            itemIs.getItemModel().toLowerCase().contains(filterPattern)  || itemIs.getItemPartNumber().toLowerCase().contains(filterPattern) ||
                    itemIs.getSerialNumber().toLowerCase().contains(filterPattern) || itemIs.getBarcodeNumber().toLowerCase().contains(filterPattern)){
                        sugg.add(itemIs);
                    }
                }
            }
            results.values = sugg;
            results.count=sugg.size();
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ItemAuto) resultValue).getItemModel();
        }
    };

}
