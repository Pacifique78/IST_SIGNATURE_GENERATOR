package com.istafrica.signature.service.auth;

import com.istafrica.signature.config.JwtService;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.domain.enums.UserRole;
import com.istafrica.signature.dto.auth.*;
import com.istafrica.signature.exception.CustomExceptions.*;
import com.istafrica.signature.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final JavaMailSender mailSender;

	@Value("${app.frontend.url}")
	private String frontendUrl;

	@Transactional
	public AuthResponse signup(SignupRequest request) {
		try {
			log.debug("Starting signup process for email: {}", request.getEmail());
			if (userRepository.existsByEmail(request.getEmail())) {
				log.debug("Email already exists: {}", request.getEmail());
				throw new ResourceConflictException("Email already exists");
			}
			log.debug("Creating new user with email: {}", request.getEmail());
			User user = User.builder()
					.name(request.getName())
					.email(request.getEmail())
					.password(passwordEncoder.encode(request.getPassword()))
					.phoneNumber(request.getPhoneNumber())
					.role(UserRole.USER)
					.verified(false)
					.build();

			log.debug("Saving user to database");
			user = userRepository.save(user);

			// Send verification email
			log.debug("Sending verification email");
			try {
				sendVerificationEmail(user);
			} catch (Exception e) {
				log.error("Failed to send verification email", e);
				throw new RuntimeException("Failed to send verification email: " + e.getMessage());
			}
			log.debug("Signup completed successfully");
			return AuthResponse.builder()
					.email(user.getEmail())
					.message("User registered successfully. Please check your email for verification.")
					.build();
		} catch (Exception e) {
			log.error("Error during signup process", e);
			throw e;
		}
	}

	@Transactional
	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

		if (!user.isVerified()) {
			throw new UnauthorizedException("Email not verified");
		}
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							request.getEmail(),
							request.getPassword()));

			String token = jwtService.generateToken(user);

			return AuthResponse.builder()
					.token(token)
					.email(user.getEmail())
					.message("Login successful")
					.build();
		} catch (BadCredentialsException e) {
			throw new UnauthorizedException("Invalid email or password");
		}
	}

	@Transactional
	public EmailVerificationResponse verifyEmail(EmailVerificationRequest request) {
		try {
			Claims claims = jwtService.verifyToken(request.getToken());

			// Verify token purpose
			if (!"email_verification".equals(claims.get("purpose"))) {
				throw new ValidationException("Invalid verification token");
			}

			String email = claims.getSubject();
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new ResourceNotFoundException("User not found"));

			if (user.isVerified()) {
				throw new ValidationException("Email already verified");
			}

			user.setVerified(true);
			userRepository.save(user);

			return EmailVerificationResponse.builder()
					.message("Email verified successfully")
					.email(user.getEmail())
					.build();

		} catch (ExpiredJwtException e) {
			throw new ValidationException("Verification link has expired");
		} catch (SignatureException e) {
			throw new ValidationException("Invalid verification link");
		}
	}

	@Transactional
	public void resendVerificationEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		if (user.isVerified()) {
			throw new ValidationException("Email already verified");
		}

		sendVerificationEmail(user);
	}

	private void sendVerificationEmail(User user) {
		try {
			log.debug("Generating verification token for user: {}", user.getEmail());
			String token = generateVerificationToken(user);
			String verificationLink = frontendUrl.endsWith("/")
					? frontendUrl + "auth/verify-email?token=" + token
					: frontendUrl + "/auth/verify-email?token=" + token;
			log.debug("Creating email message");
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setTo(user.getEmail());
			helper.setSubject("Verify Your Email Address");
			helper.setText(createVerificationEmailContent(verificationLink), true);

			log.debug("Sending email to: {}", user.getEmail());
			mailSender.send(message);
			log.debug("Verification email sent successfully");
		} catch (MessagingException e) {
			log.error("Failed to send verification email", e);
			throw new RuntimeException("Failed to send verification email");
		}
	}

	private String generateVerificationToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("purpose", "email_verification");
		claims.put("user_id", user.getId().toString());

		return jwtService.generateToken(
				claims,
				user.getEmail(),
				Duration.ofHours(24));
	}

	private String createVerificationEmailContent(String verificationLink) {
		return String.format(
				"""
						<html>
						    <body>
						        <h2>Verify Your Email Address</h2>
						        <p>Thank you for registering. Please click the link below to verify your email address:</p>
						        <p><a href="%s">Verify Email</a></p>
						        <p>If you didn't create this account, please ignore this email.</p>
						        <p>This link will expire in 24 hours.</p>
						    </body>
						</html>
						""",
				verificationLink);
	}
}