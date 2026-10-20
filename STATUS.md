# Project Status & Roadmap — TV Launcher

Last updated: 2026-10-20

This document tracks all completed, in-progress, and pending deliverables across the project lifecycle according to `PROJECT_SPEC.md`.

---

## Overall Progress Summary

| Phase | Description | Status |
|---|---|---|
| **Phase 0** | Repository and Feasibility Audit | **Completed** |
| **Phase 1** | Functional Skeleton & TV Hardware Verification | **Completed** |
| **Phase 2** | Core Usability (All Apps, Favorites Reordering, Settings) | **Completed** |
| **Phase 3** | Visual Polish, Specular Glassmorphism & Fast Search | **Completed** |
| **Phase 4** | Release Readiness & Device Compatibility | **In Progress** |

---

## Detailed Deliverables

### Phase 0: Repository and Feasibility Audit
- [x] Inspect build configuration, toolchain, and Android TV SDKs.
- [x] Verify target Android TV / Google TV device compatibility (TCL Smart TV, Android 14 / API 34).
- [x] Pair and connect development environment wirelessly over ADB.
- [x] Audit public Android TV APIs (`CATEGORY_HOME`, `CATEGORY_LEANBACK_LAUNCHER`, `QUERY_ALL_PACKAGES`, `Settings.ACTION_HOME_SETTINGS`).
- [x] Document platform limitations (Google TV private recommendations & watch history).
- [x] Create core documentation (`ARCHITECTURE.md`, `RULES.md`, `CHANGELOG.md`, `README.md`).

---

### Phase 1: Functional Skeleton
- [x] Scaffold Gradle 8.11.1 wrapper, AGP 8.5.1, Kotlin 2.0.21, and Compose for TV Material3.
- [x] Configure `AndroidManifest.xml` for TV Home Launcher roles and leanback intents.
- [x] Implement `AppInfo` model supporting TV 16:9 banners, standard icons, and labels.
- [x] Implement `AppRepository` querying TV apps with `PackageManager.MATCH_ALL` asynchronously on `Dispatchers.IO`.
- [x] Implement `FavoritesReconciler` domain logic to safely manage favorites and handle uninstalled packages.
- [x] Implement unit test suite `FavoritesReconcilerTest` with 100% pass rate.
- [x] Implement `ClockWidget` displaying live 12h/24h time and full date.
- [x] Implement `AppTile` with 16:9 banner rendering, 1.08x D-pad focus scaling, illuminated white border outline, and drop glow.
- [x] Implement `ShortcutTile` for TV settings shortcuts (Settings, Network, Audio, Manage Apps, Default Home).
- [x] Implement `HomeScreen` and `HomeViewModel` with reactive `StateFlow`.
- [x] Live hardware verification on physical TCL Android 14 TV: app discovery, D-pad remote navigation, app launching, and Back key stability.

---

### Phase 2: Core Usability
- [x] **Branding Cleanup:**
  - [x] Removed "Glass TV" name from UI header, strings, themes, and settings, creating a clean, unbranded minimalist layout.
- [x] **All Apps Screen:**
  - [x] Dedicated full-screen adaptive grid (`AllAppsScreen`) displaying all installed applications with TV banners/icons.
  - [x] Remote D-pad navigation across rows and columns.
  - [x] D-pad navigation between Home and All Apps screen via top pill bar.
- [x] **Favorites Management:**
  - [x] Remote-first modal action dialog (`AppActionDialog`) triggered via remote `Menu` button or long-press.
  - [x] Toggle favorite state (Add to Favorites / Remove from Favorites).
  - [x] Reorder favorites with remote (Move Left / Move Right), tested live on TV hardware.
  - [x] Persistence of reordered favorites order in Jetpack DataStore.
- [x] **Settings Screen:**
  - [x] Dedicated in-app settings screen (`SettingsScreen`).
  - [x] Appearance settings: Theme (Dark / OLED Black / Subtle Gradient).
  - [x] Glass/blur intensity options (Off / Low / Medium / High).
  - [x] Reduced motion toggle.
  - [x] Clock format toggle (12-hour vs 24-hour, show/hide clock).
  - [x] Deep-link button to Android TV's system default Home App selection screen.
