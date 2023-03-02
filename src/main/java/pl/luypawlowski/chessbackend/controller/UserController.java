package pl.luypawlowski.chessbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.exception.UserExistsException;
import pl.luypawlowski.chessbackend.exception.WrongCredentialsException;
import pl.luypawlowski.chessbackend.model.user.*;
import pl.luypawlowski.chessbackend.service.UserService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity saveUser(@RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.saveUser(userDto));
        } catch (UserExistsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity getUserByLogin(@RequestParam("login") String login) {
        try {
            return ResponseEntity.ok(userService.getUserByLogin(login));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(login + " not found!");
        }
    }

    @GetMapping("/get-all")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/log-in")
    public ResponseEntity logIn(@RequestBody UserLogInRequest userLogInRequest) {
        try {
            return ResponseEntity.ok(userService.logIn(userLogInRequest));
        } catch (WrongCredentialsException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/new-login")
    public UserDto editUserLogin(@RequestBody NewLoginRequest newLoginRequest,
                                 @RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return userService.updateUserLogin(user, newLoginRequest.getLogin());
    }

    @PutMapping("/new-email")
    public UserDto editUserEmail(@RequestBody NewEmailRequest newEmailRequest,
                                 @RequestHeader("Authorization") String authorization) {
        User user = userService.findUserByAuthorizationToken(authorization);

        return userService.updateUserEmail(user, newEmailRequest.getEmail());
    }
}
