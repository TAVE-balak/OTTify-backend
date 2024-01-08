package tavebalak.OTTify.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.common.constant.GradeType;
import tavebalak.OTTify.common.constant.Role;
import tavebalak.OTTify.common.constant.SocialType;
import tavebalak.OTTify.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "\"user\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String email;
    private String nickName;
    private String profilePhoto;
    private double averageRating;

    @Enumerated(EnumType.STRING)
    private GradeType grade;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String code; //구글, 네이버의 로그아웃, 탈퇴를 위한 코드

    @Builder
    public User(String email, String nickName, String profilePhoto, SocialType socialType, Role role, String code) {
        this.email = email;
        this.nickName = nickName;
        this.profilePhoto = profilePhoto;
        this.averageRating = 0;
        this.grade = GradeType.GENERAL;
        this.socialType = socialType;
        this.role = role;
        this.code = code;
    }

    public static TestUserBuilder testUserBuilder(){
        return new TestUserBuilder();
    }

    public static class TestUserBuilder extends User {
        private Long id;
        private String nickName;
        private String profilePhoto;
        private double averageRating;
        TestUserBuilder(){
        }

        public User create(final Long id, String nickName, String profilePhoto, double averageRating){
            this.id = id;
            this.nickName = nickName;
            this.profilePhoto = profilePhoto;
            this.averageRating = averageRating;
            return this;
        }
    }

    public void changeUserRole(){
        role = Role.USER;
    }

    public void changeCode(String code) { this.code = code; }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }
    public void changeProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}