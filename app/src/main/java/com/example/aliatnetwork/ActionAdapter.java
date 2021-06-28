package com.example.aliatnetwork;

import android.content.Context;
import android.content.Intent;
import android.service.autofill.OnClickAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionView> {

    private ArrayList<TicketActionView> ActionS;


    private Context context;


    public ActionAdapter(Context context, ArrayList<TicketActionView>ActionS){
        this.context=context;
        this.ActionS=ActionS;
    }




    @NonNull
    @Override
    public ActionAdapter.ActionView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.action_item,parent,false);
        ActionAdapter.ActionView holder = new ActionAdapter.ActionView (view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActionAdapter.ActionView holder, int position) {

        holder.txtActionID.setText(ActionS.get(position).getACTION_ID());
        holder.txtAction.setText(ActionS.get(position).getACTION());
        holder.txtStatus.setText(ActionS.get(position).getSTATUS());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return ActionS.size();
    }
    public void setContacts(ArrayList<TicketActionView> actionS ) {
        this.ActionS = actionS;
        notifyDataSetChanged();
    }
    public class ActionView extends RecyclerView.ViewHolder{
        public RelativeLayout parent;
        public TextView txtActionID,txtAction,txtStatus;

        public ActionView(@NonNull View itemView) {
            super(itemView);

            txtActionID=itemView.findViewById(R.id.actionId);
            txtAction=itemView.findViewById(R.id.action);
            txtStatus=itemView.findViewById(R.id.status);


        }
    }
}
