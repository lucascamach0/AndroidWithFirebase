package com.example.lcamacho.firstapplication.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lcamacho.firstapplication.DAO.ConfiguracaoFirebase;
import com.example.lcamacho.firstapplication.Entidades.Usuarios;
import com.example.lcamacho.firstapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtSenha;
    private TextView txtAbreCadastro;
    private Button btnLogar;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogar = (Button) findViewById(R.id.btnLogar);
        txtAbreCadastro = (TextView) findViewById(R.id.txtCadastro);

        txtAbreCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreCadastro();
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtEmail.getText().toString().isEmpty() && !edtSenha.getText().toString().isEmpty()){

                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());

                    validarLogin();

                }else{
                    Toast.makeText(LoginActivity.this,"Preencha os campos de e-mail e senha!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(),usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this,"Login Efetuado com sucesso!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Senha Invalida!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirTelaPrincipal(){
        Intent intentAbrirTelaPrincipal = new Intent(LoginActivity.this,PrincipalActivity.class);
        startActivity(intentAbrirTelaPrincipal);
    }

    public void abreCadastro(){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }
}
