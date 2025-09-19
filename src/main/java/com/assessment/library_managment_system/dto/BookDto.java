package com.assessment.library_managment_system.dto;
import lombok.Data;
import java.util.Set;

@Data
public class BookDto {
        private Long id;
        private String title;
        private String isbn;
        private String edition;
        private Integer publicationYear;
        private String language;
        private String summary;
        private String coverImageUrl;
        private Integer totalCopies;
        private Integer availableCopies;
        private Set<AuthorDto> authors;
        private PublisherDto publisher;
        private Set<CategoryDto> categories;

        @Data
        public static class AuthorDto {
                private Long id;
                private String firstName;
                private String lastName;
                private String biography;
        }

        @Data
        public static class PublisherDto {
                private Long id;
                private String name;
                private String address;
                private String contactEmail;
                private String contactPhone;
        }

        @Data
        public static class CategoryDto {
                private Long id;
                private String name;
                private String description;
                private Long parentId;
        }
}
