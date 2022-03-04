package com.kitsune.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kitsune.eduservice.entity.EduSubject;
import com.kitsune.eduservice.entity.excel.SubjectData;
import com.kitsune.eduservice.listener.subjectExcelListener;
import com.kitsune.eduservice.mapper.EduSubjectMapper;
import com.kitsune.eduservice.service.EduSubjectService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author Kitsune
 * @since 2022-03-03
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

//    添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //先得到文件的输入流
            InputStream in = file.getInputStream();
            //调用方法读取文件
            EasyExcel.read(in, SubjectData.class, new subjectExcelListener()).sheet().doRead();
        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
