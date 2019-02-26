/**
 * Created by Dan on 2018/6/22.
 */
export const QrcodeTypeEnum = {
  PlanTask: {type: "PlanTask", prefix: "A", remark: "生产任务单二维码,前缀：A"},
  //prefix(B) + productBatchBox.productId + "@" + productBatchBox.id + "@" + this.planTask.id;
  ProductBatchBox: {type: "ProductBatchBox", prefix: "B", remark: "产品合格证,前缀：B"},
  //qrCode: prefix(C) + row.materialId + "@" + row.batch
  MaterialFeeding: {type: "MaterialFeeding", prefix: "C", remark: "原料(投料,原辅料退库),前缀：C"}
};
