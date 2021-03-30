package io.jzheaux.pluralsight.securecoding.module5;

import io.jzheaux.pluralsight.securecoding.module4.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends CrudRepository<Music, Long> {
}
