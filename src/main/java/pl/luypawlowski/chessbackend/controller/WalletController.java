package pl.luypawlowski.chessbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.luypawlowski.chessbackend.entities.User;
import pl.luypawlowski.chessbackend.model.user.NewLoginRequest;
import pl.luypawlowski.chessbackend.model.user.UserDto;
import pl.luypawlowski.chessbackend.model.wallet.WalletDto;
import pl.luypawlowski.chessbackend.service.UserService;
import pl.luypawlowski.chessbackend.service.WalletService;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping(value = "/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;

    @GetMapping
    public WalletDto getUserWallet(@RequestHeader("Authorization") String authorization){
        User user = userService.findUserByAuthorizationToken(authorization);

        return walletService.getUserWallet(user);
    }
}
