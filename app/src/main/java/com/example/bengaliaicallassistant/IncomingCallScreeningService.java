package com.example.bengaliaicallassistant;

import android.telecom.Call;
import android.telecom.CallScreeningService;

public class IncomingCallScreeningService extends CallScreeningService {

    @Override
    public void onScreenCall(Call.Details callDetails) {

        String phoneNumber = "";

        if (callDetails.getHandle() != null) {
            phoneNumber = callDetails.getHandle().getSchemeSpecificPart();
        }

        // আপাতত কোনো call block বা reject করা হচ্ছে না।
        // শুধু incoming call শনাক্ত করা হচ্ছে।
        CallResponse response = new CallResponse.Builder()
                .setDisallowCall(false)
                .setRejectCall(false)
                .setSilenceCall(false)
                .setSkipNotification(false)
                .setSkipCallLog(false)
                .build();

        respondToCall(callDetails, response);
    }
}
