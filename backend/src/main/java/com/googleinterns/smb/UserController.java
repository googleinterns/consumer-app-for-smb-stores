package com.googleinterns.smb;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    @Autowired
    private FireStoreInstance fireStoreInstance;

    @PostMapping(value = "/userDetails")
    public void placeOrder(@RequestBody User userDetails) throws ExecutionException, InterruptedException {
        User user = new User(userDetails.getUserId(), userDetails.getUserAddress(), userDetails.getUserContactNo(),userDetails.getUserName());

        fireStoreInstance.updateUserDetails(user);
    }

    @GetMapping(value = "/getUserDetails")
    public User getUserDetails(@RequestParam String userId) throws ExecutionException, InterruptedException {
        return fireStoreInstance.getUserDetails(userId);
    }
}