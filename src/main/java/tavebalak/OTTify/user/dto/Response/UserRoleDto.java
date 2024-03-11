package tavebalak.OTTify.user.dto.Response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import tavebalak.OTTify.common.constant.Role;

@Getter
public class UserRoleDto {

    @ApiModelProperty(value = "Role")
    private Role role;

    public UserRoleDto(Role role) {
        this.role = role;
    }
}
