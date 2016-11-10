package de.redmann.test.backend.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;
import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.persistence.repositories.PasswordResetTokenRepository;
import de.redmann.test.backend.persistence.repositories.PlanRepository;
import de.redmann.test.backend.persistence.repositories.RoleRepository;
import de.redmann.test.backend.persistence.repositories.UserRepository;
import de.redmann.test.enums.PlansEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 17.10.16.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoleSet) {
        User localUser = userRepository.findByEmail(user.getEmail());
        if (localUser != null) {
            log.info("User with username {} and email {} already exist. Nothinf will be done.", user.getUsername(),
                    user.getEmail());
        } else {
            String encrpytedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encrpytedPassword);
            Plan plan = planRepository.findOne(plansEnum.getId());
            if (plan == null) {
                plan = planRepository.save(new Plan(plansEnum));
            }

            user.setPlan(plan);

            for (UserRole userRole : userRoleSet) {
                roleRepository.save(userRole.getRole());
            }

            user.getUserRoles().addAll(userRoleSet);
            localUser = userRepository.save(user);
        }

        return localUser;
    }


    public User findByUserName(String username) {
        return userRepository.findByUsername(username);
    }


    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email);
    }


    @Transactional
    public void updateUserPassword(long userId, String password) {
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        log.debug("Password updated successfully for user id{}", userId);

        Set<PasswordResetToken> resetTokens = passwordResetTokenRepository.findAllByUserId(userId);
        if (!resetTokens.isEmpty()) {
            passwordResetTokenRepository.delete(resetTokens);
        }
    }
}
