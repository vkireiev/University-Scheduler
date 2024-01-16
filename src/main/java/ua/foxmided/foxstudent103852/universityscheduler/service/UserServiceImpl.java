package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.repository.UserRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${err.msg.service.jdbc.person.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.person.update.fail}")
    private String updateFailMessage;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Optional<Person> get(Long id) {
        try {
            return userRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return userRepository.existsById(id);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            return userRepository.existsByUsernameIgnoringCase(username);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, username), e);
        }
    }

    @Transactional
    @Override
    public boolean updateEmail(Long id, String email) {
        try {
            return userRepository.updateEmail(id, email) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updatePassword(Long id, String password) {
        try {
            return userRepository.updatePassword(id, passwordEncoder.encode(password)) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

    @Transactional
    @Override
    public boolean updateProfile(Long id, Person person) {
        try {
            return userRepository.updateProfile(id,
                    person.getFirstName(), person.getLastName(),
                    person.getBirthday(), person.getPhoneNumber()) > 0;
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(updateFailMessage, "ID = " + id), e);
        }
    }

}
