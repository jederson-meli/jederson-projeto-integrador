package br.com.meli.projetointegrador.service;

import br.com.meli.projetointegrador.exception.InexistentSectionException;
import br.com.meli.projetointegrador.model.Batch;
import br.com.meli.projetointegrador.model.Category;
import br.com.meli.projetointegrador.model.Section;
import br.com.meli.projetointegrador.repository.SectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Classe de implementação de SectionService responsável por processar dados de Section.
 * @author Jederson Carvalho Macedo
 * */
@Service
@AllArgsConstructor
public class SectionServiceImpl implements SectionService {

    private SectionRepository sectionRepository;

    @Override
    public Section findById(Long id) {
        return sectionRepository.findById(id).orElseThrow(() -> new InexistentSectionException("Section " + id + " does not exists!"));
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    public void updateCurrentSize(Integer amount, Long sectionId, boolean operation) {
        Section section = findById(sectionId);

        if(operation){
            section.setCurrentSize(section.getCurrentSize() + amount);
        }
        else{
            section.setCurrentSize(section.getCurrentSize() - amount);
        }
        sectionRepository.save(section);
    }

    @Override
    public List<Batch> checkBatchStockDueDate(Long id, Integer days) {

        List<Batch> batchList = findById(id).getBatchList();
        return getBatches(days, batchList, "asc");
    }

    @Override
    public List<Batch> checkBatchStockDueDateByCategory(Integer days, String category, String order) {
        List<List<Batch>> batchsList = new ArrayList<>();
        List<Batch> batchList = new ArrayList<>();

        Category convertedCategory = Category.valueOf(category);
        List<Section> sectionList = sectionRepository.findAll().stream().filter(section -> section.getCategory() == convertedCategory).collect(Collectors.toList());

        sectionList.forEach(section -> batchsList.add(section.getBatchList()));

        batchsList.forEach(batchList::addAll);

        return getBatches(days, batchList, order);

    }

    private List<Batch> getBatches(Integer days, List<Batch> batchList, String order) {
        List<Batch> filteredList = batchList.stream().filter(batch -> batch.getExpirationDate().isBefore(LocalDate.now().plusDays(days)) && LocalDate.now().isBefore(batch.getExpirationDate())).collect(Collectors.toList());

        Comparator<Batch> expirationDateSorter = Comparator.comparing(Batch::getExpirationDate);

        List<Batch> returnedList = filteredList.stream().sorted(expirationDateSorter).collect(Collectors.toList());

        if (order.equals("desc")) {
            Collections.reverse(returnedList);
        }

        return returnedList;
    }
}
