package com.sproutt.eussyaeussyaapi.application.profile;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface ProfileService {

    String uploadProfile(Member loginMember, MultipartFile file) throws IOException;

    boolean isImageType(String fileName);

    String getDefaultProfilePath(Member loginMember);
}
