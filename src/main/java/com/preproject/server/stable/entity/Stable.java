package com.preproject.server.stable.entity;

import com.preproject.server.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Stable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stableId;

    @Column(nullable = true)
    String model;

    @Column(nullable = true)
    String prompt;

    @Column(nullable = true)
    String negative_prompt;

    // default : 768
    @Column(nullable = true)
    Integer width;

    // default : 768
    @Column(nullable = true)
    Integer height;

    // default : 50
    @Column(nullable = true)
    Integer num_inference_steps;

    // default : 7.5
    @Column(nullable = true)
    Double guidance_scale;

    // default : -1
    @Column(nullable = true)
    Integer seed;

    @Column(nullable = false)
    String imageUrl;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    public void addMember(Member member) {
        this.member = member;
        member.addStable(this);
    }
}
