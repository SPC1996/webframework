package com.keessi.xml.mapper;

import com.keessi.xml.entity.FStaff;

import java.util.List;
import java.util.Map;

public interface FStaffMapper {
    List<FStaff> selectAll();

    FStaff selectOneById(int id);

    FStaff selectOneByUsernameAndPassword(String username, String password);

    List<FStaff> selectSomeByKeys(Map<String, Object> keys);
}
