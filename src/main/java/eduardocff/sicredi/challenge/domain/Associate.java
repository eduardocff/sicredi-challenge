package eduardocff.sicredi.challenge.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Associate {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private Long cpf;

    private String name;

    @OneToMany(mappedBy = "associate")
    private Set<Vote> votings;
}
