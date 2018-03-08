package com.portal.datadit.inventorymanagement;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewHolder {
    TextView pName;
    TextView pQuantity;
    TextView pPrice;
    Button sale;

    public ViewHolder(View view) {

        pName = view.findViewById(R.id.textViewName);
        pQuantity = view.findViewById(R.id.textViewQuantity);
        pPrice = view.findViewById(R.id.textViewPrice);
        sale = view.findViewById(R.id.buttonSale);
    }
}
