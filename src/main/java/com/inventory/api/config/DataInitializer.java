package com.inventory.api.config;

import com.inventory.api.model.User;
import com.inventory.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Initializing database with seed data...");

            User admin = User.builder()
                    .username("admin")
                    .email("admin@inventory.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Created ADMIN user: {} (Admin)", admin.getUsername());

            User manager = User.builder()
                    .username("manager")
                    .email("manager@inventory.com")
                    .password(passwordEncoder.encode("manager123"))
                    .role(User.Role.MANAGER)
                    .build();
            userRepository.save(manager);
            log.info("Created MANAGER user: {} (Manager)", manager.getUsername());

            User user = User.builder()
                    .username("user")
                    .email("user@inventory.com")
                    .password(passwordEncoder.encode("user123"))
                    .role(User.Role.USER)
                    .build();
            userRepository.save(user);
            log.info("Created USER: {} (User)", user.getUsername());

            log.info("Database initialization completed successfully!");
            log.info("===========================================");
            log.info("Test Credentials:");
            log.info("ADMIN - username: admin, password: admin123");
            log.info("MANAGER - username: manager, password: manager123");
            log.info("USER - username: user, password: user123");
            log.info("===========================================");
        }
    }
}
