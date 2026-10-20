Project Specification --- Minimalist Google TV Launcher
Title: TV Launcher
Platform: Android TV / Google TV devices
Primary goal: Build a clean, lightweight, remote-first home launcher
with an Apple TV--inspired sense of polish, translucent glassmorphic
surfaces, and fast access to installed apps and useful TV features.
This project is inspired by the clarity, spacing, focus behavior, and
premium visual restraint of Apple TV. Do not copy Apple branding,
icons, proprietary assets, or exact screens. Create an original
Android TV experience.

1. Product Vision
Create an alternative home launcher for compatible Android TV / Google
TV devices. It should feel calm, premium, fast, and effortless to
navigate with a TV remote. The interface should prioritize installed
apps and a small number of genuinely useful shortcuts instead of ads,
clutter, or unnecessary recommendations.
Product principles
- Lightweight first: quick startup, low memory overhead, smooth
  transitions, minimal background work.
- Remote-first: every important action must be reachable with a
  D-pad remote; no touch interaction should be required.
- Clean and focused: strong hierarchy, generous spacing, limited
  visual noise, and restrained motion.
- Useful by default: installed apps, favorites,
  recent/continue-watching area where supported, inputs/settings
  shortcuts, and customization.
- Privacy-respecting: no account required for core launcher
  functionality; no analytics or network calls unless explicitly added
  and disclosed.
- Graceful compatibility: features must adapt to device, Android
  version, launcher role, manufacturer restrictions, and available
  APIs.
2. Scope and Platform Reality
The app is intended to function as a home launcher on supported Android
TV devices. Android TV and Google TV behavior differs by manufacturer
and OS version. The implementation must not assume that a third-party
launcher can replace every Google TV home-screen feature or access
private system data.

Before implementation, verify: - Supported Android API levels and target
SDK. - How the device lets the user select a default Home app. - Which
installed apps can be queried and launched under current
package-visibility rules. - Whether app categories, banners, launch
intents, and TV-specific metadata are available. - Whether usage
history, recent apps, HDMI inputs, system settings, or playback
continuation can be accessed through public APIs on the target device. -
Whether the launcher can be selected as the default Home app without ADB
or root on the intended devices.

Do not use root, hidden/private APIs, accessibility-service
workarounds, or device-owner privileges as a default solution. If a
requested feature is not supported by public APIs, document the
limitation and provide a safe fallback or omit it.
3. Target Users
- People who want a clean home screen without unnecessary content
  rows.
- Users who prefer a premium, minimalist interface.
- Users who want their favorite streaming apps one or two remote
  clicks away.
- Users with lower-powered TV hardware who value responsiveness.
- Users who want a customizable launcher without complicated setup.
4. Core MVP Features
4.1 Home screen
The home screen should include: - A subtle top status area with time and
date. - Optional network or system status indicators only where reliable
public APIs permit. - A prominent Favorites / Apps row with
installed TV apps. - A View all apps screen or expandable app
grid. - A small, configurable shortcuts area for supported system
settings and device functions. - An optional compact "Continue watching"
or "Recent" section only if a reliable, permitted data source exists. Do
not fake this data. - A settings entry point. - A tasteful
wallpaper/background treatment that preserves text readability.

Keep the default home screen short and uncluttered. Do not fill empty
space with ads, promotional content, or fabricated recommendations.
4.2 App library
- Discover launchable apps using supported Android package APIs.
- Display app icons and labels, with TV banners where available.
- Provide a predictable grid with consistent sizing and spacing.
- Support alphabetical sorting.
- Support favorites and manual ordering.
- Provide a simple search/filter interaction if it can be implemented
  efficiently with a TV remote.
- Launch apps using their supported launch intents.
- Handle missing icons, long labels, unavailable launch intents, and
  apps being uninstalled.
- Refresh app data when the package list changes, without repeatedly
  scanning on every render.
4.3 Favorites and ordering
- Add/remove an app from favorites.
- Reorder favorites using remote-friendly controls.
- Persist user choices locally.
- Provide sensible defaults on first launch.
- Never lose the saved order if an app is temporarily unavailable;
  reconcile it when the app returns.
4.4 Settings and personalization
Provide a focused settings screen with: - Theme: Dark (default), Light
if it remains legible on TV, and System where supported. - Background
style: solid, subtle gradient, or user-selectable wallpaper if
feasible. - Glass/transparency intensity: Low / Medium / High, with a
readability-first default. - UI density: Comfortable / Compact, if it
does not compromise remote navigation. - Animation intensity: Full /
Reduced. - Clock display: show/hide and 12/24-hour format where
appropriate. - Favorites row configuration. - App sorting and layout
options. - Reset launcher preferences with a confirmation step. - About,
version, privacy information, and open-source notices where applicable.
4.5 First-run experience
- Brief, skippable introduction.
- Explain how to set the app as the default Home launcher, using only
  supported Android flows.
