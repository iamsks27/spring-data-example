package com.example.services;

import com.example.jparepository.UserRepository;
import com.example.models.UserDao;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void persist(final UserDao userDao) {
        //convert the user to byte array
        final byte[] serializedUser = convert(userDao);
        //persist
    }

    private byte[] convert(final UserDao userDao) {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (final ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(userDao);
            return outputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}