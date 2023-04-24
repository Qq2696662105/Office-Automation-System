/*
角色管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = "/admin/system/sysRole/"

export default{
    /*
    获取角色分页列表(带搜索)
    */
    getPageList(page,limit,searchOjb){
        return request({
            url: `${api_name}/${page}/${limit}`,
            method: 'get',
            params: searchOjb
          })
    },
    removeById(id) {
        return request({
          url: `${api_name}/remove/${id}`,
          method: 'delete'
        })
    },
    insert(role){
        return request({
            url: `${api_name}/save`,
            method: 'post',
            data: role
        })
    },
    getById(id) {
        return request({
          url: `${api_name}/get/${id}`,
          method: 'get'
        })
    },
    update(role){
        return request({
            url: `${api_name}/update`,
            method: 'post',
            data: role
        })
    },
    batchRemove(idList) {
        return request({
          url: `${api_name}/batchRemove`,
          method: `delete`,
          data: idList
        })
    },
    getRoles(adminId) {
        return request({
          url: `${api_name}/getAssign/${adminId}`,
          method: 'get'
        })
    },
      
    assignRoles(assginRoleVo) {
        return request({
          url: `${api_name}/doAssign`,
          method: 'post',
          data: assginRoleVo
        })
    }
    
}