package com.crm.seed;

import com.crm.model.Company;
import com.crm.model.User;
import com.crm.model.Contact;
import com.crm.repository.CompanyRepository;
import com.crm.repository.UserRepository;
import com.crm.repository.ContactRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final ContactRepository contactRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public DataSeeder(UserRepository userRepository, CompanyRepository companyRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed users (if needed)
        long usersCount = userRepository.count();
        if (usersCount == 0) {
            int seedUsers = 20;
            List<User> users = IntStream.range(0, seedUsers)
                    .mapToObj(i -> {
                        User u = new User();
                        u.setFirstName(faker.name().firstName());
                        u.setLastName(faker.name().lastName());
                        u.setEmail(faker.internet().emailAddress());
                        return u;
                    })
                    .collect(Collectors.toList());
            userRepository.saveAll(users);
            logger.info("Seeded {} users into MongoDB.", seedUsers);
        } else {
            logger.info("Users already present ({}). Skipping user seeding.", usersCount);
        }

        // Seed companies
        long companiesCount = companyRepository.count();
        List<Company> companies = new ArrayList<>();
        if (companiesCount == 0) {
            int seedCompanies = 10;
            companies = IntStream.range(0, seedCompanies)
                    .mapToObj(i -> {
                        Company c = new Company();
                        c.setName(faker.company().name());
                        c.setCnpj(faker.number().digits(14));
                        c.setPhone(faker.phoneNumber().phoneNumber());
                        c.setWebsite("https://" + faker.internet().domainName());
                        // address minimal
                        com.crm.model.Address addr = new com.crm.model.Address();
                        addr.setStreet(faker.address().streetName());
                        addr.setNumber(faker.address().buildingNumber());
                        addr.setCity(faker.address().city());
                        addr.setState(faker.address().stateAbbr());
                        addr.setPostalCode(faker.address().zipCode());
                        c.setAddress(addr);
                        c.setStatus(com.crm.model.CompanyStatus.ATIVO);
                        return c;
                    }).collect(Collectors.toList());
            companyRepository.saveAll(companies);
            logger.info("Seeded {} companies into MongoDB.", companies.size());
        } else {
            companies = companyRepository.findAll();
            logger.info("Companies already present ({}). Skipping company seeding.", companiesCount);
        }

        // Seed contacts referencing companies
        long contactsCount = contactRepository.count();
        if (contactsCount == 0) {
            int seedContacts = 200;
            List<String> companyIds = companies.stream().map(Company::getId).collect(Collectors.toList());
            if (companyIds.isEmpty()) {
                logger.warn("No companies found to associate contacts with. Skipping contact seeding.");
            } else {
                List<Contact> contacts = IntStream.range(0, seedContacts)
                        .mapToObj(i -> {
                            Contact c = new Contact();
                            c.setFirstName(faker.name().firstName());
                            c.setLastName(faker.name().lastName());
                            c.setEmail(faker.internet().emailAddress());
                            c.setJobTitle(faker.job().title());
                            // assign random company
                            c.setCompanyId(companyIds.get(random.nextInt(companyIds.size())));
                            c.setLinkedinProfile("https://www.linkedin.com/in/" + faker.name().username());
                            return c;
                        }).collect(Collectors.toList());
                contactRepository.saveAll(contacts);
                logger.info("Seeded {} contacts into MongoDB.", seedContacts);
            }
        } else {
            logger.info("Contacts already present ({}). Skipping contact seeding.", contactsCount);
        }
    }
}
