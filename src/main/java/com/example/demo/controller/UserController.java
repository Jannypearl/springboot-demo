package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 查询所有用户
    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }

    // 根据 ID 查询用户
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }

    // 创建用户
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result<User> create(@Valid @RequestBody User user) {
        return Result.success(userService.save(user));
    }

    // 更新用户
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        return userService.update(id, user)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (userService.deleteById(id)) {
            return Result.success();
        }
        return Result.error(404, "用户不存在");
    }

    // 导出用户数据为 CSV
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCsv() {
        String csvContent = userService.exportUsersToCsv();
        byte[] csvBytes = csvContent.getBytes();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv;charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", "users_export.csv");
        headers.setContentLength(csvBytes.length);
        
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
