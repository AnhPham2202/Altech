package anh.pham.altech.service.impl;

import anh.pham.altech.entity.User;
import anh.pham.altech.repository.UserRepository;
import anh.pham.altech.service.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }
    @Override
    public User getRefById(UUID id) {
        return userRepository.getReferenceById(id);
    }
}
