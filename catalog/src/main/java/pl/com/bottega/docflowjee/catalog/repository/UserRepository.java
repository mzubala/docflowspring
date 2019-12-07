package pl.com.bottega.docflowjee.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import pl.com.bottega.docflowjee.catalog.model.User;

public interface UserRepository extends CrudRepository<User, Long>,
    JpaSpecificationExecutor<User>, JpaRepository<User, Long> {

}
