import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.w_people.R;
import com.example.w_people.activities.ProfilePreviewActivity;
import com.example.w_people.models.User;

import java.util.List;
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;

    public SearchAdapter(Context context, List<User> userList) {
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

        holder.textName.setText(user.getFullName());
        holder.textEmail.setText(user.getEmail());

        Glide.with(context)
                .load(user.getProfileImage())
                .placeholder(R.drawable.ic_user_avatar_placeholder)
                .into(holder.imageProfile);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfilePreviewActivity.class);
            intent.putExtra("userId", user.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView textName, textEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
            textEmail = itemView.findViewById(R.id.textEmail);
        }
    }
}
