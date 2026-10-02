package com.example.bengaliaicallassistant;

import android.content.SharedPreferences;
import android.telecom.Call;
import android.telecom.CallScreeningService;

import java.util.HashSet;
import java.util.Set;

public class IncomingCallScreeningService extends CallScreeningService {

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";
    private static final String TOTAL_CALLS_KEY = "total_calls";
    private static final String HISTORY_KEY = "call_history";
    private static final int MAX_HISTORY = 100;

    @Override
    public void onScreenCall(Call.Details callDetails) {

        String phoneNumber = "";

        if (callDetails.getHandle() != null) {
            phoneNumber = callDetails.getHandle().getSchemeSpecificPart();
        }

        phoneNumber = normalizeNumber(phoneNumber);

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // ===== TOTAL CALLS COUNTER =====

        int totalCalls = preferences.getInt(TOTAL_CALLS_KEY, 0);
        preferences.edit()
                .putInt(TOTAL_CALLS_KEY, totalCalls + 1)
                .apply();

        // ===== CALL HISTORY SAVE =====

        Set<String> history = new HashSet<>(
                preferences.getStringSet(HISTORY_KEY, new HashSet<>())
        );

        long now = System.currentTimeMillis();
        String entry = phoneNumber + "|" + now;
        history.add(entry);

        // Keep only latest MAX_HISTORY entries
        if (history.size() > MAX_HISTORY) {
            java.util.ArrayList<String> list = new java.util.ArrayList<>(history);
            java.util.Collections.sort(list, (a, b) -> {
                try {
                    long ta = Long.parseLong(a.split("\\|")[1]);
                    long tb = Long.parseLong(b.split("\\|")[1]);
                    return Long.compare(tb, ta);
                } catch (Exception e) {
                    return 0;
                }
            });
            java.util.ArrayList<String> trimmed =
                    new java.util.ArrayList<>(list.subList(0, MAX_HISTORY));
            history = new HashSet<>(trimmed);
        }

        preferences.edit()
                .putStringSet(HISTORY_KEY, history)
                .apply();

        // ===== TRUSTED CHECK =====

        Set<String> trustedNumbers =
                preferences.getStringSet(NUMBERS_KEY, new HashSet<>());

        boolean isTrusted = trustedNumbers.contains(phoneNumber);

        CallResponse response =
                new CallResponse.Builder()
                        .setDisallowCall(false)
                        .setRejectCall(false)
                        .setSilenceCall(false)
                        .setSkipNotification(false)
                        .setSkipCallLog(false)
                        .build();

        respondToCall(callDetails, response);
    }

    private String normalizeNumber(String number) {
        return number
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }
}
