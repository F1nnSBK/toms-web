package com.toms.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "website_config")
public class WebsiteConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;
    private String description;
    private String keywords;

    private String googleAnalytics;
    private String googleTagManager;

    public WebsiteConfig(String title,
                         String description,
                         String keywords,
                         String googleAnalytics,
                         String googleTagManager) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.googleAnalytics = googleAnalytics;
        this.googleTagManager = googleTagManager;
    }

    public WebsiteConfig(String title) {
        this.title = title;
    }
}