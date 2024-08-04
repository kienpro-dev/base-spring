package com.example.projectbase.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class CryptionUtil {
    public static SecretKeySpec secretKeySpec;
    public static IvParameterSpec ivParameterSpec;
    // Tạo khóa bí mật từ mật khẩu
    public static void createSecretKey(final String myKey) throws Exception {
        byte[] key = myKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // Chỉ sử dụng 128 bit đầu tiên
        secretKeySpec = new SecretKeySpec(key, "AES");
    }

    // Tạo IV ngẫu nhiên
    public static void createIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        ivParameterSpec = new IvParameterSpec(iv);
    }

    // Mã hóa
    public static String encrypt(String strToEncrypt, String secretKey) throws Exception {
        try {
            createSecretKey(secretKey);
            createIv();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Giải mã
    public static String decrypt(String strToDecrypt, String secretKey) throws Exception {
        try {
            createSecretKey(secretKey);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));
            return new String(decrypted, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
