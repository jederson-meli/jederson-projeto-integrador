package br.com.meli.projetointegrador.dto;

import br.com.meli.projetointegrador.model.Category;
import br.com.meli.projetointegrador.model.Section;
import br.com.meli.projetointegrador.service.WarehouseService;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SectionPostDTO {
    @NotNull(message = "Id missing.")
    private Long id;
    @NotNull(message = "Name missing.")
    private String name;
    @NotNull(message = "Category missing.")
    private String category;
    @NotNull(message = "Size missing.")
    private Integer size;
    @NotNull(message = "CurrentSize missing.")
    private Integer currentSize;
    @NotNull(message = "Warehouse missing.")
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

    public static List<SectionPostDTO> map(List<Section> sectionList) {
        return sectionList.stream().map(SectionPostDTO::map).collect(Collectors.toList());
    }
}

