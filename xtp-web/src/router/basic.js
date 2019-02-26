import * as basics from "../views/basic/index";
import * as material from "../views/material/index";
import * as product from "../views/product/index";
import { compConfig } from "./utils";

const basicRoutes = compConfig(basics, basics.baseUrl);
const materialRoutes = compConfig(material, material.baseUrl);
const productRoutes = compConfig(product, product.baseUrl);

export default {
    basicRoutes,
    materialRoutes,
    productRoutes
};
