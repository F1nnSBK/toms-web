package com.toms.app.repository;

import com.toms.app.model.WebsiteConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteConfigRepository extends JpaRepository<WebsiteConfig, Long> {
}
