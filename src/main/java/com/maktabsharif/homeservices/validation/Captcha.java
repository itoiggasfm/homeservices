package com.maktabsharif.homeservices.validation;

public class Captcha {
    public static String returnCaptchaText() throws Exception {
        int captchaLength = 5;
        return (generateCaptchaText(captchaLength));
    }

    public static String generateCaptchaText(int captchaLength) {
        // Characters to show in captcha A-Z, a-z & 1-9.
        String saltChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer captchaStrBuffer = new StringBuffer();
        java.util.Random rnd = new java.util.Random();

        // build a random captchaLength chars slat
        while (captchaStrBuffer.length() < captchaLength) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            captchaStrBuffer.append(saltChars.substring(index, index + 1));
        }

        return captchaStrBuffer.toString();
    }
}