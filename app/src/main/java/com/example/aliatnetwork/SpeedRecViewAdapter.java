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

public class SpeedRecViewAdapter extends RecyclerView.Adapter<SpeedRecViewAdapter.ViewHolder>{

    private ArrayList<Speed> speeds=new ArrayList<>();
    private Context context;
    public SpeedRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.speed_list_item,parent,false);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.txtName.setText(contacts.get(position).getName());
        holder.txtdownload.setText(speeds.get(position).getSpddownload ());
        holder.txtupload.setText(speeds.get(position).getSpdupload ());
        holder.txtspdlat.setText(speeds.get(position).getSpdlat ());
        holder.txtspdlng.setText(speeds.get(position).getSpdlng ());
        holder.txtspddate.setText(speeds.get (position).getSpddate ().toString ());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,speeds.get(position).getSpddownload() ,Toast.LENGTH_SHORT).show();
                System.out.println(speeds.get(position).getSpddownload());
            }
        });



    }

    @Override
    public int getItemCount() {
        return speeds.size();
    }

    public void setContacts(ArrayList<Speed> speeds) {
        this.speeds = speeds;
        notifyDataSetChanged();
    }

     public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtdownload,txtupload,txtspdlat,txtspdlng,txtspddate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdownload=itemView.findViewById(R.id.txtdownload);
            txtupload=itemView.findViewById(R.id.txtupload);
            txtspdlat=itemView.findViewById(R.id.txtspdlat);
            txtspdlng=itemView.findViewById(R.id.txtspdlng);
            txtspddate=itemView.findViewById(R.id.txtspddate);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
