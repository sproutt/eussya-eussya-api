package com.sproutt.eussyaeussyaapi.application.project;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.project.dto.ProjectCreateDto;
import com.sproutt.eussyaeussyaapi.api.project.dto.ProjectUpdateDto;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.project.Project;
import com.sproutt.eussyaeussyaapi.domain.project.ProjectRepository;
import com.sproutt.eussyaeussyaapi.domain.project.exceptions.NoSuchProjectException;
import com.sproutt.eussyaeussyaapi.domain.project.exceptions.NotAuthorizedException;
import io.swagger.annotations.Authorization;
import javax.naming.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Override
    public void addProject(JwtMemberDTO jwtMemberDTO, ProjectCreateDto projectCreateDto) {

        Member userInfo = memberRepository.findById(jwtMemberDTO.getId()).orElseThrow(NoSuchMemberException::new);
        Project project = projectCreateDto.toEntity(userInfo);

        projectRepository.save(project);
    }

    @Override
    public void updateProject(JwtMemberDTO jwtMemberDTO, ProjectUpdateDto projectUpdateDto) {

        Member userInfo = memberRepository.findById(jwtMemberDTO.getId()).orElseThrow(NoSuchMemberException::new);
        Project project = projectRepository.findById(projectUpdateDto.getId()).orElseThrow(NoSuchProjectException::new);

        if (!userInfo.isEqualId(project.getWriter().getMemberId())) {
            throw new NotAuthorizedException();
        }

        project.update(projectUpdateDto);

        projectRepository.save(project);

    }

    @Override
    public void deleteProject() {

    }

    @Override
    public Project getProject() {
        return null;
    }
}
