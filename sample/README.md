# Running samples

To run sample project, you need a [Firebase project](https://console.firebase.google.com/). You can create a new one or reuse existing. 

## Firebase setup
1. Open **Authentication** module in **Build** category
2. Setup a new sign-in method, select **Google** provider
3. Make sure Google Sign-in is enabled

## Server setup

1. In your Firebase project, open **Project settings** and select **Service account tab**
2. Having selected **Firebase Admin SDK**, press **Generate new private key** at the bottom
3. Downloaded file, rename to **admin.json** and place it in _resources_ at `server/src/main/resources`
4. Run `main` function in `Application.kt`, server is running at port _8080_ 

## Web setup

1. In your Firebase project, open **Project settings** and select **General** tab (should be preselected)
2. Scroll down to "Your apps" section, press button to add new Web App
3. Register new Web App, opt out from hosting
4. Copy `firebaseConfig` content from displayed window
5. Paste the content into `web/admin.json` file, making sure it's valid JSON (double quotes around keys, remove trailing
   semicolon etc.)