<script setup>
import { ElMessage , ElMessageBox} from 'element-plus'
import { ref , onMounted , watch} from 'vue'
import { getStuList,getStuOne,addStu,updateStu,deleteStu,deductScore} from '@/api/stu2'
import { getClazzList } from '@/api/clazz2'

// 查询表单数据
const formInline = ref({
  name: '',
  degree: '',
  clazzId: '',
  
})



// 钩子函数，页面加载完成后，获取员工列表
onMounted( async () => {
  onSubmit()
   //加载所有班级数据
  const result = await getClazzList('','','',1,999999);
  console.log(result);
  if(result.code){
    clazzList.value = result.data.rows
  }else{
    ElMessage.error(result.msg);
  }
})

// 存储班级列表
const clazzList = ref([]);

// 表单查询按钮
const onSubmit = async ()  => {
    const res = await getStuList(
        formInline.value.name,
        formInline.value.degree,
        formInline.value.clazzId,
        currentPage.value,
        pageSize.value)
    console.log(res);
        if(res.code){
      tableData.value = res.data.rows
      total.value = res.data.total
    }else{
      ElMessage.error(res.msg);
    }
}

const tableData = ref([])           //员工数据列表


// 表单重置查询按钮
const resetForm = () => {
    formInline.value = {
        name: '',
        degree: '',
        clazzId: '',
    }
    onSubmit()
}


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
      const result = await deleteStu(ids);
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

// 新增学员按钮
const addStuOne = () => {
    dialogFormVisible.value = true
    dialogFormTitle.value = '新增学员'
    clearStuInfo()
    if(stuFormRef.value){
      stuFormRef.value.resetFields()
  }
}


const stuFormRef = ref()   // 学员信息表单规则引用
// 对话框学员信息数据
const stuInfo = ref({
  name: '',
  gender: '',
  idCard: '',
  address: '',
  graduationDate: '',

  on: '',
  phone: '',
  isCollege: '',
  degree: '',
  clazzId: '',

})


// 表单校验规则
const stuFormRules = {
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


// 清空学员信息
const clearStuInfo = () => { 
  stuInfo.value = {
  name: '',
  gender: '',
  idCard: '',
  address: '',
  graduationDate: '',

  on: '',
  phone: '',
  isCollege: '',
  degree: '',
  clazzId: '',

}
}

// 学历选项列表
const degrees = ref([{ name: '初中', value: 1 }, { name: '高中', value: 2 }, { name: '专科', value: 3 }, { name: '本科', value: 4 }, { name: '硕士', value: 5 }, { name: '博士', value: 6 }])

//性别列表数据
const genders = ref([{ name: '男', value: 1 }, { name: '女', value: 2 }])

const dialogFormTitle = ref('')   // 学员信息对话框标题

const isColleges = ref([{ name: '院校', value: 1 }, { name: '非院校', value: 0 }])

// 学员信息表单数据保存按钮
const saveForm = async () => {
  // 确保表单引用存在
  if (!stuFormRef.value) {
    console.error('表单引用不存在')
    return
  }
   
    // 先进行表单校验
    const isValid = await new Promise((resolve) => {
      stuFormRef.value.validate((valid) => {
        resolve(valid)
      })
    })
  
    if (!isValid) {
      ElMessage.error('请填写完整且正确的表单信息')
      return
    }

      let result;
      if(stuInfo.value.id){ //修改
        result = await updateStu(stuInfo.value);
      }else { //新增
        result = await addStu(stuInfo.value);
      }
      
      if(result.code) {//成功
        ElMessage.success('保存成功');
        dialogFormVisible.value = false;
        onSubmit();
      }else { //失败了
        ElMessage.error(result.msg);
      }
      
    
  }

//  添加取消保存的函数，确保清空相关状态
const cancelSave = () => {
  dialogFormVisible.value = false
  clearStuInfo()
  ElMessage.info('已取消保存')
}


// 编辑学员按钮
const handleEdit = async (id) => {
  if(stuFormRef.value){
      stuFormRef.value.resetFields()
  }
    const result = await getStuOne(id);
    console.log(result);
  if(result.code){
    dialogFormVisible.value = true;
    dialogFormTitle.value = '修改学员';
    stuInfo.value = result.data;
  }
}


//记录勾选的学员的id
const selectedIds = ref([]);
//复选框勾选发生变化时触发 - selection: 当前选中的记录 (数组)
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map( item => item.id);
  console.log(selectedIds.value);
}

