<script setup>
import { ref, onMounted } from 'vue'
const tableData = ref([])
import { getall,deletedept,adddept,updatedept,getByIddept } from '@/api/dept3.js'
import { ElMessage, ElMessageBox } from 'element-plus'

const dialogFormVisible = ref(false);
const formTitle = ref('');

const form = ref({
  name: '',
  
})

onMounted(() => {
    getalldept()
})

const getalldept = async () => {
    const result = await getall();
    if(result.code){
    tableData.value = result.data
    }else{
      ElMessage.error(result.data.msg);
    }
}

const addForm = () => { 
  formTitle.value = '新增部门';
  dialogFormVisible.value = true;
  form.value = {name: ''};
   if (ruleFormRef.value){ 
    ruleFormRef.value.resetFields();
   }
}

const conFirmsave = async () => { 
  if(!ruleFormRef.value)return;
  //判断是新增还是编辑
  
 
  ruleFormRef.value.validate( async (valid) => {
    if (valid) { 
      let result ;
      if(form.value.id){
      result = await updatedept(form.value);
    }else{
      result = await adddept(form.value);
    }
    if(result.code){
      dialogFormVisible.value = false;
      getalldept();
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


const editForm = async (id) => {
  formTitle.value = '编辑部门';
  dialogFormVisible.value = true;
  if (ruleFormRef.value){ 
    ruleFormRef.value.resetFields();
   }
   const result = await getByIddept(id);
   console.log(result);
   if(result.code){
    form.value = result.data;
   }else{
    ElMessage.error(result.msg);
   }
}

const deleteForm =  (id) => {
  console.log(id);
  ElMessageBox.confirm(
    
    'Are you sure to delete this department?',
    'Warning',
    {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning',
    }
  )
    .then(async () => {
      try {
      const result = await deletedept(id);
      if(result.code){
        getalldept();
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
  ]
})

// let centerDialogVisible = ref(false);

const ruleFormRef = ref();

</script>

<template>

    <h1>部门管理</h1>
    <div class="container">
    <el-button type="primary" @click="addForm()">+新增部门</el-button>
    </div>

<!-- 表格 -->
<div class="container">
 <el-table :data="tableData" border style="width: 100%">
    <el-table-column type="index" label="序号" width="180" align="center"/>
    <el-table-column prop="name" label="部门名称" width="180" align="center"/>
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
 <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500" align-center>
  
    <el-form :model="form" :rules="rules" ref="ruleFormRef">
      <el-form-item label="部门名称" label-width= "80px" prop="name" >
        <el-input v-model="form.name" autocomplete="off" />
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

  <!-- delete对话框-->
   <!-- <el-dialog
    v-model="centerDialogVisible"
    title="Warning"
    width="500"
    align-center
  >
    <span>Are you sure to delete this department?</span>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="centerDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="deleteForm()">
          Confirm
        </el-button>
      </div>
    </template>
  </el-dialog> -->

</template>

<style scoped>
.container {
  margin: 20px;
}
</style>