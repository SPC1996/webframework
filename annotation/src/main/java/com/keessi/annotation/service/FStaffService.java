package com.keessi.annotation.service;

import com.keessi.annotation.entity.FStaff;

import java.util.List;
import java.util.Map;

public interface FStaffService {
    List<FStaff> findAll();

    FStaff findOneById(int id);

    FStaff login(String username, String password);

    List<FStaff> search(Map<String, Object> keys);
}
