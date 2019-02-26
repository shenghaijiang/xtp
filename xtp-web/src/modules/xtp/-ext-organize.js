import {OrganizeAPI} from "./base";
import {XTP} from "../api";

Object.assign(OrganizeAPI, {
  getOrganizeCoding(params) {
    return new Promise((resolve) => {
      XTP.organization.getOrganizeCoding(params).then(({data, res}) => {
        resolve({ data, res });
      });
    });
  }
});

export {
  OrganizeAPI
};
