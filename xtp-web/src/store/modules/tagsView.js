const tagsView = {
  state: {
    visitedViews: [],
    cachedViews: [],
    keepAlive: true
  },
  mutations: {
    //获取是否缓存
    GET_KEEPALIVE(state, route) {
      const exitRoute = state.cachedViews.find((cachedItem) => cachedItem === route.name);
      state.keepAlive = !!exitRoute;
    },
    ADD_VISITED_VIEWS: (state, view) => {
      const item = state.visitedViews.some((v) => v.path === view.fullPath || v.path === view.path);
      if (item) {return;}
      if (view.path === "/") {return;}
      state.visitedViews.push({
        name: view.name,
        path: view.path,
        title: view.meta.title
      });
      state.cachedViews.push(view.name);
    },
    DEL_VISITED_VIEWS: (state, view) => {
      const item = state.visitedViews.find((v) => v.path === view.fullPath || v.path === view.path);
      if (item) {
        state.visitedViews.splice(state.visitedViews.indexOf(item), 1);
        state.cachedViews.splice(state.cachedViews.indexOf(item.name), 1);
      }
    },
    DEL_OTHERS_VIEWS: (state, view) => {
      const item = state.visitedViews.find((v) => v.path === view.fullPath || v.path === view.path);
      if (item) {
        state.visitedViews = [item];
        state.cachedViews = [item.name];
      }
    },
    DEL_ALL_VIEWS: (state) => {
      state.visitedViews = [];
      state.cachedViews = [];
    },
    CHANGE_CATCHD_VIEWS: (state, route) => {
    }
  },
  actions: {
    addVisitedViews({commit}, view) {
      commit("ADD_VISITED_VIEWS", view);
    },
    delVisitedViews({ commit, state }, view) {
      return new Promise((resolve) => {
        commit("DEL_VISITED_VIEWS", view);
        resolve([...state.visitedViews]);
      });
    },
    delOthersViews({ commit, state }, view) {
      return new Promise((resolve) => {
        commit("DEL_OTHERS_VIEWS", view);
        resolve([...state.visitedViews]);
      });
    },
    delAllViews({ commit, state }) {
      return new Promise((resolve) => {
        commit("DEL_ALL_VIEWS");
        resolve([...state.visitedViews]);
      });
    },
    cachedViewsChange({ commit, state }, route) {
      return new Promise((resolve) => {
        commit("CHANGE_CATCHD_VIEWS", route);
        resolve([...state.cachedViews]);
      });
    }
  }
};

export default tagsView;
