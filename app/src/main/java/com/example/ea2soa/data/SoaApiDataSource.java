package com.example.ea2soa.data;

import com.example.ea2soa.data.model.User;

import java.io.IOException;

public class SoaApiDataSource extends LoginDataSource {

    public Result<User> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            User fakeUser =
                    new User(
                            "Jane",
                            "Doe",
                            1312312312,
                            12312323,
                            "pepe@gmail.com"
                    );
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

}
