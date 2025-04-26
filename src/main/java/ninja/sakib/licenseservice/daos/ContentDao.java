package ninja.sakib.licenseservice.daos;

import ninja.sakib.licenseservice.models.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentDao extends JpaRepository<Content, String> {
}
