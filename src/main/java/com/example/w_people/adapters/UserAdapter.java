package com.example.w_people.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_people.R;
import com.example.w_people.activities.ChatActivity;
import com.example.w_people.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    // Constructor
    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Bind data to the view holder's views
        holder.usernameTextView.setText(user.getUsername());
        holder.fullNameTextView.setText(user.getFullName());
        holder.bioTextView.setText(user.getBio());

        // Set click listener to open ChatActivity
        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to open ChatActivity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("receiverId", user.getUid());  // Pass the user's UID
            intent.putExtra("receiverUsername", user.getUsername());  // Pass the user's username
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, fullNameTextView, bioTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
            bioTextView = itemView.findViewById(R.id.bioTextView);
        }
    }
}
