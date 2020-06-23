<template>
  <span v-on:click="clicked = true">
    <strong>
      <big v-if="!clicked">Add to cart</big>
      <big v-else>Added</big>
    </strong>
  </span>
</template>

<script>
export default {
  name: "AddToCartButton",
  data: function(){
    return{
      clicked: false
    }
  }
};
</script>