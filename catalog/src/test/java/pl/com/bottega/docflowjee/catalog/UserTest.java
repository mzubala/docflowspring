package pl.com.bottega.docflowjee.catalog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;
import pl.com.bottega.docflowjee.catalog.model.Address;
import pl.com.bottega.docflowjee.catalog.model.User;
import pl.com.bottega.docflowjee.catalog.repository.UserRepository;
import pl.com.bottega.docflowjee.catalog.repository.UserSpecification;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private TransactionTemplate tt;

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSpec() {
        for (int i = 0; i < 5; i++) {
            User user = new User();
            int j = i;
            tt.execute(o -> {
                if (j == 0) {
                    Address address = new Address();
                    em.persist(address);
                    user.setAddress(address);
                }
                em.persist(user);
                return null;
            });
        }

        List<User> users = userRepository.findAll(new UserSpecification());

        users.forEach(u -> u.getAddress());
    }

}
