package com.maktabsharif.homeservices.validation;

import java.util.regex.Pattern;

public class Validators {



    //validating number
    public boolean validateNumber(String number){
        if(Pattern.compile("\\d{1}").matcher(number).matches())
            return true;
        else
            return false;
    }

    //validating number
    public boolean validateEmail(String email){
        if(Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").matcher(email).matches())
            return true;
        else{
//            System.out.println("Invalid Email address.");
            return false;
        }

    }

    //validating password match
    public String validatePasswordMatch(String newPassword, String newPasswordConfirmation){
        if(newPassword.equals(newPasswordConfirmation))
            return newPassword;
        else{
            System.out.println("\nPasswords do not match.\n");
            return null;
        }
    }

    //validating password Policy
    public boolean validatePasswordPolicy(String password){
        if(password != null){
            if(Pattern.compile("^[A-Za-z0-9]{8,}").matcher(password).matches())
                return true;
            else{
                return false;
            }

        }
        else
            return false;
    }


    public boolean validateNewAndOldPasswordEquality(String newPassword, String oldPassword){
        if(newPassword != null){
            if(newPassword.equals(oldPassword)){
                System.out.println("\nNew and old passwords are the same.\n");
                return true;
            }
            else
                return false;
        }
        else
            return false;

    }

    public boolean validateImageExtension(String imagePth){
        if(Pattern.compile(".*\\.jpg").matcher(imagePth).matches())
            return true;
        else{
            return false;
        }
    }

    public  boolean validateCardNumber(String cardNumber){
        if(Pattern.compile("[0-9]{16}").matcher(cardNumber).matches())
            return true;
        else{
            return false;
        }
    }

    public  boolean validateCcv2(String cvv2){
        if(Pattern.compile("[0-9]{3,4}").matcher(cvv2).matches())
            return true;
        else{
            return false;
        }
    }

}
