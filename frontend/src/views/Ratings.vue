<template>
  <div>
    <Logout />
    <div class="bd-lead">
      <div class="container">
        <div>
          <!-- {{merchantName}}, {{orderID}}, {{merchantID}} -->
          <textarea class="textarea" placeholder="Please enter your feedback"></textarea>

          <br />
          <center>
            <div>
              <table>
                <tr>
                  <td style="display: table-cell; vertical-align: middle;">
                    <strong>Timeliness</strong>
                  </td>
                  <td>
                    <span style="padding-left: 10px">
                      <StarRating
                        v-model="timelinessRating"
                        :inline="true"
                        inactive-color="#FFFFFF"
                        active-color="#008CBA"
                        v-bind:border-width="2"
                        :padding="1"
                        :glow="2"
                        :star-size="20"
                        border-color="	#008CBA"
                        glow-color="#008CBA"
                        :rounded-corners="true"
                        :show-rating="false"
                      ></StarRating>
                    </span>
                  </td>
                </tr>

                <tr>
                  <td style="display: table-cell; vertical-align: middle;">
                    <strong>Quality</strong>
                  </td>
                  <td>
                    <span style="padding-left: 10px">
                      <StarRating
                        v-model="qualityRating"
                        :inline="true"
                        inactive-color="#FFFFFF"
                        active-color="#008CBA"
                        :border-width="2"
                        :padding="1"
                        :glow="2"
                        :star-size="20"
                        border-color="	#008CBA"
                        glow-color="#008CBA"
                        :show-rating="false"
                        :rounded-corners="true"
                      ></StarRating>
                    </span>
                  </td>
                </tr>
              </table>
            </div>
          </center>
        </div>

        <div>
          <br />
          <p>
            <button v-on:click="submit()" class="button is-info" type="submit" value="Ask Me Later">
              <span>Ask Me Later</span>
            </button>&nbsp;
            <button class="button is-info" v-on:click="submit()" type="submit" value="Submit">
              <span>Submit</span>
            </button>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import StarRating from "vue-star-rating";
import Logout from "@/components/Logout.vue";
export default {
  name: "Ratings",
  props: ["merchantName", "merchantID", "orderID"],
  components: {
    StarRating,
    Logout
  },
  data() {
    return {
      timelinessRating: 0,
      qualityRating: 0
    };
  },
  methods: {
    submit() {
      this.$router.push("/home");
    }
  }
};
</script>

<style scoped>
.bd-lead {
  padding: 0.75rem;
}
</style>
