<template>
  <div class="container">
    <section id="installBanner" class="banner">
      <button id="installBtn" class="button is-rounded is-success is-small">Install app</button>
    </section>
    <div id="app">
      <router-view />
    </div>
  </div>
</template>


<script>
let deferredPrompt;

window.addEventListener("beforeinstallprompt", event => {
  event.preventDefault();
  deferredPrompt = event;

  document.querySelector("#installBtn").addEventListener("click", event => {
    console.log(event);
    deferredPrompt.prompt();

    deferredPrompt.userChoice.then(choiceResult => {
      if (choiceResult.outcome === "accepted") {
        console.log("User accepted the A2HS prompt");
      } else {
        console.log("User dismissed the A2HS prompt");
      }
      deferredPrompt = null;
    });
  });
  document.querySelector("#installBanner").style.display = "flex";
});
export default {
  metaInfo: {
    title: "Bharat Ka Naya Kirana",
    meta: [
      { name: "viewport" },
      { content: "width=device-width, initial-scale=1.0" }
    ]
  }
};
</script>

<style>
@import "../node_modules/bulma/css/bulma.css";
.banner {
  align-content: center;
  display: none;
  justify-content: center;
  width: 100%;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
