package com.kitsune;


import com.kitsune.eduservice.EduApplication;
import com.kitsune.eduservice.entity.EduCourse;
import com.kitsune.eduservice.service.EduCourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

//@SpringBootConfiguration
@SpringBootTest(classes = EduApplication.class)
public class demo {

    @Autowired
    private EduCourseService eduCourseService;

    @Test
    public void run(){
        EduCourse course = eduCourseService.getById("1533782144808644609");

        course.setPrice((new BigDecimal(3.5)));
        course.setBuyCount(new Long(40));

        eduCourseService.updateById(course);

    }


}
