package com.example.aliatnetwork;

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

public class SiteIdAdapter  extends ArrayAdapter<SiteId> {
    private List<SiteId> SiteIdFull;

    public SiteIdAdapter(@NonNull FragmentActivity TicketInfoFragment, @NonNull List<SiteId> siteIDList) {
        super(TicketInfoFragment, 0, siteIDList);

        SiteIdFull = new ArrayList<>(siteIDList);
    }



    @NonNull
    @Override
    public Filter getFilter() {
        return siteIdfilter;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.site_id_autocomplete_row, parent, false
            );
        }

        TextView textViewSiteID = convertView.findViewById(R.id.edit_text_site_id);
        TextView textViewSiteName= convertView.findViewById(R.id.edit_text_site_name);

        SiteId siteID = getItem(position);

        if (siteID != null) {
            textViewSiteID.setText(siteID.getSITE_ID());
            textViewSiteName.setText(siteID.getWARE_NAME());
        }

        return convertView;
    }

    private Filter siteIdfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<SiteId> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(SiteIdFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SiteId item : SiteIdFull) {
                    if (item.getSITE_ID().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SiteId) resultValue).getSITE_ID();
        }
    };
}

