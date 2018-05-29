package com.example.services;

import com.example.jparepository.UserRepository;
import com.example.models.User;
import com.example.models.UserDao;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User persist(final UserDao userDao) {
        //convert the user to byte array
        final byte[] serializedUser = convert(userDao);
        return this.userRepository.save(new User(serializedUser));
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

    public List<UserDao> getAllUsers() {
        return this.userRepository.findAll()
                .stream()
                .map(this::toUserDao)
                .collect(Collectors.toList());
    }

    private UserDao toUserDao(final User user) {
        final byte[] userDao = user.getUserDao();
        return convert(userDao);
    }

    private UserDao convert(final byte[] userDao) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(userDao);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return (UserDao) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}