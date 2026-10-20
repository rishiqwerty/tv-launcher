# Architecture Documentation — TV Launcher

**Title:** TV Launcher  
**Platform:** Android TV / Google TV (API 28+ / Android 9.0+, Target SDK 34 / 35)  
**Primary Goal:** Lightweight, remote-first TV home launcher with Apple TV–inspired clarity, translucent glassmorphic surfaces, fast app access, and zero ad clutter.

---

## 1. High-Level Architecture Overview

TV Launcher follows modern Android Clean Architecture and Unidirectional Data Flow (UDF) patterns, keeping boundaries lightweight, modular, and testable without heavy dependency injection overhead.

```
┌────────────────────────────────────────────────────────────────────────┐
│                               UI Layer                                 │
│  - Jetpack Compose for TV (tv-material, tv-foundation)                 │
│  - Screens: HomeScreen, AllAppsScreen, SettingsScreen, FirstRunModal   │
│  - Components: GlassPanel, AppTile, TVFocusContainer, ClockWidget      │
│  - Theme: Glassmorphic tokens, focus states, responsive TV dimensions  │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │ StateFlow / Events
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             Domain Layer                               │
│  - Models: AppInfo, LauncherSettings, SystemShortcut                   │
│  - Logic: App sorting, favorites management & reconciliation           │
│  - Focus & remote interaction state machines                           │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                              Data Layer                                │
│  - AppRepository: PackageManager discovery, TV banner/icon caching     │
│  - SettingsRepository: DataStore Preferences for favorites & settings  │
│  - BroadcastReceivers: Package addition, removal, and updates          │
└────────────────────────────────────┬───────────────────────────────────┘
                                     │
                                     ▼
┌────────────────────────────────────────────────────────────────────────┐
│                       Platform / System Integration                    │
│  - Android PackageManager (QUERY_ALL_PACKAGES, LEANBACK_LAUNCHER)      │
│  - System Intents (Settings, Network, Audio, Date/Time, Home Picker)   │
│  - Lifecycle-aware Clock & ConnectivityManager                         │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Module & Package Structure

The project uses a single, well-structured module (`:app`) to maximize build speed and eliminate cross-module Gradle configuration overhead on TV builds:

```
com.glasstv.launcher/
├── GlassLauncherApp.kt                 # Application class
├── MainActivity.kt                     # Single-activity entry point (HOME, LEANBACK_LAUNCHER)
│
├── data/
│   ├── model/
│   │   ├── AppInfo.kt                  # App identity, label, banner/icon, TV flags
│   │   ├── LauncherSettings.kt         # Theme, blur level, density, motion, clock format
│   │   └── SystemShortcut.kt           # Settings shortcuts (Network, Audio, Display, etc.)
│   ├── repository/
│   │   ├── AppRepository.kt            # App discovery, caching, package change emission
│   │   └── SettingsRepository.kt       # Persistent preferences via Jetpack DataStore
│   └── receiver/
│       └── PackageChangeReceiver.kt    # Listens for PACKAGE_ADDED/REMOVED/REPLACED
│
├── domain/
│   ├── AppSortOrder.kt                 # Alphabetical, Recently Installed, Favorites First
│   └── FavoritesReconciler.kt          # Validates & reconciles saved favorites with installed apps
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt                    # Glass palette (dark obsidian, frosted translucent tints)
│   │   ├── Theme.kt                    # GlassLauncherTheme provider
│   │   ├── Type.kt                     # High-legibility TV typography tokens
│   │   └── Dimens.kt                   # 1080p baseline dimensions, safe margins, overscan
│   ├── components/
│   │   ├── GlassPanel.kt               # Translucent frosted surface with fallback
│   │   ├── AppTile.kt                  # Remote-focusable tile with banner/icon & scale focus
│   │   ├── ClockWidget.kt              # Lifecycle-aware time/date indicator
│   │   ├── ShortcutTile.kt             # Quick-access system settings tiles
│   │   └── GlassDialog.kt              # Accessible modal dialogs for settings & actions
│   ├── screens/
│   │   ├── HomeScreen.kt               # Status bar, Favorites row, Shortcuts row, App access
│   │   ├── AllAppsScreen.kt            # Categorized / searchable full application grid
│   │   └── SettingsScreen.kt           # Customization, density, appearance, and home app setup
│   └── viewmodel/
│       ├── HomeViewModel.kt            # UI state for Home screen
│       └── SettingsViewModel.kt        # UI state for preferences & configuration
```

---

## 3. UI and Remote Navigation Strategy

### D-Pad Remote First
- **No Touch Assumption:** Every component uses `Modifier.focusable()` and coordinates focus with D-pad navigation (`DPAD_UP`, `DPAD_DOWN`, `DPAD_LEFT`, `DPAD_RIGHT`, `DPAD_CENTER`/`ENTER`).
- **Unmistakable Focus State:** Focused tiles apply a 1.08x scale transformation, elevated drop shadow, and a crisp high-contrast border ring (`#FFFFFF` with subtle blur glow). Focus is never indicated by subtle color shift alone.
- **Overscan & TV Safe Areas:** Home layout respects a minimum 5% safe padding (80dp horizontal, 48dp vertical at 1080p) to avoid edge cutoff on legacy TV screens.
- **Back Button Stack:** Pressing `BACK` follows a predictable hierarchy:
  1. Dismiss open dialogs/actions menu.
  2. Return from `AllAppsScreen` or `SettingsScreen` to `HomeScreen`.
  3. On `HomeScreen`, scroll and refocus the first favorite app (never close the launcher or cause black screens).

