package com.example.bengaliaicallassistant;

import android.content.SharedPreferences;
import android.telecom.Call;
import android.telecom.CallScreeningService;

import java.util.HashSet;
import java.util.Set;

public class IncomingCallScreeningService extends CallScreeningService {

    private static final String PREFS_NAME = "trusted_numbers";
    private static final String NUMBERS_KEY = "numbers";
    private static final String BLOCKED_KEY = "blocked_numbers";
    private static final String TOTAL_CALLS_KEY = "total_calls";
    private static final String BLOCKED_CALLS_KEY = "blocked_calls";
    private static final String HISTORY_KEY = "call_history";
    private static final int MAX_HISTORY = 100;

    @Override
    public void onScreenCall(Call.Details callDetails) {

        String phoneNumber = "";

        if (callDetails.getHandle() != null) {
            phoneNumber = callDetails.getHandle().getSchemeSpecificPart();
        }

        phoneNumber = normalizeNumber(phoneNumber);

        SharedPreferences prefs =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Total calls counter
        int totalCalls = prefs.getInt(TOTAL_CALLS_KEY, 0);
        prefs.edit().putInt(TOTAL_CALLS_KEY, totalCalls + 1).apply();

        // Call history save
        Set<String> history = new HashSet<>(
                prefs.getStringSet(HISTORY_KEY, new HashSet<>()));
        long now = System.currentTimeMillis();
        history.add(phoneNumber + "|" + now);
        if (history.size() > MAX_HISTORY) {
            java.util.ArrayList<String> list =
                    new java.util.ArrayList<>(history);
            java.util.Collections.sort(list, (a, b) -> {
                try {
                    long ta = Long.parseLong(a.split("\\|")[1]);
                    long tb = Long.parseLong(b.split("\\|")[1]);
                    return Long.compare(tb, ta);
                } catch (Exception e) {
                    return 0;
                }
            });
            history = new HashSet<>(list.subList(0, MAX_HISTORY));
        }
        prefs.edit().putStringSet(HISTORY_KEY, history).apply();

        // Blocked check
        Set<String> blockedNumbers =
                prefs.getStringSet(BLOCKED_KEY, new HashSet<>());
        boolean isBlocked = blockedNumbers.contains(phoneNumber);

        CallResponse.Builder builder = new CallResponse.Builder();

        if (isBlocked) {
            int blockedCalls = prefs.getInt(BLOCKED_CALLS_KEY, 0);
            prefs.edit()
                    .putInt(BLOCKED_CALLS_KEY, blockedCalls + 1)
                    .apply();

            builder.setDisallowCall(true)
                    .setRejectCall(true)
                    .setSilenceCall(true)
                    .setSkipNotification(true)
                    .setSkipCallLog(false);
        } else {
            builder.setDisallowCall(false)
                    .setRejectCall(false)
                    .setSilenceCall(false)
                    .setSkipNotification(false)
                    .setSkipCallLog(false);
        }

        respondToCall(callDetails, builder.build());
    }

    private String normalizeNumber(String number) {
        return number
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");
    }
}
