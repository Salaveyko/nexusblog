package com.nexusblog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private String name;
    private Set<Long> userIds;
    public RoleDto (Long id, String name){
        this(id,name,new HashSet<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDto roleDto = (RoleDto) o;
        return Objects.equals(id, roleDto.id)
                && Objects.equals(name, roleDto.name)
                && Objects.equals(userIds, roleDto.userIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userIds);
    }
}