//批量删除
const deleteByIds = () => {
  //弹出确认框
  ElMessageBox.confirm('您确认删除该员工吗?','提示',
    { confirmButtonText: '确认',cancelButtonText: '取消',type: 'warning'}
  ).then(async () => { //确认
    if(selectedIds.value && selectedIds.value.length > 0){
      const result = await deleteStu(selectedIds.value);
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

<<<<<<< HEAD
const deductVioScore = ref('')      // 扣分分数
const studentId = ref('')            // 违纪的学员id
const dialogViolationVisible = ref(false)   // 违纪信息对话框显示隐藏
// 违纪按钮
const handleViolation = (id) => {
  dialogViolationVisible.value = true
  deductVioScore.value = ''
  studentId.value = id
  
}
// 确认扣分按钮
const confirmViolation = async () => {
  dialogViolationVisible.value = false
  const result = await deductScore(studentId.value,deductVioScore.value);
  if(result.code){
    ElMessage.success('扣分成功');
    dialogViolationVisible.value = false;
    onSubmit();
  }else{
    ElMessage.error(result.msg);
  }
}
// 取消扣分对话框按钮
const cancelViolation = () => {
  dialogViolationVisible.value = false
  
}
=======

>>>>>>> 1835923879e3ccaf4295a3cfe36a67f24abf2077



</script>


<template>
    <!-- 头部表单 -->
    <h3>学员管理</h3>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
    <el-form-item label="name">
      <el-input v-model="formInline.name" placeholder="name" clearable />
    </el-form-item>
    <el-form-item label="degree">
      <el-select
        v-model="formInline.degree"
        placeholder="degree"
        clearable
      > 
        <el-option v-for="item in degrees"
         :label="item.name"
         :value="item.value"
         :key="item.value" />
      </el-select>
    </el-form-item>
    <el-form-item label="class">
      <el-select
        v-model="formInline.clazzId"
        placeholder="class"
        clearable
      > 
        <el-option v-for="item in clazzList"
         :label="item.name"
         :value="item.id"
         :key="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit()">Query</el-button>
      <el-button @click="resetForm(ruleFormRef)">Reset</el-button>
    </el-form-item>
  </el-form>
  
  <!-- 两个按钮 -->
    <div class="container">
        <el-button type="primary" @click="addStuOne()">+新增学员</el-button>
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
    <el-table-column property="no" label="student number" width="150"/>
    <el-table-column property="clazzName" label="class" width="150"/>
    <el-table-column label="degree" width="120">
      <template #default="scope">
          <span v-if="scope.row.degree == 1">初中</span>
          <span v-else-if="scope.row.degree == 2">高中</span>
          <span v-else-if="scope.row.degree == 3">专科</span>
          <span v-else-if="scope.row.degree == 4">本科</span>
          <span v-else-if="scope.row.degree == 5">硕士</span>
          <span v-else-if="scope.row.degree == 6">博士</span>
          <span v-else>其他</span>
        </template>
        </el-table-column>
    <el-table-column property="violationCount" label="violationCount" width="150"/>
    <el-table-column property="violationScore" label="violationScore" />
    <el-table-column property="updateTime" label="updateTime" />
<<<<<<< HEAD
    <el-table-column label="operation" width="200">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)">Edit</el-button>
          <el-button size="small" @click="handleViolation(scope.row.id)">违纪</el-button>
=======
    <el-table-column label="operation" width="180">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)">Edit</el-button>
>>>>>>> 1835923879e3ccaf4295a3cfe36a67f24abf2077
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



  <!-- 新增学员or编辑学员对话框 -->
   <el-dialog v-model="dialogFormVisible" :title="dialogFormTitle" width="800px" align-center >


    
    
    
    <el-form 
    ref="stuFormRef"
    :rules="stuFormRules"
    :model="stuInfo"
    label-width="80px" 
    class="form-container">
      <!-- 第一行两列布局 -->
      <el-row :gutter="20">
        <el-col :span="12">

          <el-form-item label="姓名" prop="name">
            <el-input v-model="stuInfo.name" placeholder="请输入学员姓名"></el-input>
          </el-form-item>

          <el-form-item label="性别" prop="gender">
            <el-select placeholder="请选择" v-model="stuInfo.gender">
              <el-option 
              v-for="val in genders"
              :label="val.name" 
              :value="val.value"
              :key="val.value"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="身份证号">
            <el-input v-model="stuInfo.idCard" placeholder="请输入学员身份证号"></el-input>
          </el-form-item>
          
          <el-form-item label="联系地址" prop="address">
            <el-input v-model="stuInfo.address" placeholder="请输入学员联系地址"></el-input>
          </el-form-item>

          <el-form-item label="毕业时间" label-width= "80px" prop="graduationDate" >
        
        <el-date-picker
        v-model="stuInfo.graduationDate"
        type="date"
        placeholder="请选择毕业时间"
        value-format="YYYY-MM-DD"
        format="YYYY-MM-DD"
      />
      
      </el-form-item>

        </el-col>

        <el-col :span="12">

          <el-form-item label="学号" >
            <el-input placeholder="请输入学号" v-model="stuInfo.no" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="phone">
            <el-input placeholder="请输入员工手机号" v-model="stuInfo.phone" autocomplete="off"></el-input>
          </el-form-item>

          <el-form-item label="是否院校" >
            <el-radio-group v-model="stuInfo.isCollege">
              <el-radio v-for="item in isColleges"
               :label="item.value"
               :key="item.value">{{item.name}}</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="最高学历">
            
            <el-select v-model="stuInfo.degree" placeholder="请选择">
              <el-option v-for="item in degrees"
               :label="item.name" 
               :value="item.value"
               :key="item.value"></el-option>
              
            </el-select>
            
          </el-form-item>

          <el-form-item label="所属班级">
            
            <el-select v-model="stuInfo.clazzId" placeholder="请选择">
              <el-option v-for="item in clazzList"
               :label="item.name" 
               :value="item.id"
               :key="item.id"></el-option>
              
            </el-select>
            
          </el-form-item>

        </el-col>
      </el-row>
      
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
<<<<<<< HEAD

  <!-- 违纪扣分对话框 -->
  <el-dialog v-model="dialogViolationVisible" title="违纪扣分" width="500" align-center>
    <el-form-item label="违纪扣分" prop="score">
        <el-input v-model="deductVioScore" placeholder="请输入违纪扣分"></el-input>
      </el-form-item>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancelViolation()">Cancel</el-button>
        <el-button type="primary" @click="confirmViolation()">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>

=======
>>>>>>> 1835923879e3ccaf4295a3cfe36a67f24abf2077
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









</style>

