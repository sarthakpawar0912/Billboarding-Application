package com.billboarding.Services.Payment;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class GenerateSignature {
    public static void main(String[] args) throws Exception {

        String orderId = "order_RnVDlNcltNhb8j";
        String paymentId = "pay_mock987654";
        String secret = "Umse7jXNrEHMVVw8gNMf2DDa";   // Paste ONLY locally

        String payload = orderId + "|" + paymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        String signature = Base64.getEncoder().encodeToString(
                sha256_HMAC.doFinal(payload.getBytes())
        );

        System.out.println("MOCK SIGNATURE = " + signature);
    }
}
