package com.assessment.library_managment_system.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String name;

        private String description;

        @ManyToOne
        @JoinColumn(name = "parent_id")
        private Category parent;

        @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
        private Set<Category> children = new HashSet<>();

        @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
        private Set<Book> books = new HashSet<>();
}