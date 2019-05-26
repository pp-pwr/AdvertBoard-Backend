package ppztw.AdvertBoard.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ppztw.AdvertBoard.Exception.BadRequestException;
import ppztw.AdvertBoard.Model.User.AuthProvider;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Payload.ApiResponse;
import ppztw.AdvertBoard.Payload.AuthResponse;
import ppztw.AdvertBoard.Payload.LoginRequest;
import ppztw.AdvertBoard.Payload.SignUpRequest;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.Security.OnRegistrationCompleteEvent;
import ppztw.AdvertBoard.Security.TokenProvider;
import ppztw.AdvertBoard.User.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        if (userRepository.existsByName(signUpRequest.getName())) {
            throw new BadRequestException("Username already in use.");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Profile profile = new Profile();
        profile.setVisibleName(signUpRequest.getName());
        user.setProfile(profile);

        User result = userRepository.save(user);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(result));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    @PostMapping("/signupConfirm")
    public ResponseEntity<?> confirmUser(@Valid @NotNull @NotBlank @RequestParam String token) {
        userService.confirmToken(token);

        return ResponseEntity.ok(new ApiResponse(true, "Mail confirmed"));
    }

}
