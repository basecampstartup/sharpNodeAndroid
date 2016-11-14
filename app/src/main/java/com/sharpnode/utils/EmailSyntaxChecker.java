
package com.sharpnode.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailSyntaxChecker {
    private static Pattern emailValidate;
    private static Matcher matcher;

    /**
     * Method to validate the input email.
     *
     * @param inputEmail The email to be validated.
     * @return A boolean if the email is valid or not
     */
    public static boolean check(String inputEmail) {
        //The regular expression of email.
        String emailRegEx = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*@\"\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (inputEmail.contains("*") || inputEmail.contains("#") || inputEmail.contains("/") || inputEmail.contains("&") || inputEmail.contains("%")) {
            return false;
        }
        // Checking if the email is valid or not.
        emailValidate = Pattern.compile(emailRegEx);
        matcher = emailValidate.matcher(inputEmail);
        boolean result = matcher.matches();

        //Return true if email is valid else return false.
        return result;
    }

}
