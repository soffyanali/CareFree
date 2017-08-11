package com.example.carefree.carefree;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class CustomerRegistrationTest
{

    @Test
    public void CustomerRegistration() throws Exception {

        //Test case pass
        CustomerRegistration lobj=new CustomerRegistration();
        Boolean check=lobj.insertToDatabase("Soffyan", "sof","","asdsad@gmail.com","089323233");
        //Check if password is correct
        assertEquals(check.toString(),"true",.1);

        //Test case Fail with missing one parameter
        CustomerRegistration lobj1=new CustomerRegistration();
        Boolean check1=lobj1.insertToDatabase("","sof","","asdsad@gmail.com","089323233");
        //Check if password is correct
        assertEquals(check1.toString(),"false",.1);

        //Test case Fail with excedding parameter value
        CustomerRegistration lobj2=new CustomerRegistration();
        Boolean check3=lobj2.insertToDatabase("Soff","sof","","asdsadsdfsfsdfsdfsdfasdfdsafdsaf@gmail.com","089323233");
        //Check if password is correct
        assertEquals(check3.toString(),"false",.1);



    }



}