package com.ollie.torch;

import android.content.Context;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;

/** Each power button long-press opens a session here: toggle the torch and vanish. */
public class TorchSessionService extends VoiceInteractionSessionService {

    @Override
    public VoiceInteractionSession onNewSession(Bundle args) {
        return new TorchSession(this);
    }

    private static class TorchSession extends VoiceInteractionSession {

        TorchSession(Context context) {
            super(context);
        }

        @Override
        public void onShow(Bundle args, int showFlags) {
            super.onShow(args, showFlags);
            Torch.toggle(getContext());
            hide();
        }
    }
}
