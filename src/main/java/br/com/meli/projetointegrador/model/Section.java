package br.com.meli.projetointegrador.model;

import lombok.*;

import javax.persistence.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;
    private Integer size;
    private Integer currentSize;


    @ManyToOne
    private Warehouse warehouse;

    @OneToMany(mappedBy = "section",cascade = CascadeType.ALL)
    private List<Batch> batchList;
}



