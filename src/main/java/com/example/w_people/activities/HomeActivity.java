package com.example.w_people.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.w_people.R;
import com.example.w_people.adapters.UserAdapter;
import com.example.w_people.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private final List<User> userList = new ArrayList<>();
    private final List<User> allUsers = new ArrayList<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);
        loadUsers();
        // Set up SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });
    }

    private void loadUsers() {
        firestore.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allUsers.clear();
                    allUsers.addAll(queryDocumentSnapshots.toObjects(User.class));
                    userList.clear();
                    userAdapter.notifyDataSetChanged();
                    Log.d("HomeActivity", "Total users loaded: " + allUsers.size());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HomeActivity.this, "Error loading users", Toast.LENGTH_SHORT).show();
                    Log.e("HomeActivity", "Load failed", e);
                });
    }

    private void filterUsers(String query) {
        userList.clear();

        if (query == null || query.trim().isEmpty()) {
            userList.clear();

        } else {
            String lowerQuery = query.toLowerCase();
            for (User user : allUsers) {
                if (user.getUsername() != null && user.getUsername().toLowerCase().contains(lowerQuery)) {
                    userList.add(user);

                }
            }
        }

        userAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
