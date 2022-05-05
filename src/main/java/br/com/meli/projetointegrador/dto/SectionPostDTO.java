package br.com.meli.projetointegrador.dto;

import br.com.meli.projetointegrador.model.Category;
import br.com.meli.projetointegrador.model.Section;
import br.com.meli.projetointegrador.service.WarehouseService;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionPostDTO {
    private Long id;
    private String name;
    private String category;
    private Integer size;
    private Integer currentSize;
    private Long warehouse;

    public static Section map(SectionPostDTO sectionPostDTO, WarehouseService warehouseService) {
        Category category = Category.valueOf(sectionPostDTO.getCategory());

        return Section.builder()
                .id(sectionPostDTO.getId())
                .name(sectionPostDTO.getName())
                .category(category)
                .size(sectionPostDTO.getSize())
                .currentSize(sectionPostDTO.getCurrentSize())
                .warehouse(warehouseService.findById(sectionPostDTO.warehouse))
                .build();
    }

    public static SectionPostDTO map(Section section) {
        return new SectionPostDTO(section.getId(), section.getName(), section.getCategory().toString(), section.getSize(), section.getCurrentSize(), section.getWarehouse().getId());
    }
}

