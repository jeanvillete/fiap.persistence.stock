package fiap.sample.login.user.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

interface UserRepository extends MongoRepository<User, String> {

    Optional<Integer> countByLoginAndType(String login, UserType type);

    Optional<User> findByLoginAndType(String login, UserType type);

}
