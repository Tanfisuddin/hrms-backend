package com.imw.commonmodule.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "document type is required")
    private String documentType;

    @NotBlank(message = "Name on Document is required")
    private String nameOnDocument;

    @NotBlank(message = "Number on Document is required")
    private String numberOnDocument;

    @NotBlank(message = "Document Medial Url is required")
    private String mediaUrl;

}
