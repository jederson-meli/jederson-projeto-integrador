package br.com.meli.projetointegrador.service;

import br.com.meli.projetointegrador.dto.SectionPostDTO;
import br.com.meli.projetointegrador.model.Batch;
import br.com.meli.projetointegrador.model.Section;

import java.util.List;
/**
 * Interface de serviço responsável por processar dados de Section.
 * @author Jederson Carvalho Macedo
 * */
public interface SectionService {
    Section findById(Long id);
    List<Section> findAll();

    Section save(Section section);
    void updateCurrentSize(Integer amount, Long sectionId, boolean operation);
    List<Batch> checkBatchStockDueDate(Long id, Integer days);
    List<Batch> checkBatchStockDueDateByCategory(Integer days, String category, String order);
}
