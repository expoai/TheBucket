package com.expoai.bucket.enums;

import com.expoai.bucket.entity.Role;

public enum RoleEnum {

    ADMIN(1),
    USER(2),
    SCRAPPER(3),
    ;

    long id ;

    RoleEnum(long i) {
        id = i;
    }

    Role buildRole() {
        return Role
                .builder()
                .id(id)
                .build() ;
    }
}
