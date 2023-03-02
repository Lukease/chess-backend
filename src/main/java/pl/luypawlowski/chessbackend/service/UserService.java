package pl.luypawlowski.chessbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.exception.UserExistsException;
import pl.luypawlowski.chessbackend.exception.WrongCredentialsException;
import pl.luypawlowski.chessbackend.model.user.UserDto;
import pl.luypawlowski.chessbackend.model.user.UserLogInRequest;
import pl.luypawlowski.chessbackend.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public Long saveUser(UserDto userDto) {
        User user = userDto.toDomain();
        if (usersRepository.existsByLogin(user.getLogin())) {
            throw new UserExistsException("User " + user.getLogin() + " exist");
        }
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        User save = usersRepository.save(user);

        return save.getId();
    }

    public UserDto getUserByLogin(String login) {
        return UserDto.fromDomain(usersRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("User not found!")));
    }

    @Transactional
    public UserDto logIn(UserLogInRequest userLogInRequest) {
        User user = findUserByLogin(userLogInRequest.getLogin());

        if (passwordEncoder.matches(userLogInRequest.getPassword(), user.getPassword())) {
            String token = LocalDateTime.now() + user.getPassword() + user.getLogin() + new Random().nextLong();
            LocalDateTime localDateTimePlusHour = LocalDateTime.now().plusHours(1);
            user.setActiveToken(token);
            user.setValidUtil(localDateTimePlusHour);
            return UserDto.fromDomain(user);
        } else {
            throw new WrongCredentialsException("Wrong data!");
        }
    }

    public List<UserDto> getAllUsers() {
        return usersRepository.findAll().stream().map(UserDto::fromDomain).collect(Collectors.toList());
    }

    @Transactional
    public UserDto updateUserLogin(User user,String newLogin) {
        user.setLogin(newLogin);

        return UserDto.fromDomain(user);
    }

    @Transactional
    public UserDto updateUserEmail(User user,String newEmail) {
        user.setEmail(newEmail);

        return UserDto.fromDomain(user);
    }

    private User findUserByLogin(String login) {
        return usersRepository.findByLogin(login).orElseThrow(() -> new WrongCredentialsException("Wrong data"));
    }

    public boolean findUserAuthorizationToken(String authorization, String login) {
        User user = usersRepository.findByLogin(login).orElseThrow(() -> new WrongCredentialsException("Wrong data"));

        return user.getActiveToken().equals(authorization) && user.getValidUtil().isAfter(LocalDateTime.now());
    }

    public boolean findUserAuthorizationTokenById(String authorization, Long id) {
        User user = usersRepository.findById(id).orElseThrow(() -> new WrongCredentialsException("Wrong data"));

        return user.getActiveToken().equals(authorization) && user.getValidUtil().isBefore(LocalDateTime.now());
    }

    public User findUserByAuthorizationToken(String activeToken) {
        Optional<User> user = usersRepository.findByActiveToken(activeToken);

        if (user.isPresent() && user.get().getValidUtil().isAfter(LocalDateTime.now())) {
            return user.get();
        } else {
            throw new WrongCredentialsException("No active Token found");
        }
    }
}
