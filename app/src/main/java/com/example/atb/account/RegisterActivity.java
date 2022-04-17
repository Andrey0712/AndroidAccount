package com.example.atb.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.atb.MainActivity;
import com.example.atb.R;
import com.example.atb.application.HomeApplication;
import com.example.atb.databinding.ActivityLoginBinding;
import com.example.atb.databinding.ActivityRegisterBinding;
import com.example.atb.network.BaseActivity;
import com.example.atb.network.account.AccountService;
import com.example.atb.network.account.dto.AccountResponseDTO;
import com.example.atb.network.account.dto.RegisterDTO;
import com.example.atb.network.account.dto.ValidationRegisterDTO;
import com.example.atb.security.JwtSecurityService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {
    ActivityRegisterBinding binding;

    private TextView tvInfo;
    private TextInputLayout textFieldEmail;
    private TextInputEditText txtEmail;
    private TextInputLayout textFieldFirstName;
    private TextInputEditText txtFirstName;
    private TextInputLayout textFieldSecondName;
    private TextInputEditText txtSecondName;
    private TextInputLayout textFieldPhone;
    private TextInputEditText txtPhone;
    private TextInputLayout textPassword;
    private TextInputEditText txtPassword;
    private TextInputLayout textConfirmPassword;
    private TextInputEditText txtConfirmPassword;
    private ProgressBar progressBar;
    private Button button;

    private Handler handler = new Handler();
    // One Preview Image
    ImageView IVPreviewImage;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    String sImage="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setActivityTitle("Register");

        tvInfo=findViewById(R.id.tvInfo);
        textFieldEmail=findViewById(R.id.textFieldEmail);
        txtEmail=findViewById(R.id.txtEmail);
        textFieldFirstName=findViewById(R.id.textFieldFirstName);
        txtFirstName=findViewById(R.id.txtFirstName);
        textFieldSecondName=findViewById(R.id.textFieldSecondName);
        txtSecondName=findViewById(R.id.txtSecondName);
        textFieldPhone=findViewById(R.id.textFieldPhone);
        txtPhone=findViewById(R.id.txtPhone);
        textPassword=findViewById(R.id.textPassword);
        txtPassword=findViewById(R.id.txtPassword);
        textConfirmPassword=findViewById(R.id.textConfirmPassword);
        txtConfirmPassword=findViewById(R.id.txtConfirmPassword);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        progressBar = findViewById(R.id.progressBar);
        this.button = (Button) this.findViewById(R.id.button);

    }

    public void handleSelectImageClick(View view) {
        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri uri = data.getData();
                // update the preview image in the layout
                IVPreviewImage.setImageURI(uri);
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // initialize byte stream
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                // compress Bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                // Initialize byte array
                byte[] bytes=stream.toByteArray();
                // get base64 encoded string
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);
            }
        }
    }

    public void handleClick(View view) {
        RegisterDTO registerDTO = new RegisterDTO();
        this.progressBar.setIndeterminate(true);

        Thread thread = new Thread(new Runnable()  {

            @Override
            public void run() {
                // Update interface
                handler.post(new Runnable() {
                    public void run() {
                        registerDTO.setEmail(txtEmail.getText().toString());
                        registerDTO.setFirstName(txtFirstName.getText().toString());
                        registerDTO.setSecondName(txtSecondName.getText().toString());
                        registerDTO.setPhone(txtPhone.getText().toString());
                        registerDTO.setPassword(txtPassword.getText().toString());
                        registerDTO.setConfirmPassword(txtConfirmPassword.getText().toString());

                        registerDTO.setPhoto(sImage);


                        if (!validationFields(registerDTO))
                            return;


                        AccountService.getInstance()
                                .jsonApi()
                                .register(registerDTO)
                                .enqueue(new Callback<AccountResponseDTO>() {
                                    @Override
                                    public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
                                        if (response.isSuccessful()) {
                                            AccountResponseDTO data = response.body();
                                            JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
                                            jwtService.saveJwtToken(data.getToken());
                                            //tvInfo.setText("response is good");
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            try {
                                                showErrorsServer(response.errorBody().string());
                                            } catch (Exception e) {
                                                System.out.println("------Error response parse body-----");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
                                        //String str = t.toString();
                                        t.printStackTrace();//печатает места, где произошло исключение в исходном коде
                                        Toast.makeText(RegisterActivity.this,
                                                "Помилка : " + t.getMessage(),
                                                Toast.LENGTH_LONG).show();

                                    }
                                });
                        //buttonStart2.setEnabled(false);
                    }
                });
                // Do something ... (Update database,..)
                SystemClock.sleep(1000); // Sleep 1 seconds.

                progressBar.setIndeterminate(false);
                progressBar.setMax(1);
                progressBar.setProgress(1);


            }
        });
        thread.start();
    }





    private boolean validationFields(RegisterDTO registerDTO) {
        textFieldEmail.setError("");
        if (registerDTO.getEmail().equals("")) {
            textFieldEmail.setError("Вкажіть пошту");
            return false;
        }

        textFieldFirstName.setError("");
        if (registerDTO.getFirstName().equals("")) {
            textFieldFirstName.setError("Вкажіть ім'я");
            return false;
        }
        textFieldSecondName.setError("");
        if (registerDTO.getSecondName().equals("")) {
            textFieldSecondName.setError("Вкажіть Призвище");
            return false;
        }
        textFieldPhone.setError("");
        if (registerDTO.getPhone().equals("")) {
            textFieldPhone.setError("Вкажіть телефон");
            return false;
        }
        textPassword.setError("");
        if (registerDTO.getPassword().equals("")) {
            textPassword.setError("Вкажіть пароль");
            return false;
        }
        textConfirmPassword.setError("");
        if (registerDTO.getConfirmPassword().equals("")) {
            textConfirmPassword.setError("Підтвердіть пароль");
            return false;
        }

        textFieldFirstName.setError("");
        if (sImage.equals("")) {
            textFieldFirstName.setError("Обнріть фотку");
            return false;
        }
        return true;
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
        textFieldEmail.setError(str);

        str = "";
        if (result.getErrors().getFirstName() != null) {
            for (String item : result.getErrors().getFirstName()) {
                str += item + "\n";
            }
        }
        textFieldFirstName.setError(str);

        str = "";
        if (result.getErrors().getSecondName() != null) {
            for (String item : result.getErrors().getSecondName()) {
                str += item + "\n";
            }
        }
        textFieldSecondName.setError(str);

        str = "";
        if (result.getErrors().getPhone() != null) {
            for (String item : result.getErrors().getPhone()) {
                str += item + "\n";
            }
        }
        textFieldPhone.setError(str);

        str = "";
        if (result.getErrors().getPassword() != null) {
            for (String item : result.getErrors().getPassword()) {
                str += item + "\n";
            }
        }
        textPassword.setError(str);

        str = "";
        if (result.getErrors().getConfirmPassword() != null) {
            for (String item : result.getErrors().getConfirmPassword()) {
                str += item + "\n";
            }
        }
        textConfirmPassword.setError(str);
    }

//    private void doStartProgressBar()  {
//
//        final int MAX = 110;
//        this.progressBar.setMax(MAX);
//        RegisterDTO registerDTO = new RegisterDTO();
//        if (!validationFields(registerDTO))
//            return;
//        Thread thread = new Thread(new Runnable()  {
//
//            @Override
//            public void run() {
//                handler.post(new Runnable() {
//                    public void run() {
//                        button.setEnabled(false);
//                    }
//                });
//                for( int i =0; i < MAX; i++) {
//                    final int progress = i + 1;
//                    AccountService.getInstance()
//                            .jsonApi()
//                            .register(registerDTO)
//                            .enqueue(new Callback<AccountResponseDTO>() {
//                                @Override
//                                public void onResponse(Call<AccountResponseDTO> call, Response<AccountResponseDTO> response) {
//                                    if (response.isSuccessful()) {
//                                        AccountResponseDTO data = response.body();
//                                        JwtSecurityService jwtService = (JwtSecurityService) HomeApplication.getInstance();
//                                        jwtService.saveJwtToken(data.getToken());
//                                        //tvInfo.setText("response is good");
//                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                        startActivity(intent);
//                                    } else {
//                                        try {
//                                            showErrorsServer(response.errorBody().string());
//                                        } catch (Exception e) {
//                                            System.out.println("------Error response parse body-----");
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<AccountResponseDTO> call, Throwable t) {
//                                    String str = t.toString();
//                                    int a = 12;
//                                }
//                            });
//                    // Do something (Download, Upload, Update database,..)
//                    SystemClock.sleep(200); // Sleep 20 milliseconds.
//
//                    // Update interface.
//                    handler.post(new Runnable() {
//                        public void run() {
//                            progressBar.setProgress(progress);
//                            int percent = (progress * 100) / MAX;
//
//                            //textViewInfo1.setText("Percent: " + percent + " %");
//                            if(progress == MAX)  {
//                                //textViewInfo1.setText("Completed!");
//                                button.setEnabled(true);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//        thread.start();
//    }
}