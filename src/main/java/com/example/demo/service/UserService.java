package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public User login(String phone, String password) {
        User user = userMapper.findByPhone(phone);
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    public User getUserInfo(Long userId) {
        return userMapper.findById(userId);
    }
}