- [x] **Dynamic Package Change Monitoring:**
  - [x] Registered `PackageChangeReceiver` at runtime in `MainActivity` to update app list on installation or removal without requiring app restart.
- [x] **Remote Navigation Polish:**
  - [x] Auto-focus first favorite item on TV launch via `FocusRequester`.
  - [x] Deterministic Back button handling (subscreens return to Home, pressing Back on Home keeps launcher active).

---

### Phase 3: Visual Polish & Responsive Refinements
- [x] **State-of-the-Art Apple TV / visionOS Frosted Glass Overhaul:**
  - [x] **Liquid Glass Engine & Atmospheric Backdrop (`GlassBackdrop.kt`):**
    - Multi-layered luminous celestial orbs (Sapphire, Violet, Cyan, Emerald, Rose) providing vibrant depth and optical refraction behind translucent glass surfaces.
    - Hardware-accelerated blur (`Modifier.blur(48.dp)`) enabled on Android 12+ (tested live on Android 14 TV).
    - Deep space midnight gradient foundation (`#080C16` to `#04060B`).
    - Top frosted glass horizon specular catchlight across the screen.
  - [x] **Top Navigation Bar Floating Glass Island (`TopNavBar.kt`):**
    - Redesigned into a floating frosted glass segmented control capsule (`RoundedCornerShape(28.dp)`) with 1dp specular rim highlight.
    - Each segment equipped with modern icons (`Home`, `Apps`, `Settings`) and bold typography.
    - Active segment illuminated with frosted cyan/white glass gradient and soft ambient drop shadow.
    - Focused segment springs to 1.06x scale with high-luminance white specular highlight and pure white border.
  - [x] **Executive Glass Clock Pill:**
    - Floating frosted glass capsule with pulsing emerald live beacon, bold time, glass hairline divider, and date.
  - [x] **Modern Section Headers (`HomeScreen.kt`, `AllAppsScreen.kt`):**
    - Replaced dated dots with illuminated vertical accent beacons and frosted glass count badges (`[ 8 APPS ]`, `[ CONTROLS ]`, `[ 17 APPS ]`).
  - [x] **Frosted Glass Control Center Quick Access Tiles (`ShortcutTile.kt`):**
    - Redesigned into 186x76dp frosted glass control center cards with 20dp corners and 1dp specular bevel borders.
    - Glowing floating squircle icon badges with vivid saturation (Cyan for Settings, Indigo for Wi-Fi, Purple for Audio, Amber for Apps, Emerald for Home) and ambient colored halo bleed.
    - Dynamic focus state with 1.06x spring scale, top-down specular sheen sweep, pure white focus ring, and colored backlight glow.
  - [x] **Frosted Glass Settings Center (`SettingsScreen.kt`):**
    - Transformed full-width dark rectangles into modern frosted glass control cards.
    - Vivid squircle icon badges for each setting (Palette, Sparkles, Speed, Clock, Visibility, Home, Info).
    - Floating frosted glass status pill on the right showing active states (`[ DARK GLASS ]`, `[ MEDIUM ]`, `[ DYNAMIC ]`, `[ 12-HOUR ]`, `[ OPEN SETUP ]`).
    - Focus scaling, specular sheen reflections, and instant state toggle feedback.
  - [x] **Frosted Glass Search Pill (`AllAppsScreen.kt`):**
    - 280dp frosted glass capsule with cyan search icon, high-contrast text input, instant clear button, and glowing focus ring.
  - [x] **Physical TV Verification:**
    - Verified all 3 screens (`HomeScreen`, `AllAppsScreen`, `SettingsScreen`) live on physical TCL Android 14 TV (`192.168.1.10:5555`).

---

### Phase 4: Release Readiness & Optimization
- [ ] Device compatibility verification (overscan safe margins, 1080p and 4K display densities).
- [ ] Memory footprint and frame rendering audit (no jank / stable 60fps during D-pad navigation).
- [ ] Permission and offline privacy audit (verify no background network leaks).
- [ ] Release APK signing configuration.
- [ ] Final user documentation and QA checklist.

