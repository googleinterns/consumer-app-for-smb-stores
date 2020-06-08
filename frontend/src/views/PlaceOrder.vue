<template>
  <div>
    <Logout/>
    <SelectItems/>
  </div>
</template>

<script>
import SelectItems from '@/components/SelectItems.vue'
import Logout from '@/components/Logout.vue'

export default {
  name: 'PlaceOrder',
  components: {
    SelectItems,
    Logout
  }
}
</script>