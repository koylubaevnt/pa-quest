package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Blob;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_content_id")
    @SequenceGenerator(name = "gen_content_id", sequenceName = "content_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "content")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob content;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    @Size(max = 60)
    private ContentType type;

    @Column(name = "extension")
    @NotNull
    @Size(max = 10)
    private String ext;

    @Column(name = "name")
    @NotNull
    @Size(max = 1024)
    private String fileName;

}