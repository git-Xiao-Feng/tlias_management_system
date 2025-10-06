<script setup>
import { ElMessage , ElMessageBox} from 'element-plus'
import { ref , onMounted , watch} from 'vue'
import { getStaffList,getStaffById,addStaff,updateStaff, DeleteStaff } from '@/api/staff3'
import { Delete , Plus } from '@element-plus/icons-vue' 
import { getall as queryAllDeptApi } from '@/api/dept3'

// 查询表单数据
const formInline = ref({
  name: '',
  gender: '',
  begin: '',
  end: '',
  hiredate: [],
})

// 监听hiredate的变化，拆分赋值给begin和end
watch(() => formInline.value.hiredate, (newValue) => {
  if (Array.isArray(newValue) && newValue.length === 2) {
    formInline.value.begin = newValue[0]
    formInline.value.end = newValue[1]
  }else{
    formInline.value.begin = ''
    formInline.value.end = ''
  }
})


// 钩子函数，页面加载完成后，获取员工列表
onMounted( async () => {
  onSubmit()
   //加载所有部门数据
  const result = await queryAllDeptApi();
  if(result.code){
    deptList.value = result.data
  }
})

// 表单查询按钮
const onSubmit = async ()  => {
    const res = await getStaffList(
        formInline.value.name,
        formInline.value.gender,
        formInline.value.begin,
        formInline.value.end,
        currentPage.value,
        pageSize.value)
    console.log(res)
    tableData.value = res.data.rows
    total.value = res.data.total

  ElMessage.info('submit!')
}

// const value1 = ref('')


// 表单重置查询按钮
const resetForm = () => {
    formInline.value = {
        name: '',
        gender: '',
        begin: '',
        end: '',
        hiredate: [],
    }
    onSubmit()
}

const tableData = ref([])           //员工数据列表


// 删除按钮
const handleDelete = (ids) => {
    ElMessageBox.confirm(
    
    'Are you sure to delete this employee?',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
      const result = await DeleteStaff(ids);
      if(result.code){
        onSubmit();
        ElMessage.success('删除成功');
      }else{
        ElMessage.error(result.msg);
      } 
    }catch (error) {
        ElMessage.error('删除失败: ' + (error.message || '未知错误'))
      }
      
      
    })
    .catch(() => {
      ElMessage.info('Delete canceled')
    })
}

const currentPage = ref(1)   // 当前页码
const pageSize = ref(10)   // 每页条数
const total = ref(0)   // 总条数



// 页数变化监听
const handleCurrentChange = (val) => {
  currentPage.value = val
  onSubmit()
}

// 每页条数变化监听
const handleSizeChange = (val) => {
  pageSize.value = val
  onSubmit()
}

const dialogFormVisible = ref(false)   // 员工信息对话框显示隐藏

// 新增员工按钮
const addStaffOne = () => {
    dialogFormVisible.value = true
    dialogFormTitle.value = '新增员工'
    clearstaffInfo()
    if(staffFormRef.value){
      staffFormRef.value.resetFields()
  }
}


const staffFormRef = ref()   // 员工信息表单规则引用
// 对话框员工信息数据
const staffInfo = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  deptId: '',
  entryDate: '',
  image: '',
  exprList: []
})


