package com.bionic.edu.sfc.service.dao;

import com.bionic.edu.sfc.dao.IDao;
import com.bionic.edu.sfc.entity.Hideable;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Ivan
 * 2014.10
 */
@Transactional(Transactional.TxType.REQUIRED)
public interface IService<T extends Hideable> {

    public IDao<T> getDao();

    default public void create(T object) {
        getDao().create(object);
    }

    default public void update(T object) {
        getDao().update(object);
    }

    default public void delete(T object) {
        getDao().delete(object);
    }

    default public void deleteWithUniqueFields(T object, String... uniquePropertyNames) {
        getDao().deleteWithUniqueFields(object, uniquePropertyNames);
    }

    default public T findById(long id) {
        return getDao().findById(id);
    }

    default public List<T> getAll(String orderValueName) {
        return getDao().getAll(orderValueName);
    }
}
