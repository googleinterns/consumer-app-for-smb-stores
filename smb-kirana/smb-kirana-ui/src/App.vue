<template>
  <div id="app">
    <nav class="navbar is-info" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
    </div>
  </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  metaInfo: {
    title: "BharatKaNayaKirana",
    meta: [
      {name: "viewport"},
      {content: "width=device-width, initial-scale=1 maximum-scale=1.0, user-scalable=no"}
    ]
  }
}
</script>

<style>
  @import '../node_modules/bulma/css/bulma.css'
</style>