// 表单校验规则
const staffFormRules = {
  username: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度应在2-20个字符之间', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 2, max: 10, message: '姓名长度应在2-10个字符之间', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '性别不能为空', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}


// 清空员工信息
const clearstaffInfo = () => { 
  staffInfo.value = {
    username: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    deptId: '',
    entryDate: '',
    image: '',
    exprList: []
  }

  imageUrl.value = ''
  tempUploadFile.value = null
}


// 添加工作经历
const addWorkExperience = () => { 
    staffInfo.value.exprList.push({
      company: '',
      job: '',
      begin: '',
      end: '',
      workDuring: []
    })
}

// 监听workDuring的变化，拆分赋值给begin和end
watch(() => staffInfo.value.exprList, (newValue) => {
  if (staffInfo.value.exprList && staffInfo.value.exprList.length > 0) {
    staffInfo.value.exprList.forEach( expr => {
      expr.begin = expr.workDuring[0]
      expr.end = expr.workDuring[1]
    })
  }
},{deep: true})


// 删除工作经历
const deleteWorkExperience = (index) => { 
  if(index >= 0 && index < staffInfo.value.exprList.length){
  staffInfo.value.exprList.splice(index,1)
  }
  
}

//职位列表数据
const jobs = ref([{ name: '班主任', value: 1 },{ name: '讲师', value: 2 },{ name: '学工主管', value: 3 },{ name: '教研主管', value: 4 },{ name: '咨询师', value: 5 },{ name: '其他', value: 6 }])
//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])
//部门列表数据
const deptList = ref([])


const dialogFormTitle = ref('')   // 员工信息对话框标题


// 员工信息表单数据保存按钮
const saveForm = async () => {
  // 确保表单引用存在
  if (!staffFormRef.value) {
    console.error('表单引用不存在')
    return
  }
   try {
    // 先进行表单校验
    const isValid = await new Promise((resolve) => {
      staffFormRef.value.validate((valid) => {
        resolve(valid)
      })
    })
  
    if (!isValid) {
      ElMessage.error('请填写完整且正确的表单信息')
      return
    }
    // 如果有临时上传文件，则先上传图片
    if (tempUploadFile.value) {
      // 保存当前的图片地址（用于判断是否上传成功）
      const originalImageUrl = staffInfo.value.image;
      
      // 提交上传
      uploadRef.value.submit();
      
      // 使用Promise等待图片上传完成
      await new Promise((resolve, reject) => {
        // 设置轮询检查图片是否上传成功
        const checkInterval = setInterval(() => {
          // 如果图片地址发生变化，说明上传成功
          if (staffInfo.value.image !== originalImageUrl) {
            clearInterval(checkInterval);
            resolve();
          }
        }, 20); // 每20ms检查一次
        
        // 添加超时处理
        setTimeout(() => {
          clearInterval(checkInterval);
          reject(new Error('图片上传超时'));
        }, 30000);
      });
    }
        
        

      let result;
      if(staffInfo.value.id){ //修改
        result = await updateStaff(staffInfo.value);
      }else { //新增
        result = await addStaff(staffInfo.value);
      }
      
      if(result.code) {//成功
        ElMessage.success('保存成功');
        dialogFormVisible.value = false;
        // 清空临时文件引用
        tempUploadFile.value = null
        onSubmit();
      }else { //失败了
        ElMessage.error(result.msg);
      }
      } catch (error) {
    console.error('保存失败:', error);
    ElMessage.error('保存过程中发生错误');
    }
    
  }

//  添加取消保存的函数，确保清空相关状态
const cancelSave = () => {
  dialogFormVisible.value = false
  clearstaffInfo()
  ElMessage.info('已取消保存')
}


// 编辑员工按钮
const handleEdit = async (id) => {
  if(staffFormRef.value){
      staffFormRef.value.resetFields()
  }
    const result = await getStaffById(id);
  if(result.code){
    dialogFormVisible.value = true;
    dialogFormTitle.value = '修改员工';
    staffInfo.value = result.data;

    //对工作经历进行处理
    let exprList = staffInfo.value.exprList;
    if(exprList && exprList.length > 0){
      exprList.forEach((expr) => {
        expr.workDuring = [expr.begin, expr.end];
      })
    }
  }
}


const imageUrl = ref('')   // 图片预览
const tempUploadFile = ref(null)   // 临时上传文件

const uploadRef = ref(null);   // 上传组件实例（用于手动触发上传）

// 文件选择/变化时触发：保存文件并生成预览
const handleFileChange = (file) => {
  // 保存选中的文件（用于后续手动上传）
  tempUploadFile.value = file.raw; 
  
  // 生成前端临时预览 URL（blob 格式，不会上传到服务器）
  imageUrl.value = URL.createObjectURL(file.raw);
  
  ElMessage.success('图片加载成功');
};
// 图片上传成功后触发
const handleAvatarSuccess = (response,uploadFile) => {

    staffInfo.value.image = response.data;
    
    ElMessage.success('图片上传成功');
  
  
}
// 文件上传之前触发
const beforeAvatarUpload = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('只支持上传图片')
    return false
  } else if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('只能上传10M以内图片')
    return false
  }
  return true
}


//记录勾选的员工的id
const selectedIds = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map( item => item.id);
}

//批量删除
const deleteByIds = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该员工吗?','提示',
    { confirmButtonText: '确认',cancelButtonText: '取消',type: 'warning'}
  ).then(async () => { //确认
    if(selectedIds.value && selectedIds.value.length > 0){
      const result = await DeleteStaff(selectedIds.value);
      if(result.code){
        ElMessage.success('删除成功');
        onSubmit();
      }else{
        ElMessage.error(result.msg);
      }
    }else {
      ElMessage.info('您没有选择任何要删除的数据');
    }
  }).catch(() => { //取消
    ElMessage.info('您已取消删除');
  })
}



