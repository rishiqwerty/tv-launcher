# Changelog — TV Launcher

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added - 2026-10-20 (Phase 3: Visual Polish, Specular Glassmorphism & Fast Search)
- **Specular Glassmorphic Polish:**
  - Implemented Apple TV-inspired specular top-down gradient sheen overlay (`0.24f` white highlight to transparent) on focused app tiles.
  - Added cyan glowing ambient drop shadow (`AccentCyan.copy(alpha = 0.7f)`) and high-contrast white border on focus.
  - Added rich radial gradient backdrop container for apps without wide 16:9 banners.
- **Shortcut Tiles Iconography:**
  - Added circular tinted icons for all quick access shortcuts (`Settings`, `Network`, `Audio`, `Manage Apps`, `Default Home`).
- **Live Search & Filter:**
  - Implemented remote-accessible `AppSearchBar` pill in `AllAppsScreen` supporting live query matching, virtual TV keyboard input, and instant clear.
- **Favorites Empty State:**
  - Designed an empty-state frosted card with star icon and direct `[ Browse All Apps ]` navigation button when no favorites are pinned.
- **Branding Renaming:**
  - Completely removed "Glass TV" name across the codebase: renamed application class to `TVLauncherApp`, theme to `TVLauncherTheme`, and updated all manifest/resource references.
- **Hardware Verification:**
  - Verified live on physical TCL Android 14 TV: tested focus specular glow, search filtering ("you" -> YouTube, YouTube Music), keyboard dismissal, and smooth D-pad remote navigation.

### Added - 2026-10-20 (Phase 2: Core Usability, All Apps Grid, Favorites Reordering & Branding Polish)
- **Branding Polish:**
  - Removed "Glass TV" name from UI header, resources, and settings to create a minimalist, unbranded launcher experience.
  - Added [`STATUS.md`](file:///Users/rishavsharma/Documents/Projects/tv_launcher/STATUS.md) to track completed, in-progress, and pending deliverables.
- **Top Navigation Pill Bar:**
  - Implemented `TopNavBar` featuring Apple TV-inspired frosted glass pills (`[ Home ]`, `[ All Apps ]`, `[ Settings ]`) with real-time clock.
  - Implemented `FocusRequester` auto-focus so the first favorite tile immediately receives focus on cold start.
- **All Apps Grid Screen:**
  - Implemented `AllAppsScreen` featuring an adaptive multi-column grid (`TvLazyVerticalGrid`) displaying all 17 installed applications with artwork.
- **Favorites Management & Action Dialog:**
  - Implemented `AppActionDialog` triggered by the remote `Menu` button or long-press.
  - Added support for adding/removing favorites and remote-driven reordering (`Move Left` / `Move Right`).
  - Verified live on physical TV: reordered favorites to move Apple TV right of JioHotstar and confirmed instant DataStore persistence.
- **Settings Screen:**
  - Implemented `SettingsScreen` with live switching for Themes (Dark, OLED Black, Subtle Gradient), Glass intensity, Reduced Motion toggle, and 12/24-hour clock formatting.
  - Added deep-link launcher shortcut to Android TV's `Settings.ACTION_HOME_SETTINGS`.
- **Dynamic Package Monitoring:**
  - Registered `PackageChangeReceiver` at runtime in `MainActivity` to automatically refresh application inventory on install/uninstall events.
- **Scaffolded Android TV Build Configuration:**
  - Configured Gradle 8.11.1 wrapper, AGP 8.5.1, Kotlin 2.0.21, and Compose for TV Material3 1.0.0.
  - Defined `AndroidManifest.xml` with `android.intent.category.HOME`, `DEFAULT`, and `LEANBACK_LAUNCHER`.
  - Added `android.permission.QUERY_ALL_PACKAGES` and fullscreen dark TV theme.
- **Data & Domain Architecture:**
  - Implemented `AppInfo`, `LauncherSettings`, and `SystemShortcut` models.
  - Created `AppRepository` querying installed TV and mobile applications using `PackageManager.MATCH_ALL` and `ResolveInfoFlags`.
  - Implemented `SettingsRepository` for local persistence using Jetpack DataStore Preferences.
  - Created `FavoritesReconciler` domain logic to safely manage favorites ordering and handle uninstalled packages.
  - Added unit test suite `FavoritesReconcilerTest` with 100% pass rate.
- **Remote-First UI Components:**
  - Implemented `GlassTVTheme` with frosted obsidian palette and 1080p safe dimensions.
  - Implemented `ClockWidget` displaying real-time clock and date with 10-second lifecycle-friendly tick.
  - Implemented `AppTile` with 16:9 TV banner support, 1.08x D-pad focus scaling, white border glow, and label rendering.
  - Implemented `ShortcutTile` for TV settings shortcuts (Settings, Network, Audio, Apps, Default Home).
  - Implemented `HomeScreen` and `HomeViewModel` with reactive unidirectional data flow (`StateFlow`).
- **Live Hardware Testing on TCL Smart TV (Android 14 / API 34):**
  - Installed and launched debug APK via wireless ADB.
  - Successfully discovered 17 installed leanback activities and rendered 16:9 banners.
  - Verified remote D-pad navigation (`DPAD_LEFT`, `DPAD_RIGHT`, `DPAD_UP`, `DPAD_DOWN`) and focus transitions.
  - Verified app launching via `DPAD_CENTER` (Apple TV launched and returned via `BACK` key).
- Completed Phase 0 Repository and Platform Feasibility Audit.
- Verified physical test environment: TCL Smart TV running Android 14 (API 34) at 1080p density (1920x1080 override, 3840x2160 native, 320 dpi).
- Connected and paired development environment to TV wirelessly over ADB.
- Audited Android TV / Google TV launcher requirements: verified `CATEGORY_HOME`, `CATEGORY_DEFAULT`, and `CATEGORY_LEANBACK_LAUNCHER` intent resolution.
- Verified `Settings.ACTION_HOME_SETTINGS` resolves directly to `com.android.permissioncontroller.role.ui.HomeSettingsActivity` for default launcher selection.
- Verified package visibility and discovered installed leanback TV packages (Netflix, YouTube, Prime Video, Apple TV, YouTube Music, TV Settings).
- Documented platform limitation: Google TV private content providers for recommendation / "Continue Watching" are unavailable via public Android APIs without private Google permissions.
- Created core documentation:
  - `ARCHITECTURE.md` (System layers, navigation hierarchy, package visibility, glass rendering, API constraints).
  - `RULES.md` (Platform restrictions, remote-first UX rules, D-pad requirements, testing standards).
  - `README.md` (Project overview, hardware requirements, build instructions, TV pairing and installation guide).
  - `CHANGELOG.md` (Project change history).
