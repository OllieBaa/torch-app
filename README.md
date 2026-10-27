# Torch

A tiny personal Android app (Pixel 8a) that toggles the torch when you **long-press the power button**, including from the lock screen, with a short buzz to confirm.

## How it works

Android doesn't let apps take over the power button. But the long-press opens whatever app is set as the **default digital assistant**. This app registers itself as an assistant, and when "invoked" it just toggles the torch and disappears.

Side effect: Gemini / "Hey Google" is no longer your assistant. The Gemini app still opens normally.

## If it stops working (e.g. after an Android update)

Check these first. Updates sometimes reset them:

1. **Settings > Apps > Default apps > Digital assistant app** must be **Torch**.
2. **Settings > System > Gestures > Press & hold power button** must be **Digital assistant**.
3. Is the Torch app still installed? If not, reinstall it (see below).

If the settings are right and it still doesn't work, open this folder in Claude Code and say: *"The Torch app has stopped working on the power button after an Android update. Read the README and help me fix it."*

## Installing on the phone without a Mac

Keep a copy of `app-debug.apk` (attached to the GitHub release, or in `app/build/outputs/apk/debug/` after building). Copy it to the phone (e.g. via Google Drive), tap it, and allow "install unknown apps" when asked.

## Building and installing from the Mac

Needs (via Homebrew): `openjdk@17`, `gradle`, cask `android-commandlinetools`, cask `android-platform-tools`.

```sh
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
echo "sdk.dir=/opt/homebrew/share/android-commandlinetools" > local.properties
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Connecting the phone wirelessly: Developer options > Wireless debugging > Pair device with pairing code, then `adb pair <ip:port> <code>`. Turn wireless debugging off afterwards.

`keystore/debug.keystore` is the signing key. Keep it: the phone only accepts updates signed with the same key. (It's a standard development key, not a secret.)

## Files

- `Torch.java`: switches the rear flash on/off and buzzes.
- `TorchVoiceService.java`, `TorchSessionService.java`: the "assistant" that the power button triggers.
- `TorchRecognitionService.java`: empty stub Android requires for assistants.
- `MainActivity.java`: the app screen (big toggle button, setup shortcut).
