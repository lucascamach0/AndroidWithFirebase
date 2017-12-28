package com.example.lcamacho.firstapplication.Activity;

import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.lcamacho.firstapplication.DAO.ConfiguracaoFirebase;
import com.example.lcamacho.firstapplication.Entidades.Usuarios;
import com.example.lcamacho.firstapplication.Helper.Base64Custom;
import com.example.lcamacho.firstapplication.Helper.Preferencias;
import com.example.lcamacho.firstapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import javax.net.ssl.*;


public class CadastroActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtNome;
    private EditText edtSobrenome;
    private EditText edtSenha;
    private EditText edtConfirmarSenha;
    private EditText edtAniversario;
    private RadioButton rbMasculino;
    private RadioButton rbfeminino;
    private Button btnGravar;
    private FirebaseAuth autenticacao;

   // private Connection connection;

    private Usuarios usuarios;

//    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, java.sql.SQLException {
//
//        Class.forName(driver);
//
//        return DriverManager.getConnection(url,username,password);
//        //return DriverManager.getConnection(url, username, password);
//    }
//    public static Connection createConnection() throws ClassNotFoundException, java.sql.SQLException {
//        return createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        new JSONTask().execute("http://172.16.96.221:8084/mobileServices/controller/buscarMso?mso=CTBC");

        edtEmail = (EditText) findViewById(R.id.edtCardEmail);
        edtNome = (EditText) findViewById(R.id.edtCardNome);
        edtSobrenome = (EditText) findViewById(R.id.edtCardSobrenome);
        edtSenha = (EditText) findViewById(R.id.edtCardSenha);
        edtConfirmarSenha = (EditText) findViewById(R.id.edtCardConfirmarSenha);
        edtAniversario = (EditText) findViewById(R.id.edtCardAniversario);
        rbMasculino = (RadioButton) findViewById(R.id.rbMasculino);
        rbfeminino = (RadioButton) findViewById(R.id.rbFeminino);

        btnGravar = (Button) findViewById(R.id.btnGravar);

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSenha.getText().toString().equals(edtConfirmarSenha.getText().toString())){

                    usuarios = new Usuarios();
                    usuarios.setNome(edtNome.getText().toString());
                    usuarios.setSobrenome(edtSobrenome.getText().toString());
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());
                    usuarios.setAniversario(edtAniversario.getText().toString());
                    if(rbfeminino.isChecked()) {
                        usuarios.setSexo("Feminino");
                    }else{
                        usuarios.setSexo("Masculino");
                    }

                    cadastrarUsuario();

                }else{
                    Toast.makeText(CadastroActivity.this, "As senhas não diferentes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,"Usuario Cadastrado com sucesso",Toast.LENGTH_LONG).show();

                    String identificador = Base64Custom.codificarBase64(usuarios.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(identificador);
                    usuarios.salvar();

                    Preferencias preferenciasAndroid = new Preferencias(CadastroActivity.this);
                    preferenciasAndroid.salvarUsuarioPreferencias(identificador,usuarios.getNome());

                    abrirLoginUsuario();
                }
                else{
                    String exececao = "";
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        exececao = "Senha fraca";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exececao = "O e-mail digitado é invalido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e){
                        exececao = "Esse e-mail já está cadastrado no sistema";
                    }catch (Exception e){
                        exececao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, "Erro: " + exececao, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class JSONTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            BufferedReader reader = null;
            HttpURLConnection connection = null;

            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();

            }finally {
                if(connection != null){
                    connection.disconnect();
                }

                try {
                    if(reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
