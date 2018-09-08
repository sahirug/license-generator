package com.home.test.keygenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@SuppressWarnings("unused")
public class KeyGen {
    private KeyPairGenerator generator;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final String ALGORITHM = "RSA";

    public KeyGen(int keyLength) throws Exception {
        this.generator = KeyPairGenerator.getInstance(ALGORITHM);
        this.generator.initialize(keyLength);
        createKeys();
    }

    private void createKeys() {
        this.pair = this.generator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void writeToFile(URI resource, byte[] key) {
        File file = new File(Paths.get(resource).toString());

        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(key);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isKeyExisting(URI resource) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(resource));
            if(keyBytes.length == 0){
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
