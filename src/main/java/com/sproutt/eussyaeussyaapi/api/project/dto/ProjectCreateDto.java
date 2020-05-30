package com.sproutt.eussyaeussyaapi.api.project.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.project.Project;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProjectCreateDto {

    @NotNull
    @Size(max = 80)
    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate finishDate;

    @NotNull
    @Size(max = 100)
    private int countOfGoal;

    public Project toEntity(Member writer) {
        return Project.builder()
                      .countOfGoal(countOfGoal)
                      .finishDate(finishDate)
                      .startDate(startDate)
                      .title(title)
                      .writer(writer)
                      .build();
    }

}
