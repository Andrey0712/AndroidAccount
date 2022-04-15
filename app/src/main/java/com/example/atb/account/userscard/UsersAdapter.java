package com.example.atb.account.userscard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.atb.R;
import com.example.atb.application.HomeApplication;
import com.example.atb.constants.Urls;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UserCardViewHolder> {
    private List<UserDTO> users;

    public UsersAdapter(List<UserDTO> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_user, parent, false);
        return new UserCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCardViewHolder holder, int position) {
        if(users!=null && position<users.size())
        {
            UserDTO user = users.get(position);
            holder.useremail.setText(user.getEmail());
            String url = Urls.BASE+user.getPhoto();
            Glide.with(HomeApplication.getAppContext())
                    .load(url)
                    .apply(new RequestOptions().override(600, 300))
                    .into(holder.userimg);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
