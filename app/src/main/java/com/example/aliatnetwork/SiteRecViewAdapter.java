package com.example.aliatnetwork;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.proxy.annotation.Post;

public class SiteRecViewAdapter extends RecyclerView.Adapter<SiteRecViewAdapter.ViewHolder> {

    private ArrayList<Sitelistview> sites=new ArrayList<>();
    private Context context;

    public SiteRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public SiteRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.site_list_item,parent,false);
        SiteRecViewAdapter.ViewHolder holder =new SiteRecViewAdapter.ViewHolder (view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull SiteRecViewAdapter.ViewHolder holder, int position) {
        holder.txtwareid.setText(sites.get(position).getWAREID ());
        holder.txtsiteid.setText(sites.get(position).getSITEID ());
        holder.txtwarename.setText(sites.get(position).getWARENAME ());
        holder.txtwareaddr.setText(sites.get(position).getWADDRESS ());
        holder.txtwarelat.setText(sites.get(position).getWARELAT ());
        holder.txtwarelng.setText(sites.get (position).getWARELNG ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,sites.get(position).getWAREID () ,Toast.LENGTH_SHORT).show();
                System.out.println(sites.get(position).getWAREID ());

                // pass on click wareid value to new activity Sitinforactivity
                Intent intent =  new Intent(context, SiteInfoActivity.class);
                intent.putExtra("message_key", sites.get(position).getWAREID ());
                context.startActivity(intent);

            }
        });



    }


    @Override
    public int getItemCount() {
        return sites.size();
    }

    public void setContacts(ArrayList<Sitelistview> sites) {
        this.sites = sites;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtwareid,txtsiteid,txtwarename,txtwareaddr,txtwarelat,txtwarelng;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtwareid=itemView.findViewById(R.id.txtwareid);
            txtsiteid=itemView.findViewById(R.id.txtsiteid);
            txtwarename=itemView.findViewById(R.id.txtwarename);
            txtwareaddr=itemView.findViewById(R.id.txtwareaddr);
            txtwarelat=itemView.findViewById(R.id.txtwarelat);
            txtwarelng=itemView.findViewById(R.id.txtwarelng);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
