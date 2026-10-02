package com.example.bengaliaicallassistant;

import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class IncomingCallScreeningService extends CallScreeningService {

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";

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

        Set<String> trustedNumbers =
                preferences.getStringSet(
                        NUMBERS_KEY,
                        new HashSet<>()
                );

        boolean isTrusted =
                trustedNumbers.contains(phoneNumber);

        /*
         * Trusted number:
         * সরাসরি normal incoming call হিসেবে যেতে দেওয়া হবে।
         *
         * Non-trusted number:
         * আপাতত normal call হিসেবেই যেতে দেওয়া হচ্ছে।
         * পরের ধাপে AI handling system যোগ করা হবে।
         */

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
