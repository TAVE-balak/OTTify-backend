package tavebalak.OTTify.review.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewTag {
    @Id @GeneratedValue
    @Column(name = "review_tag_id")
    private Long id;
    private String name;

    @Builder
    public ReviewTag(String name){
        this.name=name;
    }
}
