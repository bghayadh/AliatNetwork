package com.example.aliatnetwork;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class InsertDialog extends DialogFragment {


    private EditText iName,iCode,iModel,iPartNumber,sNumber,bNumber,qty;
    private Button btnInsert,btnCancel;
    private static final String TAG="Dialoge";

    public interface onInputSelected{
        void   sendInput(String name,String code,String model,String partNumber,String serial,String barcode,String qt);
    }
    public  onInputSelected onInputSelected;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_scan,container,false);

        iName=view.findViewById(R.id.item_Name);
        iCode=view.findViewById(R.id.item_Code);
        iModel=view.findViewById(R.id.item_Model);
        iPartNumber=view.findViewById(R.id.item_PartNumber);
        sNumber=view.findViewById(R.id.serial_Number);
        bNumber=view.findViewById(R.id.barcode_Number);
        qty=view.findViewById(R.id.qty);
        btnInsert= view.findViewById(R.id.btnInsertDialog);
        btnCancel=view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: closing dialog");
                getDialog().dismiss();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: capturing Input");

                String itemName = iName.getText().toString();
                String itemCode = iCode.getText().toString();
                String itemModel = iModel.getText().toString();
                String itemPartNumber = iPartNumber.getText().toString();
                String serialNumber = sNumber.getText().toString();
                String barcodeNumber = bNumber.getText().toString();
                String qtyy = "1";

                onInputSelected.sendInput(itemName,itemCode,itemModel,itemPartNumber,serialNumber,barcodeNumber,qtyy);

                if (!itemName.equals("") || !itemCode.equals("") || !itemModel.equals("") || !itemPartNumber.equals("") || !serialNumber.equals("") || !barcodeNumber.equals("") || !qtyy.equals("") ){

                    onInputSelected.sendInput(itemName,itemCode,itemModel,itemPartNumber,serialNumber,barcodeNumber,qtyy);
                    getDialog().dismiss();

                }
                if (itemName.equals("") || !itemCode.equals("") || itemModel.equals("") || itemPartNumber.equals("") || serialNumber.equals("") || !barcodeNumber.equals("") || qtyy.equals("") )
                {

                }






            }
        });






        return view;
    }

    @Override
    public void onAttach(Context context) {
               super.onAttach(context);

               try {
                   onInputSelected = (onInputSelected) getTargetFragment();

               }catch (ClassCastException e){
                    Log.e(TAG,"onAttach: ClassCastException: "+e.getMessage());
               }
    }
}
