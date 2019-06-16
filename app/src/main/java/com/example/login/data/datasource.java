package com.example.login.data;

import com.example.login.model.Account;

import java.util.ArrayList;
import java.util.List;

public class datasource {
    private static datasource instance;
    public static datasource getInstance()
    {
        if (instance==null){
            instance= new datasource();
        }
        return  instance;
    }



    public Account getAccount()
    {
        return new Account("ltvan","1751122","ltvan@apcs.vn");
    }
}
