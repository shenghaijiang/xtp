import XtTableColumn from "../xt-table/table-column";

/* istanbul ignore next */
XtTableColumn.install = function(Vue) {
  Vue.component(XtTableColumn.name, XtTableColumn);
};

export default XtTableColumn;
