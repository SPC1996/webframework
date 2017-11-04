package com.keessi.xml.service;

import com.keessi.xml.entity.FStaff;

import java.util.List;
import java.util.Map;

public interface FStaffService {
    List<FStaff> findAll();

    FStaff findOneById(int id);

    FStaff login(String username, String password);

    List<FStaff> search(Map<String, Object> keys);
}
