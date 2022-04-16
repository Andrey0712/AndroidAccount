package com.example.atb.account;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atb.MainActivity;
import com.example.atb.R;
import com.example.atb.application.HomeApplication;
import com.example.atb.databinding.ActivityLoginBinding;
import com.example.atb.network.BaseActivity;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.LoginDto;
import com.example.atb.network.account.dto.ValidationRegisterDTO;
import com.example.atb.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;

    private TextView tvInfoLogin;
    private TextInputLayout textInputLoginEmail;
    private TextInputEditText txtLoginEmail;
    private TextInputLayout textInputLoginPassword;
    private TextInputEditText txtLoginPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActivityTitle("Login");

        tvInfoLogin=findViewById(R.id.tvInfoLogin);
        textInputLoginEmail=findViewById(R.id.textInputLoginEmail);
        txtLoginEmail=findViewById(R.id.txtLoginEmail);
        textInputLoginPassword=findViewById(R.id.textInputLoginPassword);
        txtLoginPassword=findViewById(R.id.txtLoginPassword);

    }

    public void  onLoginHandler(View view){

         LoginDto loginDto=new LoginDto();
        loginDto.setEmail(txtLoginEmail.getText().toString());
        loginDto.setPassword(txtLoginPassword.getText().toString());

        if(!validationFields(loginDto))
            return;

        AccountService.getInstance()
                .jsonApi()
                .login(loginDto)
                .enqueue(new Callback<AccountResponseDTO>() {

                    @Override
                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                        if(response.isSuccessful()){
                            AccountResponseDTO data= response.body();
                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                            jwtService.saveJwtToken(data.getToken());
                            Intent intent =new Intent(LoginActivity.this, UsersActivity.class);
                            startActivity(intent);
                        }
                        else {
                            try {
                                showErrorsServer(response.errorBody().string());
                            }
                            catch (Exception e){
                                System.out.println("Error response parse body");
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                        String str=t.toString();
                    }
                });
    }

    private  boolean validationFields(LoginDto loginDto){
        textInputLoginEmail.setError("");
        if(loginDto.getEmail().equals("")){
            textInputLoginEmail.setError("Вкажіть  E-mail");
            return false;
        }

        textInputLoginPassword.setError("");
        if (loginDto.getPassword().equals("")) {
            textInputLoginPassword.setError("Вкажіть пароль");
            return false;
        }

        return  true;
    }

    private void showErrorsServer(String json) {
        Gson gson = new Gson();
        ValidationRegisterDTO result = gson.fromJson(json, ValidationRegisterDTO.class);
        String str = "";
        if (result.getErrors().getEmail() != null) {
            for (String item : result.getErrors().getEmail()) {
                str += item + "\n";
            }
        }
        textInputLoginEmail.setError(str);

        str = "";
        if (result.getErrors().getPassword() != null) {
            for (String item : result.getErrors().getPassword()) {
                str += item + "\n";
            }
        }
        textInputLoginPassword.setError(str);

    }


}