- Ask only for permissions that are essential to a clearly explained
  feature.
- Do not require account creation.
- Provide a usable home screen even if optional permissions or
  settings are declined.
5. Visual Design Direction
5.1 Design language
Use an original design inspired by the following qualities: - Minimal,
cinematic, premium. - Dark surfaces and carefully controlled contrast. -
Soft translucent panels with blurred or simulated depth. - Rounded
corners, clean typography, and large app artwork. - Spacious layout
designed for viewing from across a room. - Motion that communicates
focus and navigation rather than decoration.
5.2 Glassmorphism
Glass effects should be restrained and optimized for TV hardware: - Use
translucent surfaces over a dark background. - Apply blur only when
performance allows; provide a low-cost fallback such as a translucent
color layer. - Use subtle edge highlights and soft shadows, not heavy
glowing borders. - Maintain sufficient contrast for text, icons, focus
rings, and selected states. - Avoid stacking multiple expensive blur
layers. - Never make focus state dependent on subtle transparency alone.
5.3 Layout guidance
- Design for landscape TV displays, starting with 16:9 at 1080p.
- Remain usable at 720p and 4K through responsive spacing and scalable
  components.
- Account for overscan and safe areas.
- Keep primary content away from screen edges.
- Use large app tiles with clear labels and a generous focus target.
- Prefer a small number of high-quality rows over a dense dashboard.
- Ensure the UI remains composed when there are few apps or many apps.
5.4 Focus and navigation states
Every interactive element must have clear states: - Default. -
Focused. - Selected/active. - Pressed. - Disabled. - Loading or
unavailable where relevant.

The focused item should be unmistakable at a glance. Use a tasteful
scale increase, elevation, outline, brightness change, or combination.
Avoid excessive bounce, abrupt zoom, and distracting glow.
5.5 Motion
- Use short, smooth transitions.
- Animate focus changes and screen transitions only where they improve
  orientation.
- Avoid long intro animations and animation-heavy backgrounds.
- Respect the Reduced Motion setting.
- Ensure animations never block input or make navigation feel delayed.
6. Remote-First Interaction Requirements
The remote is the primary input device.
- All screens must be navigable using Up, Down, Left, Right,
  Select/Enter, Back, and Menu where available.
- Focus must always be visible.
- Focus movement must be predictable, including at row/grid
  boundaries.
- Back must return to the previous logical screen or dismiss the
  current overlay.
- Opening and closing a screen must restore focus to the previous item
  where practical.
- Long app labels must not break layout or focus geometry.
- Avoid requiring a keyboard for common tasks.
- Search should support TV keyboard input and be optional if it
  complicates the MVP.
- Support remote key repeat without accidental double activation.
- Ensure dialogs and settings do not trap focus.
- Provide sensible behavior when a remote has no Menu button.
7. Information Architecture
Recommended initial structure:
1. Home
   - Clock/status
   - Favorites row
   - Optional supported recent/continue-watching row
   - Shortcuts
   - Settings access
2. Apps
   - All launchable apps
   - Sort/filter
   - Favorite management
3. Settings
   - Appearance
   - Home layout
   - App organization
   - Behavior and accessibility
   - About and diagnostics
4. Dialogs / overlays
   - App actions (Open, Favorite/Unfavorite, move/reorder where
     practical)
   - Confirm reset
   - First-run launcher setup guidance
   - Error and unavailable-feature messages
Keep navigation shallow. Avoid adding separate screens unless they solve
a real user problem.
8. Technical Direction
The coding agent should inspect the repository before choosing or
changing architecture. If the repository is empty, propose a minimal
maintainable structure and document the choice before implementation.
Platform
- Native Android application targeting Android TV.
- Kotlin is preferred for native Android platform integration.
- Use Jetpack Compose for TV only if the selected Compose/TV libraries
  are stable and compatible with the project's target SDK and device
  range. Otherwise, use the most reliable supported native UI
  approach.
- Do not introduce a large cross-platform framework solely for this
  launcher; platform integration and D-pad focus behavior are core
  requirements.
- Use Android TV-compatible manifest declarations and launcher intent
  filters as appropriate.
- Declare only necessary permissions.
- Follow Android package visibility rules and document any required
  <queries> declarations.
- Use public Android APIs wherever possible.
Architecture
Prefer a small, modular architecture with clear boundaries: - UI
layer: screens, reusable components, focus handling, themes. -
Domain layer: app listing, favorites, sorting, launcher settings,
supported shortcuts. - Data layer: installed-app repository, local
settings/preferences, app metadata cache. - Platform integration:
package manager, launch intents, system settings intents, clock and
supported status indicators.

