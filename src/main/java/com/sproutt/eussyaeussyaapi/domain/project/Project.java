package com.sproutt.eussyaeussyaapi.domain.project;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(length = 80)
    private String title;

    @ManyToOne
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_project_writer"))
    @Column
    private Member writer;

    @CreatedDate //객체 생성시
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate finishDate;

    @Column
    private int countOfGoal;

    @Builder
    public Project(String title, Member writer, LocalDate startDate, LocalDate finishDate, int countOfGoal) {
        this.title = title;
        this.writer = writer;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.countOfGoal = countOfGoal;
    }

}
