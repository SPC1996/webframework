package com.keessi.annotation.mapper;

import com.keessi.annotation.entity.FStaff;

import java.util.List;
import java.util.Map;

public interface FStaffMapper {
    List<FStaff> selectAll();

    FStaff selectOneById(int id);

    FStaff selectOneByUsernameAndPassword(String username, String password);

    List<FStaff> selectSomeByKeys(Map<String, Object> keys);
}
