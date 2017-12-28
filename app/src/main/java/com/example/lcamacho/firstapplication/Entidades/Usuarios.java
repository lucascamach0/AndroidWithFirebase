package com.example.lcamacho.firstapplication.Entidades;

import com.example.lcamacho.firstapplication.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by lcamacho on 24/07/2017.
 */

public class Usuarios {

    private String id;
    private String email;
    private String senha;
    private String nome;
    private String sobrenome;
    private String aniversario;
    private String sexo;

    public Usuarios() {
    }

    @Exclude
    public Map<String , Object> toMap(){
        HashMap<String, Object> hashMapUsuario = new HashMap<>();

        hashMapUsuario.put("id",getId());
        hashMapUsuario.put("email",getEmail());
        hashMapUsuario.put("senha",getSenha());
        hashMapUsuario.put("nome",getNome());
        hashMapUsuario.put("sobrenome",getSobrenome());
        hashMapUsuario.put("aniversario",getAniversario());
        hashMapUsuario.put("sexo",getSexo());

        return hashMapUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getAniversario() {
        return aniversario;
    }

    public void setAniversario(String aniversario) {
        this.aniversario = aniversario;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuario").child(String.valueOf(getId())).setValue(this);
    }


}

