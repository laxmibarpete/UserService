package com.example.userservice.controllers;


import com.example.userservice.dtos.LogoutRequestDto;
import com.example.userservice.dtos.SignupRquestDto;
import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Token login(@RequestBody SignupRquestDto request){
        return this.userService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/")
    public User signUp(@RequestBody SignupRquestDto requestDto){
        String name = requestDto.getName();
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        User user = this.userService.signUp(name,email,password);
        return user;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(@RequestBody LogoutRequestDto request){
        this.userService.logout(request.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public User validateToken( @PathVariable("token") @NonNull String token){
        return this.userService.validateToken(token);
    }
}
