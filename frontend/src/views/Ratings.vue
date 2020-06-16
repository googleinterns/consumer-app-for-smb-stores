<template>
<div >
    <Logout />
    <div class="bd-lead">
    <div class="container">
        <div>
    <h1><b>Hope you Enjoyed our Service :)</b></h1>
    <br>
    <br>
    <h2>Kindly rate us!</h2>
    <br>
    <p><i>Timeliness  </i><StarRating v-model="timelinessRating"  
            :inline=true
             inactive-color="#FFFFFF"  
             active-color="#FFD700"
             v-bind:border-width=5
             :padding=1
             :glow=10
             :star-size=40
             border-color="	#008CBA"
             glow-color="#FFD700"
             :rounded-corners=true
             :show-rating=false
             ></StarRating></p>
    <br>
    <p><i>Quality   </i> <StarRating v-model="qualityRating"  
            :inline=true
             inactive-color="#FFFFFF"  
             active-color="#FFD700"
             :border-width=5
             :padding=1
             :glow=10
             :star-size=40
             border-color="	#008CBA"
             glow-color="#FFD700"
             :show-rating=false
             :rounded-corners=true></StarRating></p>
    </div>
    <br>
    <div>
      <br>
        <p>
        <button
          v-on:click="submit()"
          class="button is-info"
          type="submit"
          value="Ask Me Later"
        >
          <span>Ask Me Later</span>
        </button>&nbsp;
        <button
          v-on:click="submit()"
          class="button is-info"
          type="submit"
          value="Submit"
        >
           <span >Submit 
                    </span>
        </button>
      </p>
    </div>
    </div>
</div>
</div>
</template>
<script>
import StarRating from 'vue-star-rating';
import Logout from "@/components/Logout.vue";
export default {
  name: 'Ratings',
  components: {
    StarRating,
    Logout
  },
 data(){
 return  {
  timelinessRating: 0,
  qualityRating: 0
};
},
methods: {
    submit() {
      this.$router.push("home");
    }
  },
}
</script>

<style scoped>
.container {
  margin-top: 50px;
}
.btn1 {
    position: absolute;
    left: 20%;
    /* padding: 16px 30px; */
     background-color:lightyellow; 
  color: black; 
  border: 2px solid #FFD700;
  cursor: pointer;
  display: inline-block;
 padding: 10px 13px;
 border-radius: 8px;
  font-size: 16px;
 font-weight: 550; 
 font-family:Georgia, 'Times New Roman', Times, serif;

}
.btn1:hover {
  background-color: #FFD700;
  color: white;
  box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24),0 17px 50px 0 rgba(0,0,0,0.19);
}
.btn2 {
    position:absolute;
    right: 20%;
       background-color: lightyellow; 
  color: black; 
  border: 2px solid #FFD700;
  cursor: pointer;
  display: inline-block;
  padding: 10px 13px; 
 border-radius: 8px;
 font-size: 16px;
 font-weight: 550; 
 font-family:Georgia, 'Times New Roman', Times, serif;
  
}
.btn2:hover {
  background-color: #FFD700;
  color: white;
  box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24),0 17px 50px 0 rgba(0,0,0,0.19);
}
p {
    background-color: white;
    font-size: 20px;
    font-family:Georgia, 'Times New Roman', Times, serif;
    color: black;
}
h1 {
  font-family:initial;
  font-size:xx-large;
  color:black;
}
h2{
  font-family: initial;
  font-size:x-large;
   color:black;
}
.bd-lead {
  padding: 0.75rem;
}

</style>
