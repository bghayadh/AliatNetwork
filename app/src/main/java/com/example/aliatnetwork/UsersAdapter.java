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

public class UsersAdapter extends ArrayAdapter<Users> {

    private List<Users> usersFull;
    public String employe;
    public UsersAdapter(@NonNull Context context, @NonNull List<Users> usersList) {
        super(context, 0  , usersList);
        usersFull = new ArrayList<>(usersList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return userFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.user_autocomplete_row,parent,false
            );
        }
        TextView txtUserFirstName = convertView.findViewById(R.id.user_first_name);
        TextView txtUserMidelName = convertView.findViewById(R.id.user_middel_name);
        TextView txtUserLastName  = convertView.findViewById(R.id.user_last_name);


        Users users = getItem(position);

        if (users!= null){
            txtUserFirstName.setText(users.getFirstName());
            txtUserMidelName.setText(users.getMiddleName());
            txtUserLastName.setText(users.getLastName());
        }

        return convertView;

    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            List<Users> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length()==0){
                suggestions.addAll(usersFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                String filterPattern2 = constraint.toString().toLowerCase().trim();
                for (Users item:usersFull){
                    if ((item.getFirstName().toLowerCase().contains(filterPattern))){
                        suggestions.add(item);
                    }
                }

            }

            results.values = suggestions;
            results.count =suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List)results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Users) resultValue).getFirstName();

        }
    };
}
