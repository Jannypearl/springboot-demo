package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> update(Long id, User user) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        }
        user.setId(id);
        return Optional.of(userRepository.save(user));
    }

    public boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    /**
     * 导出用户数据为 CSV 格式字符串
     * @return CSV 格式的用户数据
     */
    public String exportUsersToCsv() {
        List<User> users = findAll();
        StringBuilder csvBuilder = new StringBuilder();
        
        // 添加 CSV 表头
        csvBuilder.append("ID,用户名,邮箱,年龄\n");
        
        // 添加用户数据
        users.forEach(user -> 
            csvBuilder.append(String.format("%d,%s,%s,%s\n",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAge() != null ? user.getAge() : ""))
        );
        
        return csvBuilder.toString();
    }
}
