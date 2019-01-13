package me.freelife.loc.address.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Keyword {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String word;

    private int searchCount;
}