package com.example.aliatnetwork;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.proxy.annotation.Post;

public class TicketRecViewAdapter extends RecyclerView.Adapter<TicketRecViewAdapter.ViewHolder> {

    private ArrayList<TicketListView> tickets =new ArrayList<>();
    private Context context;

    public TicketRecViewAdapter(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public TicketRecViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_list_item,parent,false);
        TicketRecViewAdapter.ViewHolder holder =new TicketRecViewAdapter.ViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TicketRecViewAdapter.ViewHolder holder, int position) {
        holder.txtTicketId.setText(tickets.get(position).getTICKET_ID ());
        holder.txtDepar.setText(tickets.get(position).getDEPARTMENT ());
        holder.txtSubj.setText(tickets.get(position).getSUBJECT ());
        holder.txtlati.setText(tickets.get(position).getLATITUDE());
        holder.txtlong.setText(tickets.get(position).getLONGITUDE());
        holder.txtstatus.setText(tickets.get(position).getSTATUS());




        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,tickets.get(position).getTICKET_ID () ,Toast.LENGTH_SHORT).show();
                System.out.println(tickets.get(position).getTICKET_ID ());

                // pass on click wareid value to new activity Sitinforactivity
                Intent intent =  new Intent(context, TicketInfoActivity.class);
                intent.putExtra("message_key", tickets.get(position).getTICKET_ID ());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public void setContacts(ArrayList<TicketListView> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parent;
        private TextView txtTicketId,txtDepar,txtSubj,txtstatus,txtlong,txtlati;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTicketId=itemView.findViewById(R.id.txtTicketId);
            txtDepar=itemView.findViewById(R.id.txtDepar);
            txtSubj=itemView.findViewById(R.id.txtSubj);
            txtlati=itemView.findViewById(R.id.txtlati);
            txtlong=itemView.findViewById(R.id.txtlong);
            txtstatus=itemView.findViewById(R.id.txtstatus);
            parent=itemView.findViewById(R.id.parent);
        }
    }
}
