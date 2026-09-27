package com.example.mad_assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_assignment.AddEditIngredientActivity;
import com.example.mad_assignment.R;
import com.example.mad_assignment.models.PantryItem;

import java.util.ArrayList;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private Context context;
    private ArrayList<PantryItem> pantryItems;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDelete(PantryItem item);
    }

    public PantryAdapter(Context context,ArrayList<PantryItem> pantryItems, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.pantryItems = pantryItems;
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);
        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());

        if (item.getExpiryDate() == null || item.getExpiryDate().trim().isEmpty()) {
            holder.tvExpiry.setText("Expiry: Not specified");
        } else {
            holder.tvExpiry.setText("Expiry: " + item.getExpiryDate());
        }

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditIngredientActivity.class);

            intent.putExtra("ingredient_id", item.getId());
            intent.putExtra("ingredient_name", item.getName());
            intent.putExtra("ingredient_quantity", item.getQuantity());
            intent.putExtra("ingredient_unit", item.getUnit());
            intent.putExtra("ingredient_expiry", item.getExpiryDate());

            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public void updateList(ArrayList<PantryItem> newList) {
        pantryItems = newList;
        notifyDataSetChanged();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvQuantity;
        TextView tvExpiry;

        Button btnEdit;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvPantryName);
            tvQuantity = itemView.findViewById(R.id.tvPantryQuantity);
            tvExpiry = itemView.findViewById(R.id.tvPantryExpiry);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
