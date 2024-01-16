package ua.foxmided.foxstudent103852.universityscheduler.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ua.foxmided.foxstudent103852.universityscheduler.exception.DataProcessingException;
import ua.foxmided.foxstudent103852.universityscheduler.model.Person;
import ua.foxmided.foxstudent103852.universityscheduler.repository.PersonRepository;
import ua.foxmided.foxstudent103852.universityscheduler.service.interfaces.PersonService;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    @Value("${err.msg.service.jdbc.person.get.fail}")
    private String getFailMessage;

    @Value("${err.msg.service.jdbc.person.getall.fail}")
    private String getAllFailMessage;

    @Value("${err.msg.service.jdbc.person.count.fail}")
    private String countFailMessage;

    private final PersonRepository personRepository;

    @Override
    public Optional<Person> get(Long id) {
        try {
            return personRepository.findById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public List<Person> findAll() {
        try {
            return personRepository.findAll();
        } catch (DataAccessException e) {
            throw new DataProcessingException(getAllFailMessage, e);
        }
    }

    @Override
    public long count() {
        try {
            return personRepository.count();
        } catch (DataAccessException e) {
            throw new DataProcessingException(countFailMessage, e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try {
            return personRepository.existsById(id);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, id), e);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            return personRepository.existsByUsernameIgnoringCase(username);
        } catch (DataAccessException e) {
            throw new DataProcessingException(
                    String.format(getFailMessage, username), e);
        }
    }

}
