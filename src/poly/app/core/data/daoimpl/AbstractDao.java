package poly.app.core.data.daoimpl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import poly.app.core.data.dao.GenericDao;
import poly.app.core.utils.HibernateUtil;

public class AbstractDao<ID extends Serializable, T> implements GenericDao<ID, T>{
    private Class<T> persistenceClass;
    
    public AbstractDao() {
        // generic < x , y > as array
        // set persistenceClass = T
        this.persistenceClass = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
    
    public String getPersistenceClassName(){
        return this.persistenceClass.getSimpleName();
    }

    protected Session getSession(){
        return HibernateUtil.getSessionFactory().openSession();
    }
    
    @Override
    public List<T> getAll() {
        List<T> list;
        Session session = this.getSession();
        try {
            list = session.createCriteria(this.persistenceClass).list();
        }catch (HibernateException ex){
            throw ex;
        }finally {
            session.close();
        }

        return list;
    }
    
    @Override
    public T getById(ID id) {
        T result;
        Session session = this.getSession();
        try {
            // note: the first parameter is class type, so we pass persistenceClass at this situation
            result = (T) session.get(this.persistenceClass, id);
        }catch (HibernateException ex){
            throw ex;
        }
        return result;
    }

    @Override
    public void insert(T entity) {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(entity);
            transaction.commit();
        }catch (HibernateException ex){
            transaction.rollback();
            throw ex;
        }finally {
            session.close();
        }
    }

    @Override
    public void update(T entity) {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(entity);
            transaction.commit();
        }catch (HibernateException ex){
            transaction.rollback();
            throw ex;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean delete(T entity) {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.delete(entity);
            transaction.commit();
            return true;
        }catch (HibernateException ex){
            transaction.rollback();
            throw ex;
        }finally {
            session.close();
        }
    }

    @Override
    public int multipleDelete(List<T> entities) {
        Session session = this.getSession();
        Transaction transaction = session.beginTransaction();
        int countDeleted = 0;
        try {
            for (T entity : entities) {
                session.delete(entity);
                countDeleted++;
            }
            transaction.commit();
        }catch (HibernateException ex){
            transaction.rollback();
            throw ex;
        }finally {
            session.close();
        }
        return countDeleted;
    }
}