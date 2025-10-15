<script setup>
import { ref, onMounted } from 'vue'
import { getClazzList,addClazz,updateClazz,deleteClazz,getClazzOne} from '@/api/clazz2.js'
import { getStaffList } from '@/api/staff3'
import { ElMessage, ElMessageBox } from 'element-plus'



const tableData = ref([])

const dialogFormVisible = ref(false);
const formTitle = ref('');

const form = ref({
  name: '',
  room: '',
  beginDate: '',
  endDate: '',
  masterId: null,
  subjectId: null,
})


// 钩子函数
onMounted(() => {
    onSubmit();
    getallstaff();
})


// 新增班级按钮
const addClassForm = () => { 
  formTitle.value = '新增班级';
  dialogFormVisible.value = true;
  form.value = {};
  
   if (ruleFormRef.value){ 
    ruleFormRef.value.resetFields();
   }
}
// 确认保存按钮
const conFirmsave = async () => { 
  if(!ruleFormRef.value)return;
  //判断是新增还是编辑
  
 
  ruleFormRef.value.validate( async (valid) => {
    if (valid) { 
      let result ;
      if(form.value.id){
      result = await updateClazz(form.value);
    }else{
      result = await addClazz(form.value);
    }
    if(result.code){
      dialogFormVisible.value = false;
      onSubmit();
      ElMessage.success('保存成功')
      }else{
        ElMessage.error(result.msg);
      }
    
      
    } else {
      ElMessage.error('请检查输入项');
    }
  });
  
}

const cancelsave = () => { 
  dialogFormVisible.value = false;
  ElMessage.info('取消成功')
}

