package com.home.test.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.home.test.keygenerator.KeyGen;
import com.home.test.utils.Constants;
import com.home.test.utils.DecryptionManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

public class Worker implements Runnable {

    private static final String RANDOM_MACHINE_ID = "DKFSS-KGK32-GDKGN-SDFRW-1";

    private static final String LICENSE_PATH = "license/license.lic";

    DecryptionManager manager;

    @Override
    public void run() {
        try {
            this.checkKeyExistence();
            manager = new DecryptionManager(Constants.PRIVATE_KEY_URL.toURI(), Constants.PUBLIC_KEY_URL.toURI());
            String cipherText = this.encrypt(RANDOM_MACHINE_ID);
            printToConsole(cipherText, "Cipher Text");
            String plainText = decrypt(cipherText);
            printToConsole(plainText, "Plain Text");

            if(!licenseFileExists()) {
                String licenseJsonAsString = generateJson(decrypt(cipherText)).toString();
                String productKey = encrypt(licenseJsonAsString);
                File licenseFile = new File("src/main/resources/license/license.lic");
                licenseFile.createNewFile();
                FileUtils.writeByteArrayToFile(licenseFile, productKey.getBytes());
            }

            validateLicense();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkKeyExistence() throws Exception {
        if( !(KeyGen.isKeyExisting(Constants.PRIVATE_KEY_URL.toURI()) || KeyGen.isKeyExisting(Constants.PUBLIC_KEY_URL.toURI())) ){
            KeyGen gen = new KeyGen(2048);
            gen.writeToFile(Constants.PRIVATE_KEY_URL.toURI(), gen.getPrivateKey().getEncoded());
            gen.writeToFile(Constants.PUBLIC_KEY_URL.toURI(), gen.getPublicKey().getEncoded());
        }
    }

    private String encrypt(String plainText) throws Exception {
        return manager.encryptMessage(plainText);
    }

    private String decrypt(String cipherText) throws Exception {
        return manager.decryptMessage(cipherText);
    }

    private boolean licenseFileExists() {
        try {
            File licenseFile = new File(ClassLoader.getSystemClassLoader().getResource(LICENSE_PATH).getFile());
            return licenseFile.exists();
        } catch (NullPointerException e) {
            return false;
        }
    }

    private JsonObject generateJson(String computerId) {
        Date startDate = Calendar.getInstance().getTime();

        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.FEBRUARY, 1, 0,0,0);
        cal.setTimeInMillis(System.currentTimeMillis());
        Date expDate = cal.getTime();

        JsonObject obj = new JsonObject();
        obj.addProperty("name", "license-01");
        obj.addProperty("start-date", startDate.toString());
        obj.addProperty("expiry-date", expDate.toString());
        obj.addProperty("computerId", computerId);

        return obj;
    }

    private void validateLicense() throws Exception {
        boolean isValidLicense;
        byte[] keyBytes = Files.readAllBytes(Paths.get(ClassLoader.getSystemClassLoader().getResource(LICENSE_PATH).toURI()));
        String licenseAsJson = this.decrypt(new String(keyBytes));
        JsonObject licenseJsonObject = (JsonObject) new JsonParser().parse(licenseAsJson);
        printToConsole(licenseJsonObject.get("start-date").getAsString(), "License Start Date");
        printToConsole(licenseJsonObject.get("expiry-date").getAsString(), "License Expiry Date");
    }

    private void printToConsole(String message, String label) {
        System.out.println("=====================================================================================");
        System.out.println("@@ "+ label +" = " + message +" @@");
        System.out.println("=====================================================================================");
    }
}
