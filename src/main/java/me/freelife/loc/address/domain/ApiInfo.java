package me.freelife.loc.address.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class ApiInfo {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer apiId;

    @Enumerated(EnumType.STRING)
    private ApiType apiType;
}