</script>


<template>
    <!-- 头部表单 -->
    <h3>员工管理</h3>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
    <el-form-item label="name">
      <el-input v-model="formInline.name" placeholder="name" clearable />
    </el-form-item>
    <el-form-item label="gender">
      <el-select
        v-model="formInline.gender"
        placeholder="gender"
        clearable
      > 
        <el-option label="male" value="1" />
        <el-option label="female" value="2" />
      </el-select>
    </el-form-item>
    <el-form-item label="hire date">
      
      
    <div class="block">
      <!-- <span class="demonstration">Default</span> -->
      <el-date-picker
        v-model="formInline.hiredate"
        type="daterange"
        range-separator="To"
        start-placeholder="Start date"
        end-placeholder="End date"
        format="YYYY-MM-DD"  
        value-format="YYYY-MM-DD" 
      />
    </div>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit()">Query</el-button>
      <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
    </el-form-item>
  </el-form>
  
  <!-- 两个按钮 -->
    <div class="container">
        <el-button type="primary" @click="addStaffOne()">+新增员工</el-button>
        <el-button type="primary" @click="deleteByIds()">-批量删除</el-button>
    </div>


    <!-- 表格 -->
     <el-table :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55" />
    <el-table-column property="name" label="name" width="120" />
    <el-table-column label="gender" width="120" >
      <template #default="scope">
        {{ scope.row.gender === 1 ? '男' : '女' }}
      </template>
    </el-table-column>
    <el-table-column label="head portrait" show-overflow-tooltip>
      <template #default="scope">
        <img :src="scope.row.image" style="width: 50px; height: 50px; border-radius: 50%;" />
      </template>
      </el-table-column>
    <el-table-column property="deptName" label="department" width="150"/>
    <el-table-column label="position" width="120">
      <template #default="scope">
          <span v-if="scope.row.job == 1">班主任</span>
          <span v-else-if="scope.row.job == 2">讲师</span>
          <span v-else-if="scope.row.job == 3">学工主管</span>
          <span v-else-if="scope.row.job == 4">教研主管</span>
          <span v-else-if="scope.row.job == 5">咨询师</span>
          <span v-else>其他</span>
        </template>
        </el-table-column>
    <el-table-column property="entryDate" label="hire date" width="150"/>
    <el-table-column property="updateTime" label="final operation time" />
    <el-table-column label="operation" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)">Edit</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">Delete</el-button>
        </template>
    </el-table-column>
  </el-table>


  <!-- 分页 -->
