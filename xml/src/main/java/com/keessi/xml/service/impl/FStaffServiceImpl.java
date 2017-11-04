package com.keessi.xml.service.impl;

import com.keessi.xml.entity.FStaff;
import com.keessi.xml.mapper.FStaffMapper;
import com.keessi.xml.service.FStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FStaffServiceImpl implements FStaffService {
    private final FStaffMapper mapper;

    @Autowired
    public FStaffServiceImpl(FStaffMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<FStaff> findAll() {
        return mapper.selectAll();
    }

    @Override
    public FStaff findOneById(int id) {
        return mapper.selectOneById(id);
    }

    @Override
    public FStaff login(String username, String password) {
        return mapper.selectOneByUsernameAndPassword(username, password);
    }

    @Override
    public List<FStaff> search(Map<String, Object> keys) {
        return mapper.selectSomeByKeys(keys);
    }
}
