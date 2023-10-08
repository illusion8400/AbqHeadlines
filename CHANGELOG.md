# CHANGELOG.md AbqHeadlines

- v1.7.2 & ABQHeadlines for WearOS v1.0
- Updated icon animation
- Misc code updates
- Added TODOs for main app code cleanup; learned more while making watch app
- Ported to Kotlin for WearOS
  - Main phone app not required for watch app to operate
  - Behaves same as main app but layout and timing are a little different
  - Loads all titles/links when app is opened
  - Displays titles only, clicks will open link in default browser
  - In progress, all sources and front page in the future
  - App will probably crash if you don't have a browser installed on watch after clicking title. TODO: Fix

- v1.7.1
  - Added Animation for frontpage
  - Added subscribe and donate links to sourcenm
  - Misc code/links cleanup
    - Replaced error handling with android log

- v1.7
  - Added new source
  - Rearranged front page layout

- v1.6
  - Added new source
  - Added scroll

- v1.5.4
  - Added logo
  - Added separate no toast(popups) release. Better for WSA
  - Code cleanup:
    - Removed duplicate click listener
    - Removed duplicate execute on refresh

- v1.5.3
  - Added copy link on long press
  - Fix reload on rotation for large screens

- v1.5.2 
  - Added large screen rules for tablets to allow landscape mode
    - Screens over 600dp width will allow screen rotation.

- v1.5.1
  - Added more koat links

- v1.5
  - Fixed kob links
  - Added some colors

- v1.4.1
  - Added new font
  - Added loading toast after front_page
    - Removed refresh toast

- v1.4
  - Added Abqjournal

- v1.3.1
  - Added slide transition

- v1.3
  - Added front page and separated sites
  - Code cleanup lambda
  - Upgraded dependency

- v1.2.1
  - Added indicator for which website is being pulled
  - Added more headlines. Some of these don't have links. TODO: Fix
  
- v1.2 
  - Fixed refresh. More like a reload
  - Added CHANGELOG.md

- v1.1.1
    - Fixed bug

- v1.1
    - Reworked UI. Easier to look at.
    - Adjusted colors for Dark/Light mode.

- v1.0
    - Initial Release