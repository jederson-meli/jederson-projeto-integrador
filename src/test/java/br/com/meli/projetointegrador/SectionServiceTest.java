package br.com.meli.projetointegrador;

import br.com.meli.projetointegrador.dto.SectionPostDTO;
import br.com.meli.projetointegrador.exception.SectionIsAlreadyInUse;
import br.com.meli.projetointegrador.model.*;
import br.com.meli.projetointegrador.repository.SectionRepository;

import br.com.meli.projetointegrador.exception.InexistentSectionException;
import br.com.meli.projetointegrador.model.Batch;
import br.com.meli.projetointegrador.model.Category;
import br.com.meli.projetointegrador.model.Section;
import br.com.meli.projetointegrador.model.Warehouse;

import br.com.meli.projetointegrador.service.SectionService;
import br.com.meli.projetointegrador.service.SectionServiceImpl;
import br.com.meli.projetointegrador.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Optional;


public class SectionServiceTest {

    private SectionService sectionService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private WarehouseService warehouseService;

    @BeforeEach
    private void initializeSectionService() {
        MockitoAnnotations.openMocks(this);
        this.sectionService = new SectionServiceImpl(sectionRepository, warehouseService);
    }

    private List<Batch> generateBatchFreshList(Section section) {

        return Arrays.asList(
                new Batch(1L, 25.5, 10.5, 20, 20, LocalDate.of(2022,1,1), LocalDateTime.of(2022, 1, 1,0,0,0,0), LocalDate.of(2022, 05, 27), new Product(), new InboundOrder(), section),
                new Batch(1L, 25.5, 11.5, 20, 20, LocalDate.of(2022,1,1), LocalDateTime.of(2022, 1, 1,0,0,0,0), LocalDate.of(2022, 06, 27), new Product(), new InboundOrder(), section),
                new Batch(1L, 25.5, 12.5, 20, 20, LocalDate.of(2022,1,1), LocalDateTime.of(2022, 1, 1,0,0,0,0), LocalDate.of(2022, 07, 27), new Product(), new InboundOrder(), section)
        );
    }

    private List<Batch> generateBatchFrozenList(Section section) {

        return Arrays.asList(
                new Batch(1L, 25.5, -10.5, 20, 20, LocalDate.of(2022,1,1), LocalDateTime.of(2022, 1, 1,0,0,0,0), LocalDate.of(2022, 05, 27), new Product(), new InboundOrder(), section),
                new Batch(1L, 25.5, -11.5, 20, 20, LocalDate.of(2022,1,1), LocalDateTime.of(2022, 1, 1,0,0,0,0), LocalDate.of(2022, 06, 27), new Product(), new InboundOrder(), section)
        );
    }

    private List<Section> generateSectionList() {
        return Arrays.asList(
                Section.builder().id(1L).name("Section 1").build(),
                Section.builder().id(2L).name("Section 2").build(),
                Section.builder().id(3L).name("Section 3").build()
        );
    }

    @Test
    public void saveTest() {
        Section section = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList());
        Mockito.when(sectionRepository.save(Mockito.any())).thenReturn(section);

        assertEquals(section, sectionService.save(section));
    }

    @Test
    public void findByIdTest() {
        Mockito.when(sectionRepository.findById(1L))
                .thenReturn(java.util.Optional.of(new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList(new Batch()))));
        Section section = sectionService.findById(1L);

        assertEquals("Section 1", section.getName());
    }

    @Test
    public void findAllTest() {
        List<Section> sectionList = generateSectionList();
        Mockito.when(sectionRepository.findAll()).thenReturn(sectionList);

        assertEquals(sectionList, sectionService.findAll());
    }

    @Test
    public void updateSectionTest() {
        Section section = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList());
        SectionPostDTO sectionPostDTO = SectionPostDTO.builder().id(1l).name("Section 1 updated").category("FROZEN").build();
        Mockito.when(sectionRepository.findById(1L))
                .thenReturn(java.util.Optional.of(section));

        sectionService.updateSection(1L, sectionPostDTO);

        assertEquals(section.getName(), sectionPostDTO.getName());
    }

    @Test
    public void updateSectionTestFailed() {
        Section section = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList(new Batch()));
        SectionPostDTO sectionPostDTO = SectionPostDTO.builder().id(1l).name("Section 1 updated").category("FROZEN").build();
        Mockito.when(sectionRepository.findById(1L))
                .thenReturn(java.util.Optional.of(section));

        assertThrows(SectionIsAlreadyInUse.class, () -> sectionService.updateSection(1L, sectionPostDTO));
    }

    @Test
    public void checkBatchStockDueDateTest() {
        Section section = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList(new Batch()));
        List<Batch> batchList = generateBatchFreshList(section);
        section.setBatchList(batchList);
        Mockito.when(sectionRepository.findById(1L))
                .thenReturn(java.util.Optional.of(section));

        assertEquals(1, sectionService.checkBatchStockDueDate(1L, 50).size());
        assertEquals(2, sectionService.checkBatchStockDueDate(1L, 80).size());
        assertEquals(3, sectionService.checkBatchStockDueDate(1L, 110).size());
    }

    @Test
    public void checkBatchStockDueDateByCategory() {
        Section section1 = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Arrays.asList(new Batch()));
        Section section2 = new Section(2L, "Section 2", Category.FROZEN, 10, 10, new Warehouse(), Arrays.asList(new Batch()));
        List<Batch> batchList1 = generateBatchFreshList(section1);
        section1.setBatchList(batchList1);
        List<Batch> batchList2 = generateBatchFrozenList(section2);
        section2.setBatchList(batchList2);
        Mockito.when(sectionRepository.findAll())
                .thenReturn(Arrays.asList(section1, section2));

        assertEquals(1, sectionService.checkBatchStockDueDateByCategory(50, "FRESH", "asc").size());
        assertEquals(2, sectionService.checkBatchStockDueDateByCategory(110, "FROZEN", "asc").size());
        assertEquals(10.5, sectionService.checkBatchStockDueDateByCategory(110, "FRESH", "asc").get(0).getMinTemperature());
        assertEquals(12.5, sectionService.checkBatchStockDueDateByCategory(110, "FRESH", "desc").get(0).getMinTemperature());
        assertEquals(-10.5, sectionService.checkBatchStockDueDateByCategory(110, "FROZEN", "asc").get(0).getMinTemperature());
        assertEquals(-11.5, sectionService.checkBatchStockDueDateByCategory(110, "FROZEN", "desc").get(0).getMinTemperature());
    }

    @Test
    public void inexistentSectionException(){
        Mockito.when(sectionRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(InexistentSectionException.class, () -> sectionService.findById(1L));
    }

    @Test
    public void updateCurrentSizeTest(){
        Section section = new Section(1L, "Section 1", Category.FRESH, 10, 10, new Warehouse(), Collections.singletonList(new Batch()));

        Mockito.when(sectionRepository.findById(Mockito.any()))
                .thenReturn(java.util.Optional.of(section));
        Mockito.when(sectionRepository.save(Mockito.any()))
                .thenReturn(section);

        sectionService.updateCurrentSize(2, 1L, false);

        assertEquals(8, section.getCurrentSize());

        sectionService.updateCurrentSize(2, 1L, true);

        assertEquals(10, section.getCurrentSize());
    }

}
