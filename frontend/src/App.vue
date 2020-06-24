<template>
  <div class="container">
    <div id="app">
      <router-view />
    </div>
  </div>
</template>


<script>
import "./registerServiceWorker";
export default {
  metaInfo: {
    title: "Bharat Ka Naya Kirana",
    meta: [
      { name: "viewport" },
      { content: "width=device-width, initial-scale=1.0" }
    ]
  },
  created() {
    const prod = process.env.NODE_ENV === "production";
    const shouldSW = "serviceWorker" in navigator && prod;
    if (shouldSW) {
      let deferredPrompt;
      window.addEventListener("beforeinstallprompt", e => {
        // Prevent the mini-infobar from appearing on mobile
        e.preventDefault();
        console.log(e);
        // Stash the event so it can be triggered later.
        deferredPrompt = e;
        // Update UI notify the user they can install the PWA
        showInstallPromotion();
        buttonInstall.addEventListener("click", e => {
          // Hide the app provided install promotion
          hideMyInstallPromotion();
          // Show the install prompt
          deferredPrompt.prompt();
          // Wait for the user to respond to the prompt
          deferredPrompt.userChoice.then(choiceResult => {
            if (choiceResult.outcome === "accepted") {
              console.log("User accepted the install prompt");
            } else {
              console.log("User dismissed the install prompt");
            }
          });
        });
      });
    }
  }
};
</script>

<style>
@import "../node_modules/bulma/css/bulma.css";
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
