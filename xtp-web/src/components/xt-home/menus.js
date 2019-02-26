// side-nav menus
/* @freemark */
const basic = [
  { sort: 2, name: "原料管理", icon: "fa fa-th-large", path: "/basic/material", children: [], displayFlag: true },
  { sort: 2, name: "原料类目", icon: "fa fa-th-large", path: "/basic/material-category", children: [], displayFlag: true },
  { sort: 1, name: "原料类别", icon: "fa fa-th-large", path: "/basic/material-type", children: [], displayFlag: true },
  { sort: 4, name: "成品类目", icon: "fa fa-th-large", path: "/basic/product-category", children: [], displayFlag: true },
  { sort: 13, name: "成品管理", icon: "fa fa-th-large", path: "/basic/product", children: [], displayFlag: true },
  { sort: 20, name: "形状管理", icon: "fa fa-th-large", path: "/basic/shape", children: [], displayFlag: true }
];
const Material = [
  { sort: 2, name: "车间返料", icon: "fa fa-th-large", path: "/material/material-back", children: [], displayFlag: true },
  { sort: 5, name: "采购退货", icon: "fa fa-th-large", path: "/material/material-order-back", children: [], displayFlag: true },
  { sort: 7, name: "采购入库", icon: "fa fa-th-large", path: "/material/material-order-in", children: [], displayFlag: true },
  { sort: 9, name: "其他入库", icon: "fa fa-th-large", path: "/material/material-other-in", children: [], displayFlag: true },
  { sort: 11, name: "其他出库", icon: "fa fa-th-large", path: "/material/material-other-out", children: [], displayFlag: true },
  { sort: 13, name: "领料出库", icon: "fa fa-th-large", path: "/material/material-out", children: [], displayFlag: true },
  { sort: 15, name: "原料库存", icon: "fa fa-th-large", path: "/material/material-storage", children: [], displayFlag: true }
];
const Product = [
  { sort: 18, name: "生产返工", icon: "fa fa-th-large", path: "/product/product-back", children: [], displayFlag: true },
  { sort: 20, name: "产品批次管理", icon: "fa fa-th-large", path: "/product/product-batch-box", children: [], displayFlag: true },
  { sort: 22, name: "生产入库", icon: "fa fa-th-large", path: "/product/product-in", children: [], displayFlag: true },
  { sort: 24, name: "其他入库", icon: "fa fa-th-large", path: "/product/product-other-in", children: [], displayFlag: true },
  { sort: 26, name: "其他出库", icon: "fa fa-th-large", path: "/product/product-other-out", children: [], displayFlag: true },
  { sort: 29, name: "领料出库", icon: "fa fa-th-large", path: "/product/product-out", children: [], displayFlag: true },
  { sort: 31, name: "成品库存", icon: "fa fa-th-large", path: "/product/product-storage", children: [], displayFlag: true }
];

// 菜单栏
const menus = [

  /* @freemark */
  { sort: 1, name: "基础信息", path: "basic", icon: "fa fa-database", displayFlag: true, children: basic },
  { sort: 3, name: "原料信息", path: "material", icon: "fa fa-database", displayFlag: true, children: Material },
  { sort: 3, name: "产品信息", path: "product", icon: "fa fa-database", displayFlag: true, children: Product }
];

export default menus;
