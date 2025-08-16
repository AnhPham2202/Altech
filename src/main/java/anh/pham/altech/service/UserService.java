package anh.pham.altech.service;

import anh.pham.altech.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    User getRefById(UUID id);
}
