import { kebabCase } from "lodash";
import RowInput from "./row-input";
import LInput from "./xt-input/index.js";
import LTable from "./xt-table/index.js";
import LTableColumn from "./xt-table-column/index.js";
import VueTreeSelectValidate from "./vue-tree-select-validate/index";
import XtSearch from "./xt-search/search";
import XtInputNumber from "./xt-input-number/index";

const components = [
  RowInput,
  LInput,
	LTable,
  LTableColumn,
  VueTreeSelectValidate,
  XtSearch,
  XtInputNumber
];

const install = function (Vue) {
	/* istanbul ignore if */
  if (install.installed) {return;}
	components.map((component) => {
    Vue.component(component.name, component);
  });
};

/* istanbul ignore if */
if (typeof window !== "undefined" && window.Vue) {
  install(window.Vue);
}

export default {
	version: "1.0.2",
  install,
  RowInput,
  LInput,
	LTable,
  LTableColumn,
  VueTreeSelectValidate,
  XtSearch,
  XtInputNumber
};
