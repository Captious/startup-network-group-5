package ua.goit.java.startup.domainservice.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.goit.java.startup.bom.Model;
import ua.goit.java.startup.dao.DataRepository;
import ua.goit.java.startup.domainservice.DataService;
import ua.goit.java.startup.dto.ModelDTO;
import ua.goit.java.startup.translator.DataTranslator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DataServiceImpl<T extends ModelDTO, V extends Model> implements DataService<V> {

    private DataRepository<T> repository;

    private DataTranslator<T, V> translator;

    public DataServiceImpl() {
    }

    @Autowired
    public DataServiceImpl(DataRepository<T> repository, DataTranslator<T, V> translator) {
        this.repository = repository;
        this.translator = translator;
    }

    @Override
    @Transactional
    public V add(V model) {
        T modelDto = translator.toDto(model);
        repository.save(modelDto);
        return model;
    }

    @Override
    @Transactional
    public Collection<V> addAll(Collection<V> collection) {
        Collection<V> addedModels = new ArrayList<V>();
        collection.forEach(model -> addedModels.add(this.add(model)));
        return addedModels;
    }

    @Override
    @Transactional
    public V update(V model) {
        T modelDto = translator.toDto(model);
        repository.save(modelDto);
        return this.add(model);
    }

    @Override
    @Transactional
    public Collection<V> updateAll(Collection<V> collection) {
        return this.addAll(collection);
    }

    @Override
    @Transactional
    public V get(long id) {
        T modelDto = repository.findOne(id);
        V model = translator.fromDto(modelDto);
        return model;
    }

    @Override
    @Transactional
    public Collection<V> getAll() {
        Set<T> modelDto = (Set<T>) repository.findAll();
        Set<V> model = new HashSet<V>();
        model.addAll(translator.getListFromDto(modelDto));
        return model;
    }

    @Override
    @Transactional
    public void remove(long id) {
        V model = this.get(id);
        T modelDto = translator.toDto(model);
        repository.delete(modelDto);
    }

    @Override
    @Transactional
    public void remove(V model) {
        T modelDto = translator.toDto(model);
        repository.delete(modelDto);
    }

    @Override
    @Transactional
    public void remove(Collection<V> collection) {
        for (V model : collection) {
            this.remove(model);
        }
    }

    @Override
    @Transactional
    public void removeAll() {
        repository.deleteAll();
    }

    @Override
    @Transactional
    public boolean exist(long id) {
        V model = this.get(id);
        T modelDto = translator.toDto(model);
        return repository.exists(modelDto.getId());
    }

    @Override
    @Transactional
    public boolean exist(V model) {
        return this.exist(model.getId());
    }
}