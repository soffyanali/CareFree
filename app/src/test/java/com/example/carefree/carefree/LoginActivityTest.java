package com.example.carefree.carefree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;


public class LoginActivityTest {
    @Test
    public void onCreate() throws Exception {

    }

    //Test case to find out JSON produce correct value
    @Test
    public void readAllusers() throws Exception {

        LoginActivity lobj=new LoginActivity();
        HashMap<String, String> users=lobj.readAllusers();

        //Check if password is correct
        assertEquals(users.get("soffyanit"),"soffyan",.1);
    }

}