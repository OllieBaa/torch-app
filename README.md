# Torch: hold the power button to turn on your torch

A tiny Android app that turns your phone's torch on and off when you **press and hold the power button**. It works on the lock screen, with the screen off, and when the phone is unlocked. You'll feel a short buzz each time, so you know it worked without looking.

Built because the built-in torch button on the lock screen is small and fiddly, especially with cold, wet or gloved hands.

Tested on a **Google Pixel 8a running Android 17**. It should work on other Pixels and most recent Android phones, but I haven't tested those.

## Before you install: one trade-off

Android doesn't let apps take over the power button directly. What the button *can* do is open your phone's "digital assistant" (normally Google Gemini). This app works by registering itself as the assistant. When the power button "opens the assistant", the app just flips the torch and gets out of the way.

**That means Gemini / Google Assistant stops being your assistant.** "Hey Google" and assistant gestures won't work. You can still open the Gemini app normally, and you can switch back at any time (see "Removing it" below).

## Privacy

The app has **no internet access** and collects nothing. The only things it's allowed to do are control the torch and vibrate the phone. When Android asks you to make it the assistant, the warning mentions reading what's on screen. This app turns that off and never uses it.

## Installing

1. **Download the app file.** On your phone, go to the [Releases page](../../releases) and download the latest `.apk` file.
2. **Open the downloaded file.** Android will warn you that it's from an unknown source, because it isn't from the Play Store. Tap **Settings**, allow your browser or file app to install apps, then go back and tap **Install**.
3. **Make it your assistant.** Open the **Torch** app and tap **Open default apps settings**, or go to **Settings > Apps > Default apps > Digital assistant app**. Choose **Torch** and accept the warning.
4. **Set the power button.** Go to **Settings > System > Gestures > Press & hold power button** and choose **Digital assistant**.
   - On non-Pixel phones this setting may have a different name or location. Search Settings for "power button" or "side key".
5. **Try it.** Press and hold the power button. The torch should come on with a buzz. Do it again to turn it off.

You can also open the Torch app and use its big on/off button.

## If it stops working

Phone updates sometimes reset settings. Check that:

1. **Settings > Apps > Default apps > Digital assistant app** is still **Torch**.
2. **Settings > System > Gestures > Press & hold power button** is still **Digital assistant**.
3. The camera app isn't open. Android won't let anything else use the torch while the camera is in use.

## Removing it

1. Go to **Settings > Apps > Default apps > Digital assistant app** and choose **Google** (or whichever assistant you had before).
2. Uninstall Torch as you would any other app.

## Known quirks

- If the power button gets held down in your pocket, the torch may come on. The buzz should let you know.
- It doesn't work while the camera app is open.

---

## For developers

Plain Java with no third-party libraries. Built from the command line with Gradle and the Android SDK.

- `Torch.java`: finds the rear flash, toggles it, and buzzes.
- `TorchVoiceService.java` and `TorchSessionService.java`: the "assistant". Each power-button invocation opens a session that toggles the torch and hides itself.
- `TorchRecognitionService.java`: an empty stub. Android requires assistants to provide a speech recogniser.
- `MainActivity.java`: the app screen.

### Building

Requires JDK 17 and the Android SDK (platform 35). On macOS with Homebrew:

```sh
brew install openjdk@17 gradle
brew install --cask android-commandlinetools android-platform-tools
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export ANDROID_HOME=/opt/homebrew/share/android-commandlinetools
sdkmanager "platforms;android-35" "build-tools;35.0.0"
echo "sdk.dir=$ANDROID_HOME" > local.properties

./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

To install over Wi-Fi: Developer options > Wireless debugging > Pair device with pairing code, then `adb pair <ip:port> <code>`.

`keystore/debug.keystore` is a standard Android development key. It's kept in the repo so that builds from any machine can update an existing install. It is not a secret.
