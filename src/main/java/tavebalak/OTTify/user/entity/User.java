package tavebalak.OTTify.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tavebalak.OTTify.oauth.constant.Role;
import tavebalak.OTTify.oauth.constant.SocialType;
import lombok.AccessLevel;
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

    @Enumerated
    private Role role;

    @Builder
    public User(Long id, String nickName, double averageRating, String profilePhoto){
        this.id = id;
        this.nickName = nickName;
        this.averageRating = averageRating;
        this.profilePhoto = profilePhoto;
    }

    public User(String email, String nickName, String profilePhoto, SocialType socialType, Role role) {
        this.email = email;
        this.nickName = nickName;
        this.profilePhoto = profilePhoto;
        this.averageRating = 0;
        this.grade = GradeType.GENERAL;
        this.socialType = socialType;
        this.role = role;
    }
}
