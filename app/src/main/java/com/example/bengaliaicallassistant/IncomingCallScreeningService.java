package com.example.bengaliaicallassistant;

import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class IncomingCallScreeningService extends CallScreeningService {

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";
    private static final String TOTAL_CALLS_KEY = "total_calls";

    @Override
    public void onScreenCall(Call.Details callDetails) {

        String phoneNumber = "";

        if (callDetails.getHandle() != null) {
            phoneNumber =
                    callDetails.getHandle()
                            .getSchemeSpecificPart();
        }

        phoneNumber = normalizeNumber(phoneNumber);

        SharedPreferences preferences =
                getSharedPreferences(
                        PREFS_NAME,
                        MODE_PRIVATE
                );

        // =========================
        // TOTAL CALLS COUNTER
        // =========================

        int totalCalls = preferences.getInt(
                TOTAL_CALLS_KEY,
                0
        );

        preferences.edit()
                .putInt(
                        TOTAL_CALLS_KEY,
                        totalCalls + 1
                )
                .apply();

        // =========================
        // TRUSTED CHECK
        // =========================

        Set<String> trustedNumbers =
                preferences.getStringSet(
                        NUMBERS_KEY,
                        new HashSet<>()
                );

        boolean isTrusted =
                trustedNumbers.contains(phoneNumber);

        CallResponse response =
                new CallResponse.Builder()
                        .setDisallowCall(false)
                        .setRejectCall(false)
                        .setSilenceCall(false)
                        .setSkipNotification(false)
                        .setSkipCallLog(false)
                        .build();

        respondToCall(
                callDetails,
                response
        );
    }

    private String normalizeNumber(String number) {

        return number
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }
}