// 编辑班级按钮
const editForm = async (id) => {
  formTitle.value = '编辑班级';
  dialogFormVisible.value = true;
  if (ruleFormRef.value){ 
    ruleFormRef.value.resetFields();
   }
   const result = await getClazzOne(id);
   console.log(result);
   if(result.code){
    form.value = result.data;
   }else{
    ElMessage.error(result.msg);
   }
}
// 删除班级按钮
const deleteForm =  (id) => {
  console.log(id);
  ElMessageBox.confirm(
    
    'Are you sure to delete this class?',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
      const result = await deleteClazz(id);
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

const rules = ref({
  name: [
    { required: true, message: 'Please input Activity name', trigger: 'blur' },
    { min: 3, max: 10, message: 'Length should be 3 to 10', trigger: 'blur' },
  ],
  beginDate: [
    { required: true, message: 'Please input begin date', trigger: 'blur' },
  ],
  endDate: [
    { required: true, message: 'Please input end date', trigger: 'blur' },
  ],
  masterId: [
    { required: true, message: 'Please input masterId', trigger: 'blur' },
  ],
  subject: [
    { required: true, message: 'Please input subjectId', trigger: 'blur' },
  ],
})

// let centerDialogVisible = ref(false);

const ruleFormRef = ref();


// 头部查询数据表单
const queryForm = ref({
    name: '',
    date: [],
    begin: '',
    end: ''
})

// 头部查询表单提交 按钮
const onSubmit = async () => { 
  const result = await getClazzList(
      queryForm.value.name,
      queryForm.value.date.length > 0? queryForm.value.date[0] : '',
      queryForm.value.date.length > 0? queryForm.value.date[1] : '',
      currentPage.value,
      pageSize.value
    );
    console.log(result);
    if(result.code){
    tableData.value = result.data.rows
    total.value = result.data.total
    }else{
      ElMessage.error(result.data.msg);
    }
}


// 重置头部查询数据表单 按钮
const resetForm = (ruleFormRef) => {
  if (ruleFormRef.value) {
    ruleFormRef.value.resetFields();
  }
  queryForm.value = {
    name: '',
    date: [],
    begin: '',
    end: ''
  }
   onSubmit();
}


const currentPage = ref(1);   // 默认页数
const pageSize = ref(10);     // 默认每页条数
const total = ref(0);         // 总条数

// 监听每页条数变化
const handleSizeChange = (size) => {
  pageSize.value = size;
   onSubmit();
}

// 监听页数变化
const handleCurrentChange = (page) => {
  currentPage.value = page;
  onSubmit();
}

// 存储员工列表
const staffList = ref([]);
// 获取员工列表用于班主任选项
const getallstaff = async () => {
    const result = await getStaffList('','','','',1,999999);
    if(result.code){
      staffList.value = result.data.rows
    }else{
      ElMessage.error(result.data.msg);
    }
}

// 学科对应数据
const subject = ref([
  {id: 1, name: 'Java'},
  {id: 2, name: '前端'},
  {id: 3, name: '大数据'},
  {id: 4, name: 'Python'},
  {id: 5, name: 'Golong'},
  {id: 6, name: '嵌入式'},
]);

</script>

<template>

    <h3>班级管理</h3>
    <!-- 头部表单 -->
    <el-form :inline="true" :model="queryForm" class="demo-form-inline">

    <el-form-item label="Class Name">
      <el-input v-model="queryForm.name" placeholder="name" clearable />
    </el-form-item>
    
    <el-form-item label="period of schooling">
    <div class="block">
      <!-- <span class="demonstration">Default</span> -->
      <el-date-picker
        v-model="queryForm.date"
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

  <!-- 按钮 -->
    <div class="container">
    <el-button type="primary" @click="addClassForm()">+新增班级</el-button>
    </div>

<!-- 表格 -->
<div class="container">
 <el-table :data="tableData" border style="width: 100%">
    <el-table-column type="index" label="序号" width="60" align="center"/>
    <el-table-column prop="name" label="班级名称"  align="center"/>
    <el-table-column prop="room" label="班级教室"  align="center"/>

    <el-table-column  label="班主任"  align="center">
      <template #default="scope">
        {{scope.row.masterId? staffList.find(item => item.id === scope.row.masterId)?.name : '无'}}
      </template>
    </el-table-column>

    <el-table-column prop="beginDate" label="开课时间"  align="center"/>
    <el-table-column prop="endDate" label="结课时间"  align="center"/>
    <el-table-column prop="status" label="状态"  align="center"/>
    <el-table-column prop="updateTime" label="最后操作时间" align="center"/>
    <el-table-column  label="操作" align="center">
        <template #default="scope">
        <el-button size="small" @click="editForm(scope.row.id)">
          Edit
        </el-button>
        <el-button
          size="small"
          type="danger"
          @click="deleteForm(scope.row.id)"
        >
          Delete
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</div>


<!-- 对话框 -->
 <el-dialog v-model="dialogFormVisible" :title="formTitle" width="400" align-center>
  
    <el-form :model="form" :rules="rules" ref="ruleFormRef">

      <el-form-item label="班级名称" label-width= "80px" prop="name" >
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>

      <el-form-item label="班级教室" label-width= "80px"  >
        <el-input v-model="form.room" autocomplete="off" />
      </el-form-item>

      <el-form-item label="开课时间" label-width= "80px" prop="beginDate" >
        
        <el-date-picker
        v-model="form.beginDate"
        type="date"
        placeholder="start date"
        value-format="YYYY-MM-DD"
        format="YYYY-MM-DD"
      />
      
      </el-form-item>

      <el-form-item label="结课时间" label-width= "80px" prop="endDate" >
        <el-date-picker
        v-model="form.endDate"
        type="date"
        placeholder="end date"
        value-format="YYYY-MM-DD"
        format="YYYY-MM-DD"
        
      />
      </el-form-item>

      <el-form-item label="班主任" label-width= "80px" prop="masterId" >
        <el-select v-model="form.masterId" placeholder="Select master">
          <el-option
            v-for="item in staffList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="学科" label-width= "80px" prop="subject" >
        <el-select v-model="form.subject" placeholder="Select subject">
          <el-option
            v-for="item in subject"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="cancelsave()">Cancel</el-button>
        <el-button type="primary" @click="conFirmsave()">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog>

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

</template>

<style scoped>
.container {
  margin: 20px;
}
</style>