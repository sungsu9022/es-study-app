import 'core-js/stable';
import 'regenerator-runtime/runtime';
import Vue from 'vue';
import 'semantic-ui-css/semantic.css';
import Home from '../module/view/home';
import CommonUtil from "../module/utils/commonUtil";

(async () => {
  console.log(`randomString : ${CommonUtil.randomString()}`);
  new Vue({
    el: '#app',
    components: {Home}
  });
})();