Keep the architecture proportional to the app. Do not create unnecessary
abstractions, modules, dependency injection frameworks, or networking
layers without a concrete need.
Persistence
- Store favorites, ordering, and user preferences locally.
- Use a suitable Android persistence mechanism such as DataStore or
  Room only where justified.
- Do not store sensitive data.
- Reconcile persisted app references with installed packages safely.
- Include migration/default handling for future preference changes.
Performance
- Avoid unnecessary network access and background services.
- Cache app metadata and refresh it when package changes are detected.
- Avoid repeated package scans during UI recomposition.
- Use efficient image/icon loading and release resources when
  appropriate.
- Keep blur, shadows, and animation costs low on entry-level TV
  hardware.
- Do not run continuous polling for clock or status if a
  lifecycle-aware update is sufficient.
- Avoid startup blocking and main-thread disk operations.
9. App Discovery and System Integration
Implement app discovery using supported package APIs and current Android
package-visibility behavior.

Requirements: - Discover launchable TV apps and decide explicitly
whether to include phone-only apps that expose a launch intent. - Prefer
TV-appropriate app metadata and artwork when available. - Provide a
fallback icon and readable label. - Launch apps through public launch
intents. - Handle missing or invalid launch intents without crashing. -
Refresh app inventory after installation, removal, update, or
replacement events using supported mechanisms. - Do not assume every app
exposes a TV banner or category. - Do not assume Google TV
recommendation rows or watch history are available to third-party
launchers.

For system shortcuts, use supported settings intents where available. If
a manufacturer-specific screen is unavailable, show a clear unavailable
state rather than silently failing.
10. Privacy and Security
- Core launcher functionality must work offline.
- No account or cloud backend is required for MVP.
- No analytics, advertising SDKs, or tracking by default.
- Avoid requesting location, microphone, contacts, storage, or other
  unrelated permissions.
- Do not inspect private app data or attempt to bypass application
  sandboxing.
- Explain why each requested permission is needed.
- Keep preferences local unless a future, explicit sync feature is
  approved.
11. Accessibility and Readability
- High contrast between text and background.
- Large readable labels suitable for typical TV viewing distance.
- Focus indication must not rely on color alone.
- Respect system font scaling where practical.
- Support reduced motion.
- Avoid excessively thin type and low-opacity text.
- Ensure translucent surfaces remain legible over every supported
  background.
- Provide accessible labels for controls and meaningful semantics for
  screen readers where supported.
12. Error Handling and Edge Cases
Handle at least: - No launchable apps found. - App uninstalled after
being favorited. - App icon or banner missing. - App launch intent
unavailable or launch fails. - Very long app names. - Many installed
apps. - Display resolution or density changes. - Preferences missing or
corrupted. - Unsupported system shortcut. - User declines optional
permission. - Device restart and launcher cold start. - App update or
package replacement. - TV remote Back behavior on each screen and
dialog.

Errors should be brief, understandable, and recoverable. Never crash the
launcher because one app's metadata is malformed or unavailable.
13. Non-Functional Requirements and Acceptance Criteria
Performance
- Home screen appears promptly after cold start on the reference
  device.
- Scrolling and focus transitions remain smooth on the lowest-spec
  supported test device.
- No obvious jank from app scanning, icon loading, blur, or
  animations.
- No unnecessary network requests.
- No persistent background service unless a documented feature
  requires it.
Functionality
- User can set the app as Home launcher on a supported device using a
  documented flow.
- Home and app library are fully usable by remote.
- Installed launchable apps appear and can be opened.
- Favorites and ordering persist across restarts.
- Installed/uninstalled app changes are reflected reliably.
- Settings persist across restarts.
- Unsupported features fail gracefully and are documented.
Quality
- No crashes during standard navigation.
- Unit tests cover sorting, favorites reconciliation, and preference
  logic.
- UI/instrumentation tests cover critical navigation where feasible.
- A manual test checklist is included for real TV hardware.
- Build and run instructions are documented.
14. Testing Plan
Test on: - Android TV emulator at 1080p. - A lower-resolution or
lower-performance profile where available. - At least one physical
Android TV / Google TV device before claiming broad compatibility. -
Different app counts and missing metadata conditions. - Remote
navigation, Back handling, app launch, favorite editing, and restart
persistence. - Light/dark or transparency settings if included. -
Accessibility contrast and reduced-motion behavior.

Maintain a compatibility matrix that records: - Device/model. - Android
version. - Default launcher selection behavior. - App discovery
behavior. - Working shortcuts. - Known limitations.
15. Out of Scope for MVP
Do not implement these unless explicitly approved after the core
launcher is stable: - Custom video streaming or playback. - Replacing
Google TV's private recommendation/continue-watching APIs. - Ad blocking
inside third-party apps. - Root-only or privileged system
modifications. - Hidden API usage or accessibility-based remote-control
hacks. - Cloud accounts, sync, or a backend. - Complex widgets or
animated live backgrounds. - AI recommendations. - Built-in app store. -
Automatic HDMI input switching unless a supported public API is
verified. - Claims of compatibility with every Google TV device.
16. Suggested Delivery Phases
Phase 0 --- Repository and feasibility audit
- Inspect repository, build files, current architecture, dependencies,
  and existing conventions.