---

## 4. App Discovery & Package Visibility Strategy

### Public Android APIs
- **Target SDK 34 / 35:** Android 11+ package visibility rules require `android.permission.QUERY_ALL_PACKAGES` in the manifest for home launcher apps.
- **Intent Queries:**
  1. Query `Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)` to retrieve first-class TV apps.
  2. Query `Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)` to capture sideloaded/mobile apps if enabled in settings.
- **Artwork Fallbacks:**
  - `activityInfo.loadBanner(packageManager)` (preferred 16:9 TV banner).
  - Fallback to `activityInfo.loadIcon(packageManager)` / `applicationInfo.loadIcon(packageManager)`.
  - Fallback to high-contrast letter avatar placeholder if icon loading fails.
- **Cache & Invalidation:** App discovery runs off the main thread (Dispatchers.IO) on startup and caches in-memory. It only refreshes when `PackageChangeReceiver` notifies of package modifications.

---

## 5. Persistence Approach & Data Flow

- **Jetpack DataStore Preferences:** Stores user preferences (favorite package names and order, theme mode, blur intensity, UI density, reduced motion).
- **Favorites Reconciliation:** When reading stored favorites, `FavoritesReconciler` compares stored package names against the currently installed package list. If an app is uninstalled, it is temporarily omitted from the active UI without discarding the stored preference, allowing it to gracefully restore if reinstalled.

---

## 6. Glassmorphism on TV Hardware

### Performance-First Glass Rendering
- **RenderEffect Blur (API 31+):** Utilizes hardware-accelerated `Modifier.blur()` where supported.
- **Translucent Fallback (API 28–30 or Low-Spec / Reduced Motion):** Provides a tailored layered tint (`rgba(20, 24, 34, 0.78)`) with a 1dp luminous edge highlight (`rgba(255, 255, 255, 0.14)`).
- **Restraint:** No nested blur surfaces. Glass background is rendered once behind active cards.

---

## 7. Platform Specifics & Known Limitations

| Feature | Supported via Public API | Implementation / Status |
|---|---|---|
| Home Launcher Intent | **Yes** (`ACTION_MAIN`, `CATEGORY_HOME`, `CATEGORY_DEFAULT`, `CATEGORY_LEANBACK_LAUNCHER`) | Fully supported. |
| Default Launcher Picker | **Yes** (`Settings.ACTION_HOME_SETTINGS`) | Launches `com.android.permissioncontroller` Home Settings. |
| TV App Discovery | **Yes** (`CATEGORY_LEANBACK_LAUNCHER` with `QUERY_ALL_PACKAGES`) | Fully supported. |
| TV Banners & Icons | **Yes** (`loadBanner()`, `loadIcon()`) | Fully supported with caching. |
| System Settings Shortcuts | **Yes** (`Settings.ACTION_SETTINGS`, `ACTION_WIRELESS_SETTINGS`, etc.) | Fully supported with fallbacks. |
| Google TV Continue Watching | **No** (Private Google API `launcherx`) | Omitted to prevent fabricated data. Documented limitation. |
| HDMI Input Switching | **Partial/Vendor-dependent** | Provided via TV settings intent; vendor-specific HDMI APIs omitted from MVP. |
