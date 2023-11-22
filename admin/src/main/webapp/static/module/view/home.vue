<template>
  <div>
    <div class="ui inverted menu">
      <div class="right menu">
        <div class="item" id="menuFrameReload" @click="reloadInnerFrame">
          <i class="refresh icon"></i> Reload Frame
        </div>
        <div class="item" id="menuOpenFrame" @click="openNewTabFrame">
          <i class="external icon"></i> Open New Window
        </div>
      </div>
    </div>
    <div class="ui grid">
      <div
        class="ui left vertical inverted menu"
        id="leftMenu"
        @click.prevent="clickItem($event)"
        style="height: 2000px"
      >
        <div class="ui blue center aligned header" style="margin-top: 10px">Manage Menu</div>
        <div class="item">
          <div class="header">menu1</div>
          <div class="menu">
            <a href="/menu1" class="item">sub menu 1</a>
            <a href="/menu2" class="item">sub menu 2</a>
          </div>
          <div class="header">menu2</div>
          <div class="menu">
            <a href="/menu3" class="item">sub menu 3</a>
            <a href="/menu4" class="item">sub menu 4</a>
          </div>
        </div>
      </div>
      <div class="ui thirteen wide column">
        <iframe
          id="innerIframe"
          width="100%"
          height="1000px"
          ref="innerFrame"
          frameborder="0"
          :src="innerFrameUrl"
        ></iframe>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'home',
  data() {
    return {
      innerFrameUrl: ''
    };
  },
  methods: {
    reloadInnerFrame() {
      const innerFrame = this.$refs.innerFrame;
      const frameSrc = innerFrame.getAttribute('src');

      if (!!frameSrc) {
        innerFrame.contentDocument.location.reload(true);
      }
    },
    openNewTabFrame() {
      if (this.innerFrameUrl) {
        window.open(this.innerFrameUrl);
      }
    },
    clickItem(e) {
      const target = e.target;
      const [hasLink, href] = this.hasLink(target);
      if (hasLink) {
        this.innerFrameUrl = href;
      }

      window.scrollTo(0, 0);
    },
    hasLink(dom) {
      if (!dom) {
        return [false, ''];
      }

      const href = dom.getAttribute('href');
      return [!!href, href];
    }
  }
};
</script>

<style scoped></style>
