package tech.wetech.admin.model.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id; //编号
    private String username; //用户名
    private Long organizationId; //所属公司
    private String roleIds; //拥有的角色列表

}