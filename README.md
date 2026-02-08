# Freeze-Dry Tray Calculator (Web App / PWA)

## Use it on your phone
This is a web app you can "Install" like an app (PWA). For that, it needs to be served from a website (not opened as a plain file).

### Easiest: host it on a free static host
Upload these files to a static host like:
- Netlify / Vercel / GitHub Pages / Cloudflare Pages

Then open the URL on your phone and choose:
- Chrome (Android): menu (⋮) -> "Add to Home screen" or "Install app"

### Local server option
If you have Python on your computer:
  python -m http.server 8080

Then on your phone (same Wi‑Fi):
  http://YOUR_COMPUTER_IP:8080/

## What it does
- 5 trays: start + end weights (grams)
- Total start, total end, water removed (g + %)
- Rehydration estimate: water to add back ~= water removed (1g ~= 1mL)
- Portioning:
  - dry grams per portion -> portions count
  - portions count -> dry per portion + water per portion

## Data
Inputs are saved locally on your device (localStorage). No accounts.
