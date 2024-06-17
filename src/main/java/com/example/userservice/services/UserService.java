package com.example.userservice.services;


import com.example.userservice.models.Token;
import com.example.userservice.models.User;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    public User signUp(String fullName, String email, String password){

        User user = new User();
        user.setName(fullName);
        user.setEmail(email);
        user.setHashPassword(this.bCryptPasswordEncoder.encode(password));
        User u = this.userRepository.save(user);
        return u;
    }

    public Token login(String email, String password){

        Optional<User> optionalUser = this.userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            // throw user not exist exception
            return null;
        }

        User user = optionalUser.get();

        if (!this.bCryptPasswordEncoder.matches(password, user.getHashPassword())){
            // throw password not match exception
            return null;
        }


        LocalDate thirtyDaysLater = LocalDate.now().plusDays( 30 );
        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setUser(user);
        token.setExpiryAt(expiryDate);
        token.setValue(RandomStringUtils.randomAlphabetic(128));
        return this.tokenRepository.save(token);
    }

    public void logout(String token){
        Optional<Token> tkn = this.tokenRepository.findByValueAndIsDeleted(token,false);
        if (tkn.isEmpty()){
            //throw TokenNotFoundORExprieEXception
            return;
        }

        Token tkn1 = tkn.get();
        tkn1.setIsDeleted(true);
        this.tokenRepository.save(tkn1);
        return;
    }

    public User validateToken(String token){
        Optional<Token> tkn = this.tokenRepository.findByValueAndIsDeletedAndExpiryAtGreaterThan(token,false, new Date());
        if (tkn.isEmpty()){
            return null;
        }
        return tkn.get().getUser();
    }

}
