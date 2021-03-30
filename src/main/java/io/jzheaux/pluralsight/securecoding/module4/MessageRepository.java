package io.jzheaux.pluralsight.securecoding.module4;

import io.jzheaux.pluralsight.securecoding.module3.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
}
