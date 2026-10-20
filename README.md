# Glass TV Launcher

A minimalist, high-performance home launcher for Android TV and Google TV devices. Designed with an Apple TV–inspired aesthetic of translucent glassmorphism, calm typography, instant D-pad navigation, and zero advertisements or sponsored clutter.

---

## Key Features

- **Clean & Remote-First:** Effortless navigation using any standard Android TV D-pad remote.
- **Glassmorphic Aesthetic:** Frosted obsidian glass panels with subtle ambient lighting and high-contrast focus rings.
- **Fast App Discovery:** Instantly indexes all installed TV apps with their 16:9 banners or icons.
- **Customizable Favorites:** Add, remove, and reorder favorite apps right from your remote.
- **Quick Settings Shortcuts:** Instant access to TV settings, Wi-Fi/Network, Sound, and Display.
- **Privacy & Performance:** Zero network requests, zero tracking SDKs, no accounts, and ultra-fast cold start.

---

## Supported Devices & Platform Requirements

- **Target Platforms:** Android TV & Google TV (e.g., TCL, Sony, Philips, Chromecast with Google TV, Nvidia Shield).
- **Minimum SDK:** Android 9.0 (API 28)
- **Target / Compile SDK:** Android 14 / 15 (API 34 / 35)
- **Primary Screen Resolutions:** 1080p (1920x1080) and 4K (3840x2160) with overscan safe margins.

---

## Prerequisites

- **Java Development Kit (JDK):** JDK 17 (OpenJDK 17 recommended)
- **Android SDK:** Platforms 34 or 35, Build-Tools 34.0.0+
- **ADB:** Android Platform Tools 34+

---

## Building and Running

### 1. Build the Debug APK
```bash
./gradlew assembleDebug
```
The APK will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Connect to Android TV via ADB
Enable **Developer Options** and **Wireless Debugging** (or USB debugging) on your TV:
```bash
# Pair device if prompted (Android 11+)
adb pair <TV_IP_ADDRESS>:<PAIRING_PORT> <PAIRING_CODE>

# Connect ADB
adb connect <TV_IP_ADDRESS>:5555
```

### 3. Install the Launcher
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### 4. Launch Directly for Testing
```bash
adb shell am start -n com.glasstv.launcher/.MainActivity
```

---

## Setting as Default Home Launcher

1. **Via In-App Settings:**
   - Open Glass TV Launcher Settings -> **Launcher Setup** -> select **Set as Default Launcher**.
   - This opens Android's system `HomeSettingsActivity` where you can pick Glass TV Launcher.
2. **Via Android TV System Settings:**
   - Navigate to `Settings` -> `Apps` -> `Special app access` -> `Default apps` -> `Home app`.
   - Select **Glass TV Launcher**.
3. **Via ADB (Optional / Advanced Testing):**
   ```bash
   adb shell cmd package set-home-activity com.glasstv.launcher/.MainActivity
   ```

---

## Known Platform Limitations

- **Google TV Continue Watching Row:** Google TV's watch history and recommendation feeds are proprietary to Google's internal `launcherx` package and are not accessible to third-party launchers via public Android APIs. Per product specification, this data is not faked.
- **HDMI Input Switching:** Direct TV input switching requires vendor-specific manufacturer APIs which are non-standard across different TV brands. TV Settings shortcut is provided as a universal fallback.

---

## Documentation Links

- [Product Specification (`PROJECT_SPEC.md`)](PROJECT_SPEC.md)
- [Architecture & Design (`ARCHITECTURE.md`)](ARCHITECTURE.md)
- [Engineering Rules & Standards (`RULES.md`)](RULES.md)
- [Changelog (`CHANGELOG.md`)](CHANGELOG.md)
