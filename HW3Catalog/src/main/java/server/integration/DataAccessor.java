package server.integration;

import org.eclipse.persistence.jpa.PersistenceProvider;
import server.model.Account;
import server.model.File;

import javax.persistence.*;
import java.util.List;


public class DataAccessor {


    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> managerThreadLocal = new ThreadLocal<>();

    public DataAccessor() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("catalog");
    }



    public void storeFile(File file) {

        EntityManager entityManager = beginTransaction();
        entityManager.persist(file);

        commitTransaction();
        entityManager.close();
    }


    public void registerUser(Account account) {
        try {
            EntityManager entityManager = beginTransaction();
            entityManager.persist(account);
        }finally{
        commitTransaction();
        }

    }

    public Account findAccount(String username, boolean endTransaction) {
        if (username == null)
            return null;

        try {
            EntityManager em = beginTransaction();
            try {
                Query q = em.createNamedQuery("findAccountByName", Account.class);
                q.setParameter("name", username);
                Account result = (Account) q.getSingleResult();
                return result;
            } catch (NoResultException ex) {
                return null;
            }
        } finally {
            if (endTransaction)
                commitTransaction();
        }
    }

    public List<File> getFiles() {

        EntityManager em = beginTransaction();
        try {
            Query q = em.createNamedQuery("getFiles", File.class);
            List<File> files = q.getResultList();

            return files;
        } catch (NoResultException ex) {
            return null;
        } finally {
                commitTransaction();
        }
    }

    public File getFile(String fileName,boolean endTransaction) {
        try {
            EntityManager em = beginTransaction();
            try {
                Query q = em.createNamedQuery("findFileByName", File.class);
                q.setParameter("name", fileName);
                File result = (File) q.getSingleResult();
                return result;
            } catch (NoResultException ex) {
                return null;
            }
        } finally {
            if (endTransaction)
                commitTransaction();
        }
    }






    public boolean deleteAccount(String username) {
        if (username == null)
            return false;

        try {
            EntityManager em = beginTransaction();
            Query q = em.createNamedQuery("deleteAccountByName", Account.class);
            q.setParameter("name", username);
            if (q.executeUpdate() > 0) {
                q = em.createNamedQuery("deleteFileByOwnerName", File.class);
                q.setParameter("name", username);
                q.executeUpdate();
                return true;
            } else
                return false;
        } finally {
            commitTransaction();
        }
    }


    private EntityManager openEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private EntityManager beginTransaction() {
        EntityManager entityManager = openEntityManager();
        managerThreadLocal.set(entityManager);
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) transaction.begin();
        return entityManager;
    }

    public void updateEntity(){
        commitTransaction();
    }

    private void commitTransaction() {
        managerThreadLocal.get().getTransaction().commit();
    }

}
