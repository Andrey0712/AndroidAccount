package com.example.atb.network.account;

import com.example.atb.account.userscard.UserDTO;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.LoginDto;
import com.example.atb.network.account.dto.RegisterDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountApi {
    @POST("/api/account/register")
    public Call<AccountResponseDTO> register(@Body RegisterDTO model);
    @POST("/api/account/login")
    public Call<AccountResponseDTO> login(@Body LoginDto model);
    @GET("/api/account/users")
    public Call<List<UserDTO>> users();

}
