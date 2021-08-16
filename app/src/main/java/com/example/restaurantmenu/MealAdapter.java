package com.example.restaurantmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    private ArrayList<Meal> meals;
    private String type;
    private TextView textView;
    private OnRVCartIVClickListener listener;

    public MealAdapter(ArrayList<Meal> meals, String type, TextView textView, OnRVCartIVClickListener listener) {
        this.meals = meals;
        this.type = type;
        this.textView = textView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal meal = meals.get(position);
        holder.mealIV.setImageResource(meal.getImage());
        holder.nameTV.setText(meal.getName());
        holder.ingredientsTV.setText(meal.getIngredients());
        holder.priceTV.setText("$"+meal.getPrice());
        holder.cartIV.setImageResource(meal.getCartIcon());
        holder.mealIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mealIV, cartIV;
        TextView nameTV, ingredientsTV, priceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mealIV = itemView.findViewById(R.id.iv_meal);
            cartIV = itemView.findViewById(R.id.iv_cart);
            nameTV = itemView.findViewById(R.id.tv_name);
            ingredientsTV = itemView.findViewById(R.id.tv_ingredients);
            priceTV = itemView.findViewById(R.id.tv_price);

            cartIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Meal meal = (meals.get(position));
                    listener.OnClickListener(meal);
                    // update cart
                    if (meal.getCart() == 0) {
                        meal.setCart(1);
                        meal.setCartIcon(R.drawable.ic_remove_cart);
                        notifyItemChanged(position);
                    } else if (meal.getCart() == 1) {
                        if (type.equals("Cart")) {
                            meals.remove(position);
                            notifyItemRemoved(position);
                            if (meals.size() == 0)
                                textView.setText("The meals you add will appear here.");
                        } else {
                            meal.setCart(0);
                            meal.setCartIcon(R.drawable.ic_add_cart);
                            notifyItemChanged(position);
                        }
                    }
                }
            });
        }
    }
}
