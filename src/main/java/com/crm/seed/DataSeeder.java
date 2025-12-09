package com.crm.seed;

import com.crm.model.User;
import com.crm.repository.UserRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long count = userRepository.count();
        if (count > 0) {
            logger.info("Database already contains {} users — skipping seeding.", count);
            return;
        }

        int seedSize = 100; // número de registros a inserir
        List<User> users = IntStream.range(0, seedSize)
                .mapToObj(i -> {
                    User u = new User();
                    u.setFirstName(faker.name().firstName());
                    u.setLastName(faker.name().lastName());
                    u.setEmail(faker.internet().emailAddress());
                    return u;
                })
                .collect(Collectors.toList());

        userRepository.saveAll(users);
        logger.info("Seeded {} users into MongoDB.", seedSize);
    }
}
