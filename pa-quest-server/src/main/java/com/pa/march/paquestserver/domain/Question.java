package com.pa.march.paquestserver.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "question", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "text"
        })
})
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_question_id")
    @SequenceGenerator(name = "gen_question_id", sequenceName = "question_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "text", unique = true)
    @NotNull
    @Size(max = 255)
    private String text;

//    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
//    @JoinColumn(name = "content_id", nullable = true)
//    private Content content;

    @Column(name = "youtube_video_url", nullable = false)
    private String youtubeVideoId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "correct_answer_id", nullable = false)
    private Answer correctAnswer;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
        fetch = FetchType.LAZY)
    @JoinTable(name = "question_answer",
        joinColumns = { @JoinColumn(name = "question_id") },
        inverseJoinColumns = { @JoinColumn(name = "answer_id") },
        uniqueConstraints = {
            @UniqueConstraint(name = "id", columnNames = {"question_id", "answer_id"})
        })
    private List<Answer> answers;

}
