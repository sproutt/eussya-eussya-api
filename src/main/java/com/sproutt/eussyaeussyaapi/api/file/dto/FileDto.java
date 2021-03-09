package com.sproutt.eussyaeussyaapi.api.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDto {
    private String nickName;
    private String storagePath;

    @Builder
    public FileDto(String nickName, String storagePath) {
        this.nickName = nickName;
        this.storagePath = storagePath;
    }
}
