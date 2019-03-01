package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "answer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "text"
        })
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Answer {

    public static final String FIELD_TEXT = "text";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "answer_id")
    @SequenceGenerator(name = "answer_id", sequenceName = "answer_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "text", unique = true)
    @NotNull
    @Size(max = 100)
    private String text;

}
