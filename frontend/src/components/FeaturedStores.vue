<template>
  <div>
    <div v-for="(merchant) in featuredStores" v-bind:key="merchant.mid" class="panel is-shadowless">
      <div v-on:click="moveToPaginatedProducts(merchant)" class="card">
        <div class="card" :style="{ 'background-color': merchant.bgcolor }">
          <div class="card-content is-paddingless">
            <div class="media is-paddingless is-marginless">
              <div class="media-left column is-marginless" style="padding-right: 0px">
                <strong>
                  <span class="is-family-primary" :style="{ 'color': merchant.textColor }">
                    <center>{{merchant.offers}}</center>
                  </span>
                </strong>
              </div>
              <div class="media-right column is-paddingless">
                <figure class="image" style="width: 170px">
                  <img :src="merchant.img" />
                </figure>
              </div>
            </div>
            <small style="color: #ab1cce">
              <b>
                Click for more products at
                <span
                  style="color: #16944a"
                >{{merchant.name}}</span>
              </b>
            </small>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "FeaturedStores",
  data: function() {
    return {
      featuredStores: [
        {
          name: "Mahendra Kirana Store",
          mid: "VxWQKTpSLLRlsuhQzdb3rapz5zv1",
          offers: "Munchy Cravings? Get Haldiram Products at 50% off!",
          img: require("../assets/haldiramMunchies.png"),
          bgcolor: "#7dd1da38",
          textColor: "#2618e6"
        },
        {
          name: "Shop For More",
          mid: "1NIEhX1qQfPZv7oUZnSZjJdCkzf1",
          offers:
            "Wash it! Clean it! Be 100% sure! Get Dettol products at 35% off!",
          img: require("../assets/DettolCollection.png"),
          bgcolor: "#f7fbbd",
          textColor: "#000000"
        }
      ]
    };
  },
  methods: {
    moveToPaginatedProducts(merchant) {
      this.$router.push({
        name: "PaginatedProducts",
        params: { merchant: merchant }
      });
    }
  }
};
</script>
