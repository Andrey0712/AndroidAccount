package com.example.atb.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atb.MainActivity;
import com.example.atb.R;
import com.example.atb.account.userscard.UserDTO;
import com.example.atb.account.userscard.UsersAdapter;
import com.example.atb.application.HomeApplication;
import com.example.atb.databinding.ActivityMainBinding;
import com.example.atb.databinding.ActivityUsersBinding;
import com.example.atb.network.BaseActivity;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.security.JwtSecurityService;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersActivity extends BaseActivity {
    ActivityUsersBinding binding;

    private UsersAdapter adapter;
    private RecyclerView rcvUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActivityTitle("Users");

        rcvUsers = findViewById(R.id.rcvUsers);
        rcvUsers.setHasFixedSize(true);
        rcvUsers.setLayoutManager(new GridLayoutManager(this, 2,
                LinearLayoutManager.VERTICAL, false));

        AccountService.getInstance()
                .jsonApi()
                .users()
                .enqueue(new Callback<List<UserDTO>>() {
                    @Override
                    public void onResponse(Call<List<UserDTO>> call, Response<List<UserDTO>> response) {
                        if(response.isSuccessful())
                        {
                            adapter=new UsersAdapter(response.body());
                            rcvUsers.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserDTO>> call, Throwable t) {

                    }


//
                });


//        List<UserDTO> userDTOS = new ArrayList<>();
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail("ss@gg.dd");
//        userDTO.setPhoto("/images/jgdrkpkn.hsz.jpeg");
//        userDTOS.add(userDTO);
//
//        UserDTO userDTO2 = new UserDTO();
//        userDTO2.setEmail("dd@vv.dd");
//        userDTO2.setPhoto("/images/smdz5rxg.uiv.jpeg");
//        userDTOS.add(userDTO2);
//        adapter=new UsersAdapter(userDTOS);
//        rcvUsers.setAdapter(adapter);

    }
}