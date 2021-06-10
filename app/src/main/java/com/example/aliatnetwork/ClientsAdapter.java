package com.example.aliatnetwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;


public class ClientsAdapter extends ArrayAdapter<Clients> {

    private List<Clients> clientsFull;
    public ClientsAdapter(@NonNull Context context, @NonNull List<Clients> clientsList) {
        super(context, 0,clientsList);

        clientsFull = new ArrayList<>(clientsList);
    }




    @NonNull
    @Override
    public Filter getFilter() {
        return clientFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.client_autocomplete_row, parent,false
            );
        }

        TextView textViewClientId = convertView.findViewById(R.id.edit_text_client_id);
        TextView textViewClientFirstName = convertView.findViewById(R.id.edit_text_client_first_name);
        TextView textViewClientLastName = convertView.findViewById(R.id.edit_text_client_last_name);

        Clients clients = getItem(position);

        if(clients != null){
            textViewClientId.setText(clients.getCLIENT_ID());
            textViewClientFirstName.setText(clients.getFIRST_NAME());
            textViewClientLastName.setText(clients.getLAST_NAME());
        }
        return convertView;
    }

    private Filter clientFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Clients> suggestions = new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                suggestions.addAll(clientsFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Clients item :clientsFull){
                    if (item.getCLIENT_ID().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count=suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
        clear();
        addAll((List)results.values) ;
        notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Clients) resultValue).getCLIENT_ID();
        }
    };
}
