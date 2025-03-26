package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        try {
            // 从Authorization头中提取token
            String token = authHeader.replace("Bearer ", "");
            
            // 验证token并获取用户ID
            if (!jwtUtil.validateToken(token)) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 401);
                response.put("message", "无效的token");
                return ResponseEntity.ok(response);
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserInfo(userId);
            
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 404);
                response.put("message", "用户不存在");
                return ResponseEntity.ok(response);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getId());
            data.put("username", user.getUsername());
            data.put("email", user.getEmail());
            data.put("createTime", user.getCreateTime());

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "success");
            response.put("data", data);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", "服务器内部错误");
            return ResponseEntity.ok(response);
        }
    }
}