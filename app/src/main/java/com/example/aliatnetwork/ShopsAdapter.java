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

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopsView> {

    private ArrayList<ShopsListView> shopsListViews=new ArrayList<>();
    private Context context;

    public ShopsAdapter(Context context){this.context=context;}

    @NonNull

    @Override
    public ShopsAdapter.ShopsView onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list_item,parent,false);
       ShopsAdapter.ShopsView holder = new ShopsAdapter.ShopsView(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ShopsAdapter.ShopsView holder, int position) {

        holder.txtShopsId.setText(shopsListViews.get(position).getSHOPS_ID());
        holder.txtShopName.setText(shopsListViews.get(position).getSHOP_NAME());
        holder.txtOwner.setText(shopsListViews.get(position).getOWNER());
        holder.txtLati.setText(shopsListViews.get(position).getLATITUDE());
        holder.txtLong.setText(shopsListViews.get(position).getLONGTITUDE());
        holder.txtAd.setText(shopsListViews.get(position).getADDRESS());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,shopsListViews.get(position).getSHOPS_ID(),Toast.LENGTH_SHORT).show();
                System.out.println(shopsListViews.get(position).getSHOPS_ID());

                Intent intent = new Intent(context,ShopInfoActivity.class);
                intent.putExtra("message_key",shopsListViews.get(position).getSHOPS_ID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopsListViews.size();
    }
    public void setContacts(ArrayList<ShopsListView> shopsListViews ) {
        this.shopsListViews = shopsListViews;
        notifyDataSetChanged();
    }

    public class ShopsView extends RecyclerView.ViewHolder{
        public RelativeLayout parent;
        public TextView txtShopsId,txtLong,txtLati,txtAd,txtOwner,txtShopName;

        public ShopsView(@NonNull View itemView) {
            super(itemView);

        txtShopsId=itemView.findViewById(R.id.txtShopsID);
        txtShopName=itemView.findViewById(R.id.txtShopsName);
        txtOwner=itemView.findViewById(R.id.txtOwner);
        txtLati=itemView.findViewById(R.id.txtlati);
        txtLong=itemView.findViewById(R.id.txtlong);
        txtAd=itemView.findViewById(R.id.txtAdd);
        parent=itemView.findViewById(R.id.parentShop);


        }
    }
}
