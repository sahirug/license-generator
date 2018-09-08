package com.home.test.utils;

import javax.crypto.Cipher;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DecryptionManager {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private static final String ALGORITHM = "RSA";

    public DecryptionManager(URI privateKeyUri, URI publicKeyUri) throws Exception {
        this.privateKey = loadPrivateKeyFromFile(privateKeyUri);
        this.publicKey = loadPublicKeyFromFile(publicKeyUri);
    }

    private PrivateKey loadPrivateKeyFromFile(URI resource) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
        return factory.generatePrivate(spec);
    }

    private PublicKey loadPublicKeyFromFile(URI resource) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory factory = KeyFactory.getInstance(ALGORITHM);
        return factory.generatePublic(spec);
    }

    public String encryptMessage(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public String decryptMessage(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, this.publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }
}
