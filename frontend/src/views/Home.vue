<template>
  <div>
    <Logout />
    <SuggestedItems />
  </div>
</template>

<script>
import SuggestedItems from "@/components/SuggestedItems.vue";
import Logout from "@/components/Logout.vue";

export default {
  name: "Home",
  components: {
    Logout,
    SuggestedItems
  }
};
</script>
