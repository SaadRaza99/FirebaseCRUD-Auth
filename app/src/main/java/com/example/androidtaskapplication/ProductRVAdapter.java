package com.example.androidtaskapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.ViewHolder> {
    // creating variables for our list, context, interface and position.
    private ArrayList<ProdectRVModal> productRVModalArrayList;
    private Context context;
    private ProductClickInterface productClickInterface;
    int lastPos = -1;

    // creating a constructor.
    public ProductRVAdapter(ArrayList<ProdectRVModal> productRVModalArrayList, Context context, ProductClickInterface productClickInterface) {
        this.productRVModalArrayList = productRVModalArrayList;
        this.context = context;
        this.productClickInterface = productClickInterface;
    }

    @NonNull
    @Override
    public ProductRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.product_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRVAdapter.ViewHolder holder, int position) {
        // setting data to our recycler view item on below line.
        ProdectRVModal productRVModal = productRVModalArrayList.get(position);
        holder.productTV.setText(productRVModal.getProductName());
        holder.productPriceTV.setText("Rs. " + productRVModal.getProductPrice());
        // adding animation to recycler view item on below line.
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickInterface.onProductViewClick(position);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickInterface.onProductEditClick(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickInterface.onProductDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private TextView productTV, productPriceTV,view,edit,delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            productTV = itemView.findViewById(R.id.idTVProductName);
            productPriceTV = itemView.findViewById(R.id.idTVProductPrice);
            view=itemView.findViewById(R.id.view);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    // creating a interface for on click
    public interface ProductClickInterface {
        void onProductViewClick(int position);
        void onProductEditClick(int position);
        void onProductDeleteClick(int position);
    }
}
