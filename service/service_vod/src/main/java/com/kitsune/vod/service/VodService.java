package com.kitsune.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {

    String uploadVideoAly(MultipartFile file);

    void removeAliyunVideo(String id);

    void removeMoreAliyunVideo(List videoIdList);
}
