package com.welton.agregadorinvestimentos.service;

import com.welton.agregadorinvestimentos.controller.dto.CreateAccountDto;
import com.welton.agregadorinvestimentos.controller.dto.CreateUserDto;
import com.welton.agregadorinvestimentos.controller.dto.UpdateUserDto;
import com.welton.agregadorinvestimentos.entity.Account;
import com.welton.agregadorinvestimentos.entity.BillingAddress;
import com.welton.agregadorinvestimentos.entity.User;
import com.welton.agregadorinvestimentos.repository.AccountRepository;
import com.welton.agregadorinvestimentos.repository.BillingAddressRepository;
import com.welton.agregadorinvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    // Imprementação de class
    private final UserRepository repository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository repository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    public UUID createUser(CreateUserDto createUserDto) {
        var entity = new User(null,
                createUserDto.userName(),
                createUserDto.email(),
                createUserDto.password(),
                null,
                null);
        var userSaved = repository.save(entity);
        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return repository.findById(UUID.fromString(userId));
    }

    public List<User> listUsers() {
        return repository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto) {
        var id = UUID.fromString(userId);
        var userEntity = repository.findById(id);

        if (userEntity.isPresent()) {
            var user = userEntity.get();
            if (updateUserDto.userName() != null) {
                user.setUserName(updateUserDto.userName());
            }

            if (updateUserDto.email() != null) {
                user.setEmail(updateUserDto.email());
            }

            if (updateUserDto.password() != null) {
                user.setPassword(updateUserDto.password());
            }
            repository.save(user);
        }
    }

    public void deleteById(String userId) {
        var id = UUID.fromString(userId);
        var userExists = repository.existsById(id);

        if (userExists) {
            repository.deleteById(id);
        }
    }

    public void createAccount(String userId, CreateAccountDto accountDto) {
        var user = repository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var account = new Account(
                null,
                user,
                null,
                accountDto.description(),
                new ArrayList<>()
        );

        var accountCreated = accountRepository.save(account);

        var billingAddress = new BillingAddress(
                accountCreated.getAccountId(),
                account,
                accountDto.street(),
                accountDto.number()
        );

        billingAddressRepository.save(billingAddress);
    }
}
