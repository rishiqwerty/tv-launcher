# Changelog — Glass TV Launcher

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [Unreleased]

### Added - 2026-10-20 (Phase 1: Functional Skeleton & TV Hardware Verification)
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
