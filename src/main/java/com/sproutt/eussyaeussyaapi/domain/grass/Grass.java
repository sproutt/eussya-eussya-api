package com.sproutt.eussyaeussyaapi.domain.grass;

import com.sproutt.eussyaeussyaapi.domain.activity.Activity;
import com.sproutt.eussyaeussyaapi.domain.activity.CompleteActivityEvent;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Grass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @OneToOne
    @JoinColumn(nullable = false)
    private Activity activity;

    @Column
    private LocalDateTime generatedTime;

    public Grass(Activity activity){
        this(null, activity.getMember(), activity);
    }

    public Grass(Long id, Member member, Activity activity) {
        this.id = id;
        this.member = member;
        this.activity = activity;
    }

    public static Grass generate(Activity activity) {
        return new Grass(activity);
    }
}