<div class="demo-pagination-block">
    
      <span class="pagination-label">每页条数：</span>
   
    <el-pagination 
    
        class="custom-pagination"
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[5, 10, 15, 20]"
      
      
      
      layout="sizes, total, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    
  </div>



  <!-- 新增员工or编辑员工对话框 -->
   <el-dialog v-model="dialogFormVisible" :title="dialogFormTitle" width="800px" align-center class="staff-dialog">


    
    
    
    <el-form 
    ref="staffFormRef"
    :rules="staffFormRules"
    :model="staffInfo"
    label-width="80px" 
    class="form-container">
      <!-- 第一行两列布局 -->
      <el-row :gutter="20">
        <el-col :span="12">

          <el-form-item label="用户名" prop="username">
            <el-input v-model="staffInfo.username" placeholder="请输入员工用户名，2-20个字"></el-input>
          </el-form-item>

          <el-form-item label="性别" prop="gender">
            <el-select placeholder="请选择" v-model="staffInfo.gender">
              <el-option 
              v-for="val in genders"
              :label="val.name" 
              :value="val.value"
              :key="val.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="职位">
            <el-select placeholder="请选择" v-model="staffInfo.job">
              <el-option 
              v-for="val in jobs"
              :label="val.name" 
              :value="val.value"
              :key="val.value"></el-option>
            </el-select>
          </el-form-item>
          
          <el-form-item label="所属部门">
            <el-select placeholder="请选择" v-model="staffInfo.deptId">
              <el-option 
              v-for="val in deptList"
              :label="val.name" 
              :value="val.id"
              :key="val.id"></el-option>
            </el-select>
          </el-form-item>

        </el-col>

        <el-col :span="12">

          <el-form-item label="姓名" prop="name">
            <el-input placeholder="请输入员工姓名，2-10个字" v-model="staffInfo.name" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="phone">
            <el-input placeholder="请输入员工手机号" v-model="staffInfo.phone" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="薪资" >
            <el-input placeholder="请输入员工薪资"  v-model="staffInfo.salary"></el-input>
          </el-form-item>

          <el-form-item label="入职日期">
            <el-date-picker
              type="date"
              v-model="staffInfo.entryDate"
              placeholder="请选择入职日期"
              value-format="YYYY-MM-DD"
            ></el-date-picker>
          </el-form-item>

        </el-col>
      </el-row>

      <!-- 头像上传 -->
      <el-form-item label="头像">
        <el-upload
            ref="uploadRef"  
            class="avatar-uploader"
            action="api/emps/upload"
            :show-file-list="false"
            :auto-upload="false"
            :on-change="handleFileChange"
            :before-upload="beforeAvatarUpload"
            :on-success="handleAvatarSuccess"
            
            
        >
            <img v-if="imageUrl" :src="imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <div slot="tip" class="el-upload__tip">
            图片大小不超过2M<br>
            仅能上传PNG、JPEG、JPG等图片<br>
            建议上传200*200或300*300尺寸的照片
        </div>
      </el-form-item>

      <!-- 工作经历（可动态添加） -->
      <el-form-item label="工作经历" label-position="top">
        <div v-for="(expr,index) in staffInfo.exprList" :key="index" class="work-experience">
          <div class="block">
          <span class="demonstration">时间</span>
          <el-date-picker
            v-model="expr.workDuring"
            type="daterange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
            format="YYYY-MM-DD"  
            value-format="YYYY-MM-DD" 
            style="width: 300px;"
          />
          </div>
          <el-input v-model="expr.company" placeholder="请输入公司的名字" style="width: 140px; margin: 0 10px;"></el-input>
          <el-input v-model="expr.job" placeholder="请输入职位" style="width: 100px;"></el-input>
          <el-button 
          type="danger"  
          style="width: 10px; margin-left: 10px;"
          @click="deleteWorkExperience(index)"><el-icon><Delete /></el-icon></el-button>
        </div>
        
      </el-form-item>
      <el-button type="success" @click="addWorkExperience()"><el-icon><Plus /></el-icon></el-button>
      
      
    </el-form>
  

    <!-- 操作按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancelSave()">Cancel</el-button>
        <el-button type="primary" @click="saveForm()">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>
<style>

.dialog-footer {
    text-align: center;
    
    
}

.dialog-footer button {
    margin: 0 20px;
}



/* 表格内容居中 */
.el-table .cell {
  text-align: center;
}

/* 表头居中 */
.el-table__header .cell {
  text-align: center;
}

.demo-form-inline .el-input {
  --el-input-width: 220px;
}

.demo-form-inline .el-select {
  --el-select-width: 220px;
}

.demo-pagination-block {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.pagination-label {
    width: 90px;
}

/* 分页组件容器使用 Flex 布局 */
.custom-pagination {
  display: flex;
  align-items: center;
  width: 100%;
}

.custom-pagination .el-pagination__sizes {
  margin-left: 8px;
  margin-right: auto; /* 这是关键，将其他元素推到右侧 */
}

/* 其他所有元素：总条数、上一页、页码、下一页、跳转 */
.custom-pagination .el-pagination__total,
.custom-pagination .el-pagination__prev,
.custom-pagination .el-pagination__pager,
.custom-pagination .el-pagination__next,
.custom-pagination .el-pagination__jump {
  margin-left: 8px; /* 增加元素间间距 */
}


/* 对话框样式 */
.form-container {
  margin-top: 10px;
}

.work-experience {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px dashed #e5e6eb;
  border-radius: 4px;
}

.work-experience .el-date-picker {
  width: 160px;
  margin-right: 10px;
}

.work-experience span {
  margin: 0 5px;
  color: #86909c;
}

.el-form-item {
  
  
  margin-bottom: 18px;
}

.el-form-item__content {
  line-height: 1.5;
}

.el-upload__tip {
  margin: 8px 10px;
  font-size: 12px;
  line-height: 1.5;
}

.el-button + .el-button {
  margin-left: 10px;
}


/* 头像上传器样式 */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}

.staff-dialog {
  height: 700px !important;
}

.staff-dialog .el-dialog__body {
  height: calc(700px - 110px) !important;
  overflow-y: auto !important;
  overflow-x: hidden;
}
</style>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}


</style>