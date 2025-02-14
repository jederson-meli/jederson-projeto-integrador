package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.dto.SectionPostDTO;
import br.com.meli.projetointegrador.service.SectionService;
import br.com.meli.projetointegrador.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<SectionPostDTO> postSection(@Valid @RequestBody SectionPostDTO sectionPostDTO) {
        return new ResponseEntity<>(SectionPostDTO.map(sectionService.save(SectionPostDTO.map(sectionPostDTO, warehouseService))), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<List<SectionPostDTO>> findAllSections() {
        return new ResponseEntity<>(SectionPostDTO.map(sectionService.findAll()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<SectionPostDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(SectionPostDTO.map(sectionService.findById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STOCK_MANAGER')")
    public ResponseEntity<SectionPostDTO> updateSection(@PathVariable Long id, @Valid @RequestBody SectionPostDTO sectionPostDTO) {
        return new ResponseEntity<>(sectionService.updateSection(id, sectionPostDTO), HttpStatus.CREATED);
    }
}
