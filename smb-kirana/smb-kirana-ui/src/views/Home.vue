<template>
  <div>
    <PastOrders/>
  </div>
</template>

<script>
// @ is an alias to /src
import PastOrders from '@/components/PastOrders.vue'

export default {
  name: 'Home',
  components: {
    PastOrders
  }
}
</script>
