package com.example.Forum.Entity;

import javax.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id_token;

    private int value;


    public Token(int value) {
        super();
        this.value = value;

    }

    public Token() {

    }

    public int getId_token() {
        return Id_token;
    }

    public void setId_token(int id_token) {
        this.Id_token = id_token;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}