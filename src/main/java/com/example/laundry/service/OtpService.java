package com.example.laundry.service;

import com.example.laundry.model.Otp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory OTP generator & verifier.
 * - TTL in minutes provided on generation.
 * - Stores OTPs in-memory; if you need persistence, persist them in storage.
 */
@Service
public class OtpService {

    private final Map<String, Otp> otpStore = new ConcurrentHashMap<>();
    private final Random rnd = new Random();

    public Otp generateOtpForOrder(String orderId, int ttlMinutes) {
        String otpVal = String.format("%04d", rnd.nextInt(10000));
        Otp otp = new Otp(otpVal, LocalDateTime.now().plusMinutes(ttlMinutes));
        otpStore.put(orderId, otp);
        return otp;
    }

    public boolean verifyOtp(String orderId, String otpAttempt) {
        Otp otp = otpStore.get(orderId);
        if (otp == null) return false;
        if (otp.isUsed()) return false;
        if (otp.isExpired()) return false;
        if (!otp.getOtpValue().equals(otpAttempt)) {
            otp.setAttempts(otp.getAttempts() + 1);
            return false;
        }
        otp.setUsed(true);
        return true;
    }

    public void invalidateOtp(String orderId) {
        otpStore.remove(orderId);
    }

    public Otp peek(String orderId) {
        return otpStore.get(orderId);
    }
}
