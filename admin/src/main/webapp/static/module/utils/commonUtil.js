import 'core-js/stable';
import 'regenerator-runtime/runtime';


export default class CommonUtil {

  static isJsonString(str) {
    try {
      const json = JSON.parse(str);
      return typeof json === 'object';
    } catch (e) {
      return false;
    }
  }

  static isNotJsonString(value) {
    return !this.isJsonString(value);
  }

  static randomString() {
    return Math.random().toString(36).substring(2);
  }
};
