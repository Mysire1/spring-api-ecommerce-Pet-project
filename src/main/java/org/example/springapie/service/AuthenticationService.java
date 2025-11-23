package org.example.springapie.service;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.example.springapie.dtos.LoginUserDto;
import org.example.springapie.dtos.RegisterUserDto;
import org.example.springapie.dtos.VerifyUserDto;
import org.example.springapie.entities.User;
import org.example.springapie.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private EmailService emailService;

    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .name(input.getName())
                .email(input.getEmail())
                .password(passwordEncoder.encode(input.getPassword())) // хеш встановлюється у правильне поле
                .verificationCode(generateVerificationCode())
                .verificationExpiration(LocalDateTime.now().plusMinutes(15))
                .enabled(false)
                .build(); // новий юзер з шифруванням
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiration(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        User user = userRepository.findByEmail(input.getEmail()).orElseThrow(() -> new RuntimeException("User not found!"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified!");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())); //викликає AuthenticationManager.authenticate() для перевірки пароля.
        return user;
    }

    public void verifyUser(VerifyUserDto input) {
        Optional<User> user = userRepository.findByEmail(input.getEmail()); //по емейлу шукаємо код
        if (user.isPresent()) {
            User user1 = user.get();
            if (user1.getVerificationExpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Account verification expired!");
            }
            if (user1.getVerificationCode().equals(input.getVerificationCode())) { //якщо код активний, то активуємо аккаунт
                user1.setEnabled(true);
                user1.setVerificationCode(null);
                user1.setVerificationExpiration(null); // все в нуль кид
                userRepository.save(user1);
            } else {
                throw new RuntimeException("Account verification expired!");
            }
        } else {
            throw new RuntimeException("User not found!");
        }
    }

    public void resendVerificationCode(String email) { //перекидаємо НОВИЙ код на емейл знову
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User user1 = user.get();
            if (user1.isEnabled()) {
                throw new RuntimeException("Account is already verified!");
            }
            user1.setVerificationCode(generateVerificationCode());
            user1.setVerificationExpiration(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user1);
            userRepository.save(user1);
        } else {
            throw new NullPointerException("User not found!");
        }
    }

    public void sendVerificationEmail(User user) { // формує HTML-версію листа з кодом підтвердження
        String subject = "Account verification";
        String verificationCode = user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
        try {
            emailService.sendVerificationEmail(user.getEmail(), subject , htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = random.nextInt(1000000);
        return String.valueOf(code);
    }

}
