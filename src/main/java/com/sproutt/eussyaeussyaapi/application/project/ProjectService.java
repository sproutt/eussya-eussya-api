package com.sproutt.eussyaeussyaapi.application.project;

import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.project.dto.ProjectCreateDto;
import com.sproutt.eussyaeussyaapi.api.project.dto.ProjectUpdateDto;
import com.sproutt.eussyaeussyaapi.domain.project.Project;

public interface ProjectService {

    void addProject(JwtMemberDTO jwtMemberDTO, ProjectCreateDto projectCreateDto);

    void updateProject(JwtMemberDTO jwtMemberDTO, ProjectUpdateDto projectUpdateDto);

    void deleteProject();

    Project getProject();

}
