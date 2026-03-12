const config = {
  get() {
    return {
      url:
        process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + "/",
      name: process.env.VUE_APP_BASE_API,
    };
  },
  getProjectName() {
    return {
      projectName: "健身人群的膳食分享系统",
    };
  },
};
export default config;