- Identify Android TV/Google TV target versions and default-launcher
  constraints.
- Verify app discovery and launcher-selection behavior on the intended
  target.
- Write a short feasibility note listing supported features,
  limitations, and decisions.
- Do not rewrite or replace existing project structure without a clear
  reason.
Phase 1 --- Functional skeleton
- App manifest and launcher entry points.
- Basic Home screen.
- App discovery and launch.
- Remote navigation and focus states.
- All-apps grid.
- Local preferences.
Phase 2 --- Core usability
- Favorites and ordering.
- Settings screen.
- First-run launcher setup.
- Package-change handling.
- Empty/error states.
- Automated tests for core logic.
Phase 3 --- Visual polish
- Glassmorphic surfaces and theme tokens.
- Focus animation and transitions.
- Responsive layout for 720p/1080p/4K.
- Performance optimization on physical hardware.
- Accessibility and reduced-motion pass.
Phase 4 --- Release readiness
- Compatibility matrix.
- Manual QA checklist.
- Privacy and permission audit.
- Release build configuration.
- Installation and default-launcher setup guide.
- Known limitations and troubleshooting notes.
17. Required Project Documentation
Create and maintain these files in the repository:
PROJECT_SPEC.md
This specification. Update it only when product scope or approved
requirements change.
ARCHITECTURE.md
Document: - High-level architecture and module boundaries. -
UI/navigation structure. - App discovery and package visibility
strategy. - Persistence approach and data flow. - Platform API
dependencies. - Key design decisions and trade-offs. - Known device/API
limitations.

Update it whenever architecture, major dependencies, platform
integrations, or data flow changes.
CHANGELOG.md
Maintain a dated, human-readable record of meaningful changes. Group
entries under Added, Changed, Fixed, Performance, and Notes where
appropriate. Do not rewrite history; append new entries for each
meaningful change.
RULES.md
Record coding conventions, UI/remote-navigation rules, dependency
policy, testing expectations, and restrictions on private APIs, root
requirements, permissions, analytics, and network access.
README.md
Include project overview, supported devices/API levels, prerequisites,
build/run instructions, installation instructions, setting the default
Home launcher, testing, and known limitations.
18. Coding Agent Working Rules
The coding agent must: 1. Read PROJECT_SPEC.md, RULES.md,
ARCHITECTURE.md, and CHANGELOG.md before making changes, when
present. 2. Inspect the existing repository and summarize its current
state before substantial implementation. 3. Work in small, reviewable
steps; do not attempt a giant one-shot rewrite. 4. Build after
meaningful changes and run relevant tests. 5. Fix regressions introduced
by its changes before proceeding. 6. Keep UI, focus behavior, and remote
navigation consistent across screens. 7. Avoid unnecessary dependencies
and explain any new dependency. 8. Never invent support for restricted
system features. Verify public API availability and document
limitations. 9. Update CHANGELOG.md for every meaningful code change.
10. Update ARCHITECTURE.md whenever architecture or major technical
decisions change. 11. Update PROJECT_SPEC.md only when approved
requirements or scope change. 12. Update README.md when setup, build,
installation, or user-facing behavior changes. 13. Add or update tests
for changed core behavior. 14. Never claim a build or test passed unless
it was actually run successfully. 15. At the end of each work session,
report: - What changed. - Files added/modified. - Build/test commands
run and their actual results. - Known issues or limitations. - The next
recommended task.
19. Definition of Done for MVP
The MVP is complete when: - The launcher can be installed and selected
as the Home app on the target test device, subject to documented device
restrictions. - The home screen is clean, fast, and visually coherent. -
App discovery, app launching, favorites, sorting, and settings work
reliably. - All critical interactions are accessible using a TV
remote. - Preferences persist after restart. - Missing metadata and app
installation changes are handled safely. - The interface remains
readable and focusable with glass effects enabled. - Core logic has
tests, a physical-device QA checklist exists, and documentation is
current. - README.md, PROJECT_SPEC.md, ARCHITECTURE.md,
CHANGELOG.md, and RULES.md accurately describe the implementation
and its known limitations.
Initial implementation instruction
Start with Phase 0 --- Repository and feasibility audit. Do not
immediately build the full UI. First inspect the repository, confirm the
target Android TV/Google TV compatibility assumptions, identify which
requested features are supported through public APIs, and report a
proposed implementation plan. Then proceed to Phase 1 in small, tested
increments.