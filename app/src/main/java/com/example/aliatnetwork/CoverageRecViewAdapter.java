package com.example.aliatnetwork;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoverageRecViewAdapter extends RecyclerView.Adapter<CoverageRecViewAdapter.ViewHolder> {

    private ArrayList<Coverage> coverages=new ArrayList<>();
    private Context context;
    public CoverageRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coverage_list_item,parent,false);
        CoverageRecViewAdapter.ViewHolder holder =new CoverageRecViewAdapter.ViewHolder (view);
        return holder;
    }



    @Override
    public void onBindViewHolder(@NonNull CoverageRecViewAdapter.ViewHolder holder, int position) {
        holder.txtsignal.setText(coverages.get(position).getCovsignal ());
        holder.txtspdlat.setText(coverages.get(position).getCovlat ());
        holder.txtspdlng.setText(coverages.get(position).getCovlng ());
        holder.txtspddate.setText(coverages.get (position).getCovdate ().toString ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,coverages.get(position).getCovsignal () ,Toast.LENGTH_SHORT).show();
                System.out.println(coverages.get(position).getCovsignal ());
            }
        });



    }

    @Override
    public int getItemCount() {
        return coverages.size();
    }

    public void setContacts(ArrayList<Coverage> coverages) {
        this.coverages = coverages;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtsignal,txtspdlat,txtspdlng,txtspddate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtsignal=itemView.findViewById(R.id.txtsignal);
            txtspdlat=itemView.findViewById(R.id.txtspdlat);
            txtspdlng=itemView.findViewById(R.id.txtspdlng);
            txtspddate=itemView.findViewById(R.id.txtspddate);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
