package com.ollie.torch;

import android.service.voice.VoiceInteractionService;
import android.service.voice.VoiceInteractionSession;

/** Kept running by the system while this app is the default assistant. */
public class TorchVoiceService extends VoiceInteractionService {

    @Override
    public void onReady() {
        super.onReady();
        // We never need the screen contents or a screenshot; skipping them makes the toggle faster.
        setDisabledShowContext(VoiceInteractionSession.SHOW_WITH_ASSIST
                | VoiceInteractionSession.SHOW_WITH_SCREENSHOT);
    }
}
