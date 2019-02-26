<template>
    <div>
        <el-button type="primary" size="small" icon="plus" @click="addNewFormClick()">新增集团</el-button>
        <div class="xt-body">
            <div class="xt-left">
                <div class="xt-left-head">
                    组织结构
                </div>
                <div class="xt-left-list">
                    <el-tree :data="rootOriList" node-key="id" :props="defaultProps" style="height: 650px;overflow: auto;border:none;"
                             @node-click="handleNodeClick" :highlight-current="true" lazy :load="loadNode" @node-expand="nodeExpand" :default-expanded-keys="defaultExpandKeys"></el-tree>
                    <!--:expand-on-click-node="false"-->
                </div>
            </div>
            <div class="xt-right">
                <div class="xt-right-head">
                    结构详情
                </div>
                <!--已选择的数据展示-->
                <div v-show="(formData.id==0&&formData.parentId==0)?false:true">
                    <div class="xt-right-body">
                        <div class="xt-form-head">
                            修改{{formData.fullName}}
                        </div>
                        <div class="xt-form">
                            <el-form ref="formData" :model="formData" label-width="80px" :rules="rules" >
                                <el-row style="margin-top: 1rem">
                                    <el-col :span="6">
                                        <el-form-item label="编码" prop="code" style="margin-bottom: 1rem">
                                            <!--<xt-row-edit-item  :uid="formData.testId" :vueHub="vueHub" :itemVal.sync="formData.code" :rules="rules" prop="code">-->
                                                <el-input size="small" v-model="formData.code" placeholder="请输入编码" :maxlength="20" :disabled="true" ></el-input>
                                                <!--</xt-row-edit-item>-->
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-form-item label="名称" prop="fullName" style="margin-bottom: 1rem">
                                            <!--<xt-row-edit-item  :uid="formData.testId" :vueHub="vueHub" :itemVal.sync="formData.fullName" :rules="rules" prop="fullName">-->
                                            <el-input size="small" v-model="formData.fullName" placeholder="请输入名称" :maxlength="20" :disabled="!formData.isEdit"></el-input>
                                                <!--</xt-row-edit-item>-->
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-form-item label="组织级别"  style="margin-bottom: 1rem">
                                            <!--prop="level">-->
                                            <el-select v-model="formData.level" size="small" placeholder="请选择组织级别" @change="levelChange" style="width: 100%" :disabled="!formData.isEdit">
                                                <el-option :key="index" v-for="(item, index) in levelList" :label="item.key" :value="item.value"></el-option>
                                            </el-select>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-form-item label="地址" style="margin-bottom:1rem">
                                            <!--prop="address">-->
                                            <el-input size="small" v-model="formData.address" placeholder="请输入地址" :maxlength="50" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row >
                                    <el-col :span="6">
                                        <el-form-item label="传真" style="margin-bottom:1rem">  <!--style="margin-bottom: 5px"-->
                                            <!--prop="fax">-->
                                            <el-input size="small" v-model="formData.fax" placeholder="请输入传真" :maxlength="20" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-form-item label="办公电话" style="margin-bottom: 1rem">
                                            <!--prop="outerPhone">-->
                                            <el-input size="small" v-model="formData.outerPhone" placeholder="请输入办公电话" :maxlength="20" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="6">
                                        <el-form-item label="描述" style="margin-bottom: 1rem">
                                            <!--prop="description">-->
                                            <el-input size="small" type="textarea" :rows="1" v-model="formData.description" placeholder="请输入描述" :maxlength="30" :disabled="!formData.isEdit"></el-input>
                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-row>
                                    <el-col :span="4" :offset="10">
                                        <el-button type="primary" @click="submitFormItem" size="small" v-show="formData.isEdit">保存
                                        </el-button>
                                        <el-button type="info" v-show="!formData.isEdit" size="small" @click="editForm(formData)">
                                            修改
                                        </el-button>
                                    </el-col>
                                    <el-col :span="8">
                                        <el-button type="default" v-show="formData.isEdit" size="small"
                                                   @click="cancelForm(formData)">取消
                                        </el-button>
                                        <el-button type="danger" v-show="!formData.isEdit" size="small"
                                                   @click="deleteForm(formData)">删除
                                        </el-button>
                                    </el-col>
                                </el-row>
                            </el-form>
                        </div>
                    </div>
                    <div class="xt-right-list" v-if="formData.level!==5">
                        <el-button type="primary" size="small" icon="plus" @click="addFormChildrenClick()">新增
                        </el-button>
                        <div class="xt-list-head">
                            结构子列表
                        </div>
                        <div class="xt-list-body">
                            <el-table
                                    :data="formData.children"
                                    style="width: 100%" max-height="380"	>
                                <el-table-column
                                        prop="code"
                                        label="组织编码" minWidth="120">
                                    <template slot-scope="scope">
                                        <span class="xt-text-hidden" :title="scope.row.code">{{scope.row.code}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="fullName"
                                        label="组织名称" minWidth="120">
                                    <template slot-scope="scope">
                                        <span class="xt-text-hidden" :title="scope.row.fullName">{{scope.row.fullName}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="address"
                                        label="地址">
                                    <template slot-scope="scope">
                                        <span class="xt-text-hidden" :title="scope.row.address">{{scope.row.address}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="accountBook"
                                        label="账套">
                                    <template slot-scope="scope">
                                        <span class="xt-text-hidden" :title="scope.row.accountBook">{{scope.row.accountBook}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作" width="150">
                                    <template slot-scope="scope">
                                        <el-button size="small" type="warning" @click="editFormChildren(scope.row)">编辑
                                        </el-button>
                                        <el-button type="danger" size="small" @click="deleteFormChildren(scope.row)">
                                            删除
                                        </el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!--新增父级信息-->
        <el-dialog title="新增集团" :visible.sync="dialog.dialogFormVisible" :close-on-click-modal="false" :modal-append-to-body="false" width="50%">
            <el-form ref="formData" :model="formData" label-width="80px" :rules="rules">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="编码" prop="code">
                            <el-input size="small" v-model="formData.code" name="code" placeholder="请输入编码" :maxlength="20" ></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="fullName">
                            <el-input size="small" v-model="formData.fullName" placeholder="请输入名称" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="组织级别">
                            <!--prop="level">-->
                            <el-select v-model="formData.level" size="small" placeholder="请选择组织级别" @change="levelChange" style="width: 100%">
                                <el-option :key="index" v-for="(item, index) in levelList" :label="item.key" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址">
                            <!--prop="address">-->
                            <el-input size="small" v-model="formData.address" placeholder="请输入地址" :maxlength="50"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="传真">
                            <!--prop="fax">-->
                            <el-input size="small" v-model="formData.fax" placeholder="请输入传真" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="办公电话">
                            <!--prop="outerPhone">-->
                            <el-input size="small" v-model="formData.outerPhone" placeholder="请输入办公电话" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="描述" >
                            <el-input size="small" type="textarea" :rows="2" v-model="formData.description" placeholder="请输入描述" :maxlength="30"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="submitForm" :loading="loading.addLoading">提交</el-button>
            </div>
        </el-dialog>

        <!--新增子列表信息-->
        <el-dialog :title="pageShow.addTitle" :visible.sync="dialog.dialogFormChildrenVisible" :close-on-click-modal="false" :modal-append-to-body="false" width="50%">
            <el-form ref="formChildrenData" :model="formChildrenData" label-width="80px" :rules="rules">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="编码" prop="code">
                            <el-input size="small" v-model="formChildrenData.code" name="code" placeholder="请输入编码" :maxlength="20" :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="fullName">
                            <el-input size="small" v-model="formChildrenData.fullName" name="name" placeholder="请输入名称" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="组织级别">
                            <!--prop="level">-->
                            <el-select v-model="formChildrenData.level" size="small" placeholder="请选择组织级别" @change="levelChange" style="width: 100%" >
                                <el-option :key="index" v-for="(item, index) in levelList" :label="item.key" :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址">
                            <!--prop="address">-->
                            <el-input size="small" v-model="formChildrenData.address" placeholder="请输入地址" :maxlength="50"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="传真">
                            <!--prop="fax">-->
                            <el-input size="small" v-model="formChildrenData.fax" placeholder="请输入传真" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="办公电话">
                            <!--prop="outerPhone">-->
                            <el-input size="small" v-model="formChildrenData.outerPhone" placeholder="请输入办公电话" :maxlength="20"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="描述">
                            <el-input size="small" type="textarea" :rows="2" v-model="formChildrenData.description" placeholder="请输入描述" :maxlength="30"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialog.dialogFormChildrenVisible = false">取消</el-button>
                <el-button type="primary" @click="submitFormChildren" :loading="loading.addChildrenLoading">提交</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script type="text/ecmascript-6">
    import { MessageBox, vueHub} from "../../common/js/util";
    import XtRowEditItem from "../../components/row-edit-item";
    import {OrganizeAPI} from "../../modules/xtp";

    export default {
        components: {XtRowEditItem},
        data() {
            const checkCode = (rule, value, callback) => {
                const _this = this;
                const param = {code: value};
                this.getHadOrganize(param).then(function({isEmpty, id}) {
                    if (id === _this.formData.id) {callback();}
                    if (_this.dialog.dialogFormChildrenVisible && id === _this.formChildrenData.id) {
                        callback();
                    } else {
                        if (!isEmpty) {return callback(new Error("该编码已存在，请重新输入"));} else {callback();}
                    }
                });
            };
            return {
                data: [], //菜单获取的数据
                defaultProps: {
                    children: "children",
                    label: "fullName"
                }, //属性控件标题以及子列表数据绑定
                vueHub: vueHub,
                inputStyle: {position: "relative", top: "5px"},
                formData: {id: 0, fullName: "", code: "", parentId: 0, parentName: "", level: 1, address: "", accountBook: "", children: [], outerPhone: "", fax: "", postalcode: "", description: "", isEdit: false}, //选择的组织结构数据
                formChildrenData: {id: 0, fullName: "", code: "", parentId: 0, parentName: "", level: 1, address: "", accountBook: "", children: [], outerPhone: "", fax: "", postalcode: "", description: "", isEdit: false}, //选择的结构数据的子列表
                pageShow: {title: "", addTitle: "新增", addButtonShow: true}, //子列表修改的标题设置
                dialog: {dialogFormVisible: false, dialogFormChildrenVisible: false}, //模态框控制
                levelList: [],
                parentList: [],
                rootOriList: [],
                rules: {
                    code: [{required: true, message: "请输入编码", trigger: "blur"},
                        { validator: checkCode, trigger: "blur" }
                    ],
                    fullName: [{ required: true, message: "请输入名称", trigger: "blur" }],
                    upCode: [{required: true, message: " ", trigger: "change"}],
                    upFullName: [{ required: true, message: " ", trigger: "change" }]
//                    level:[{required: true, message: '请输入组织级别', trigger: 'blur' }],
//                    accountBook:[{required: true, message: '请输入账套', trigger: 'blur' }],
//                    address:[{required: true, message: '请输入地址', trigger: 'blur' }],
//                    outerPhone:[{required: true, message: '请输入办公电话', trigger: 'blur' }],
//                    fax:[{required: true, message: '请输入传真', trigger: 'blur' }]
                },
                loading: {loadingNode: false, addLoading: false, addChildrenLoading: false},
                copyFormData: {},
                defaultExpandKeys: []
            };
        },
        methods: {
            nodeExpand(row, node) {
                const exitItem = this.defaultExpandKeys.find((defaultItem) => defaultItem === row.id);
                if (!exitItem) {
                    this.defaultExpandKeys.push(row.id);
                }
            },
            editForm() {
                this.copyFormData = Object.assign({}, this.formData);
                this.formData.isEdit = true;
            },
            submitFormItem() {
                const _this = this;
                const para = JSON.parse(JSON.stringify(this.formData));
                this.$refs.formData.validate((valid) => {
                    if (valid) {
                        if (para.id === 0) {
                            _this.loading.addLoading = true;
                            _this.insertOrganize(para).then((res) => {
                                if (res.data.code === 1) {
                                    _this.formData.isEdit = false;
                                    _this.getOrganizeList({
                                        pageIndex: 1,
                                        pageSize: 99999,
                                        parentId: 0
                                    }).then(function (list) {
                                        _this.rootOriList = list;
                                        _this.dialog.dialogFormVisible = false;
                                        _this.loading.addLoading = false;
                                    });
                                }
                            });
                        } else {
                            _this.loading.addLoading = true;
                            _this.updateDatas(para).then((res) => {
                                if (res.data.code === 1) {
                                    _this.formData.isEdit = false;
                                    _this.getOrganizeList({
                                        pageIndex: 1,
                                        pageSize: 99999,
                                        parentId: 0
                                    }).then(function (list) {
                                        _this.rootOriList = list;
                                        _this.dialog.dialogFormVisible = false;
                                        _this.loading.addLoading = false;
                                    });
                                }
                            });
                        }

                    }
                });
            },
            cancelForm() {
                const _this = this;
                this.formData.isEdit = false;
                this.$refs.formData.resetFields();
                OrganizeAPI.getOrganize({id: this.formData.id}).then(function ({res}) {
                    const item = res.data.data;
                    item.isEdit = false;
                    item.level = item.level + "";
                    item.children = [];
                    _this.handleNodeClick(item);
                });
            },
            ///树形控件点击事件
            handleNodeClick(data) {
                const newFormData = this.formData;
                newFormData.isEdit = false;
                newFormData.id = data.id; newFormData.fullName = data.fullName;
                newFormData.code = data.code; newFormData.parentId = data.parentId;
                newFormData.fax = data.fax; newFormData.level = data.level;
                newFormData.address = data.address; newFormData.accountBook = data.accountBook;
                newFormData.outerPhone = data.outerPhone; newFormData.description = data.description;
                newFormData.testId = data.testId;
                OrganizeAPI.listOrganize({pageIndex: 1, pageSize: 99999, parentId: data.id}).then(({res}) => {
                    const list = res.data.data.data;
                    list.map(function (item) {
                        item.testId = Math.random();
                        item.level = item.level + "";
                    });
                    newFormData.children = list;
                });

            },

            /*新增集团事件*/
            addNewFormClick() {
                this.formData = {id: 0, testId: Math.random(), fullName: "", code: "", parentId: 0, parentCode: "", level: "1", address: "", accountBook: "", children: [], outerPhone: "", fax: "", postalcode: "", description: "", isEdit: false};
                this.dialog.dialogFormVisible = true;
            },

            /*新增子列表事件*/
            addFormChildrenClick(item) {
                const _this = this;
                const formData = this.formData;
                const pageShow = this.pageShow;
                OrganizeAPI.getOrganizeCoding({code: formData.code}).then(function ({res}) {
                    let code = "";
                    if (res.data.code === 1) {
                     code = res.data.data;
                    }
                    _this.formChildrenData = {id: 0, testId: Math.random(), fullName: "", code: code, parentId: formData.id, parentName: "", level: parseInt(formData.level, 10) + 1 + "", address: "", accountBook: "", children: [], outerPhone: "", fax: "", postalcode: "", description: "", isEdit: false};
                    switch (formData.level) {
                        case 1:
                            pageShow.addTitle = "新增集团";
                            break;
                        case 2:
                            pageShow.addTitle = "新增公司";
                            break;
                        case 3:
                            pageShow.addTitle = "新增工厂";
                            break;
                        case 4:
                            pageShow.addTitle = "新增部门";
                            break;
                        case 5:
                            pageShow.addTitle = "新增岗位";
                            break;
                        default:
                            break;
                    }
                    _this.dialog.dialogFormChildrenVisible = true;
                });
            },

            /*编辑子列表事件*/
            editFormChildren(item) {
                this.formChildrenData = Object.assign({}, item);
                const pageShow = this.pageShow;
                switch (this.formChildrenData.level) {
                    case 1:
                        pageShow.addTitle = "修改集团";
                        break;
                    case 2:
                        pageShow.addTitle = "修改公司";
                        break;
                    case 3:
                        pageShow.addTitle = "修改工厂";
                        break;
                    case 4:
                        pageShow.addTitle = "修改部门";
                        break;
                    case 5:
                        pageShow.addTitle = "修改岗位";
                        break;
                    default:
                        break;
                }
                if (this.$refs.formChildrenData) {this.$refs.formChildrenData.resetFields();}
                this.dialog.dialogFormChildrenVisible = true;
            },

            /*删除子列表数据事件*/
            deleteFormChildren(item) {
                const _this = this;
                this.$confirm("此操作将永久删除该组织, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    OrganizeAPI.listOrganize({pageIndex: 1, pageSize: 99999, parentId: item.id}).then(({res}) => {
                        const listLength = res.data.data.data.length ? res.data.data.data.length : 0;
                        if (listLength === 0) {
                            OrganizeAPI.deleteOrganize({id: item.id}).then(({res}) => {
                                if (res.data.code === 1) {
                                    _this.$message({
                                        message: "删除成功",
                                        type: "success"
                                    });
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: _this.formData.id}).then(function (list) {
                                        _this.formData.children = list;
                                    });
                                } else {
                                    _this.$message({
                                        message: "删除失败",
                                        type: "error"
                                    });
                                }
                            });
                        } else {
                            _this.$message({
                                message: "请先删除它的结构子列表",
                                type: "warning"
                            });
                        }
                    });
                });

            },

            /*删除选择的组织结构内容事件*/
            deleteForm(item) {
                const _this = this;
                this.$confirm("此操作将永久删除该组织, 是否继续?", "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    if (item.children.length === 0) {
                        OrganizeAPI.deleteOrganize({id: item.id}).then(({res}) => {
                            if (res.data.code === 1) {
                                _this.$message({
                                    message: "删除成功",
                                    type: "success"
                                });
                                _this.formData = {id: 0, fullName: "", code: "", parentId: 0, parentCode: "", level: "", address: "", accountBook: "", children: [], outerPhone: "", fax: "", postalcode: "", description: "", isEdit: false};
                            } else {
                                _this.$message({
                                    message: "删除失败",
                                    type: "error"
                                });
                            }
                            _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                                _this.rootOriList = list;
                            });
                        });
                    } else {
                        _this.$message({
                            message: "请先删除结构子列表",
                            type: "warning"
                        });
                    }
                });

            },

            /*新增/修改已选择的组织事件*/
            submitForm() {
                const _this = this;
                // const para = Object.assign({}, this.formData);
                const para = JSON.parse(JSON.stringify(this.formData));
                this.$refs.formData.validate((valid) => {
                    if (valid) {
                        if (para.id === 0) {
                            _this.loading.addLoading = true;
                            _this.insertOrganize(para).then((res) => {
                                if (res.data.code === 1) {
                                    _this.formData.isEdit = false;
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                                        _this.rootOriList = list;
                                        _this.dialog.dialogFormVisible = false;
                                        _this.loading.addLoading = false;
                                    });
                                }
                            });
                        } else {
                            _this.loading.addLoading = true;
                            _this.updateDatas(para).then((res) => {
                              _this.loading.addLoading = false;
                                if (res.data.code === 1) {
                                    _this.formData.isEdit = false;
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                                        _this.rootOriList = list;
                                        _this.dialog.dialogFormVisible = false;
                                        _this.loading.addLoading = false;
                                    });
                                }
                            });
                        }
                    }
                });
            },

            /*新增/修改子列表中的数据*/
            submitFormChildren() {
                const _this = this;
                // const para = Object.assign({}, this.formChildrenData);
                const para = JSON.parse(JSON.stringify(this.formChildrenData));
                this.$refs.formChildrenData.validate((valid) => {
                    if (valid) {
                        if (para.id === 0) {
                            _this.loading.addChildrenLoading = true;
                            _this.insertOrganize(para).then((res) => {
                                if (res.data.code === 1) {
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: _this.formData.id}).then(function (res) {
                                        _this.formData.children = res;
                                        _this.dialog.dialogFormChildrenVisible = false;
                                        _this.loading.addChildrenLoading = false;
                                    });
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                                        _this.rootOriList = list;
                                    });
                                }
                            });
                        } else {
                            _this.loading.addChildrenLoading = true;
                            _this.updateDatas(para).then((res) => {
                                if (res.data.code === 1) {
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: _this.formData.id}).then(function (res) {
                                        _this.formData.children = res;
                                        _this.dialog.dialogFormChildrenVisible = false;
                                        _this.loading.addChildrenLoading = false;
                                    });
                                    _this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                                        _this.rootOriList = list;
                                    });
                                }
                            });
                        }
                    }
                });
            },


            /*提交数据（修改数据）*/
            updateDatas(newForm) {
                const _this = this;
                const para = Object.assign({}, newForm);
                delete para.children;
                return new Promise(function (resolve, reject) {
                    OrganizeAPI.updateOrganize(para).then(({res}) => {
                      _this.loading.addLoading = false;
                        if (res.data.code === 1) {
                            _this.$message({
                                message: "更新成功",
                                type: "success"
                            });
                            resolve(res);
                        } else {
                            _this.$message({
                                message: res.data.msg || "更新失败",
                                type: "error"
                            });
                            resolve(res);
                        }
                    });
                });
            },
            //添加数据事件
            insertOrganize(newForm) {
                const _this = this;
                const para = Object.assign({}, newForm);
                return new Promise(function (resolve, reject) {
                    OrganizeAPI.insertOrganize(para).then(({res}) => {
                        if (res.data.code === 1) {
                            _this.$message({
                                message: "添加成功",
                                type: "success"
                            });
                            resolve(res);
                            _this.formData.isEdit = false;
                        } else {
                            _this.$message({
                                message: "添加失败",
                                type: "error"
                            });
                            resolve(res);
                        }
                    });
                });
            },

            /*模拟修改具体数据*/
            updateData(form, item) {
                form.id = item.id;
                form.fullName = item.fullName;
                form.code = item.code;
                form.parentId = item.parentId;
                form.parentCode = item.parentCode;
                form.level = item.level;
                form.address = item.address;
                form.accountBook = item.accountBook;
            },

            //树状展开
            loadNode(node, resolve) {
                if (node.level === 0) {
                    resolve([]);
                } else {
                    OrganizeAPI.listOrganize({pageIndex: 1, pageSize: 99999, parentId: node.data.id}).then(({res}) => {
                        if (res.data.code === 1) {
                            const data = res.data.data.data;
                            data.map(function (item) {
                                item.testId = Math.random();
                                item.level = item.level + "";
                            });
                            node.data.children = data;
                            resolve(data);
                        } else {
                            node.data.children = [];
                            resolve([]);
                        }
                    });
                }
            },
            //选择框组织级别变动
            levelChange(val) {
                const _this = this;
                OrganizeAPI.listOrganize({pageIndex: 1, pageSize: 99999, level: val - 1}).then(({res}) => {
                    _this.parentList = res.data.data.data;
                });
            },
            getLevelList() {
                const _this = this;
                DictAPI.listDict({pageIndex: 1, pageSize: 999999, type: "ORGANIZE"}).then(function({res}) {
                    if (res.data.code === 1) {
                        _this.levelList = res.data.data.data;
                    }
                });
            },

            /*获取组织列表*/
            getOrganizeList(params) {
                const _this = this;
                return new Promise(function (resolve, reject) {
                    OrganizeAPI.listOrganize(params).then(({res}) => {
                        const list = res.data.data.data;
                       list.map(function (item) {
                            item.level = item.level + "";
                           item.testId = Math.random();
                        });
                        resolve(list);
                    });
                });
            },
            getHadOrganize(params) {
                return new Promise(function(resolve, reject) {
                    let isEmpty = true, id = "";
                    OrganizeAPI.listOrganize(params).then(({res}) => {
                        if (res.data.data.count !== 0) {
                            isEmpty = false;
                            id = res.data.data.data[0].id;
                        }
                        resolve({isEmpty, id});
                    });
                });
            }
        },
        created() {
            const _this = this;
            this.getOrganizeList({pageIndex: 1, pageSize: 99999, parentId: 0}).then(function (list) {
                _this.rootOriList = list;
            });
            this.getLevelList();
        }
    };
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
    .xt-left {
        display: inline-block;
        float: left;
        width: 20%;
        height: 700px;
        background-color: #EEF1F6;
        border: 1px solid #DFE6EC;
    }

    .xt-left-head {
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        /*color:#ffffff;*/
        text-align: center;
        background-color: #EEF1F6;
    }

    .xt-left-list {
        height: 697px;
        overflow: auto;
    }

    .xt-right {
        display: inline-block;
        width: 78%;
        height: 700px;
        border: 1px solid #E4E8F1;
    }

    .xt-right-head {
        height: 3rem;
        font-weight: bolder;
        font-size: 16px;
        line-height: 3rem;
        /*color:#ffffff;*/
        text-align: center;
        background-color: #EEF1F6;
    }

    .xt-right-body {
        /*height: 285px;*/
        /*background-color: pink;*/
        padding: 0.3rem 0.2rem;
    }

    .xt-form-head {
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #DFE6EC;
        background-color: #EEF1F6;
        /*color: #ffffff;*/
    }

    .xt-form {
        padding: 0.3rem 0.4rem;
        border: 1px solid #DFE6EC;
    }

    .xt-right-list {
        padding: .1rem 0.2rem;
        /*height: 300px;*/
        /*background-color: yellow;*/
    }

    .xt-list-head {
        height: 2rem;
        line-height: 2rem;
        text-align: center;
        font-size: 14px;
        font-weight: bolder;
        border: 1px solid #DFE6EC;
        background-color: #EEF1F6;
        /*color: #ffffff;*/
    }
</style>
