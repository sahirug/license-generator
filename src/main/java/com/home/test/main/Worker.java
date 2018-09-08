package com.home.test.main;

import com.google.gson.JsonObject;
import com.home.test.keygenerator.KeyGen;
import com.home.test.utils.DecryptionManager;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class Worker implements Runnable {

    private static final URL PRIVATE_KEY_URL = Thread.currentThread().getContextClassLoader().getResource("private-key.cen");
    private static final URL PUBLIC_KEY_URL = Thread.currentThread().getContextClassLoader().getResource("public-key.cen");

    private static final String RANDOM_MACHINE_ID = "DKFSS-KGK32-GDKGN-SDFRW-1";

    DecryptionManager manager;

    @Override
    public void run() {
        try {
            this.checkKeyExistence();
            manager = new DecryptionManager(PRIVATE_KEY_URL.toURI(), PUBLIC_KEY_URL.toURI());
            String cipherText = this.encrypt(RANDOM_MACHINE_ID);
            printToConsole(cipherText, "Cipher Text");
            String plainText = decrypt(cipherText);
            printToConsole(plainText, "Plain Text");

            JsonObject objToEncrypt = generateJson(plainText);
            String productKey = this.encrypt(objToEncrypt.toString());
            printToConsole(productKey, "Product Key");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkKeyExistence() throws Exception {
        if( !(KeyGen.isKeyExisting(PRIVATE_KEY_URL.toURI()) || KeyGen.isKeyExisting(PUBLIC_KEY_URL.toURI())) ){
            KeyGen gen = new KeyGen(2048);
            gen.writeToFile(PRIVATE_KEY_URL.toURI(), gen.getPrivateKey().getEncoded());
            gen.writeToFile(PUBLIC_KEY_URL.toURI(), gen.getPublicKey().getEncoded());
        }
    }

    private String encrypt(String plainText) throws Exception {
        return manager.encryptMessage(RANDOM_MACHINE_ID);
    }

    private String decrypt(String cipherText) throws Exception {
        return manager.decryptMessage(cipherText);
    }

    private JsonObject generateJson(String computerId) {
        Date startDate = Calendar.getInstance().getTime();

        Calendar cal = Calendar.getInstance();
        cal.set(2020, 1, 1, 0,0,0);
        cal.setTimeInMillis(System.currentTimeMillis());
        Date expDate = new Date(new Date().getTime());

        JsonObject obj = new JsonObject();
        obj.addProperty("name", "license-01");
        obj.addProperty("start-date", startDate.toString());
        obj.addProperty("expiry-date", expDate.toString());
        obj.addProperty("computerId", computerId);

        return obj;
    }

    private void printToConsole(String message, String label) {
        System.out.println("=====================================================================================");
        System.out.println("@@ "+ label +" = " + message +" @@");
        System.out.println("=====================================================================================");
    }
}
