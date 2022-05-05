package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.dto.SectionPostDTO;
import br.com.meli.projetointegrador.model.Section;
import br.com.meli.projetointegrador.model.Warehouse;
import br.com.meli.projetointegrador.service.SectionService;
import br.com.meli.projetointegrador.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fresh-products/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<SectionPostDTO> postSection(@RequestBody SectionPostDTO sectionPostDTO) {
        return new ResponseEntity<>(SectionPostDTO.map(sectionService.save(SectionPostDTO.map(sectionPostDTO, warehouseService))), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SectionPostDTO>> getAllSections() {
        return new ResponseEntity<>(SectionPostDTO.map(sectionService.findAll()), HttpStatus.OK);
    }
}
