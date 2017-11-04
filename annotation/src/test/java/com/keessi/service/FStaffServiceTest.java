package com.keessi.service;

import com.keessi.annotation.config.RootConfig;
import com.keessi.annotation.entity.FStaff;
import com.keessi.annotation.service.FStaffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class FStaffServiceTest {
    @Autowired
    private FStaffService service;

    @Test
    public void findAll() {
        List<FStaff> staffs = service.findAll();
        System.out.println(staffs);
        assertNotNull(staffs);
    }
}
