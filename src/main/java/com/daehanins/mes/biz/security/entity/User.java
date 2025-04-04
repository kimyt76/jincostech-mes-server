package com.daehanins.mes.biz.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 사용자Users 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("users")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    public User() {
        super();
    }

    public User(String userId, String email, String password) {
        this.userId = userId;
//        this.email = email;
        this.password = password;
    }

    /**
     * 사용자ID
     */
    @TableId("user_id")
    private String userId;

    /**
     * 사용자로그인명  :: reserved by spring security
     */
    @TableField(exist = false)
    private String username;

    /**
     * 비밀번호
     */
    private String password;

    /**
     * 사용자이름
     */
    private String memberName;

    /**
     * 사용자코드(사번)
     */
    private String memberCd;

    /**
     * 직급
     */
    private String jobPosition;

    /**
     * 부서
     */
    private String department;

    /**
     * 핸드폰번호
     */
    private String cellPhone;

    /**
     * 이메일
     */
    private String email;

    /**
     * 사용여부
     */
    private String useYn;

    /**
     * 비고
     */
    private String memo;

    /**
     * 부여권한
     */
    @TableField(exist=false)
    private Set<Role> roles = new HashSet<>();

    // Spring Security 적용하면서 username 대신에 user_id 사용하기위한 처리
    public String getUsername() {
        return this.userId;
    }
    public void setUsername(String username) {
        this.userId = username;
    }

}
