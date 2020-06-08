<template>
  <div>
    <Logout/>
    <PastOrders/>
  </div>
</template>

<script>

import PastOrders from '@/components/PastOrders.vue'
import Logout from '@/components/Logout.vue'

export default {
  name: "Home",
   components: {
    PastOrders,
    Logout
  },
};
</script>
