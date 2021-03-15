/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.jpa_controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sample.entity.Users;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.entity.Roles;
import sample.jpa_controller.exceptions.IllegalOrphanException;
import sample.jpa_controller.exceptions.NonexistentEntityException;
import sample.jpa_controller.exceptions.PreexistingEntityException;

/**
 *
 * @author saost
 */
public class RolesJpaController implements Serializable {

    public RolesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Roles roles) throws PreexistingEntityException, Exception {
        if (roles.getUsersCollection() == null) {
            roles.setUsersCollection(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Users> attachedUsersCollection = new ArrayList<Users>();
            for (Users usersCollectionUsersToAttach : roles.getUsersCollection()) {
                usersCollectionUsersToAttach = em.getReference(usersCollectionUsersToAttach.getClass(), usersCollectionUsersToAttach.getUserId());
                attachedUsersCollection.add(usersCollectionUsersToAttach);
            }
            roles.setUsersCollection(attachedUsersCollection);
            em.persist(roles);
            for (Users usersCollectionUsers : roles.getUsersCollection()) {
                Roles oldRoleIdOfUsersCollectionUsers = usersCollectionUsers.getRoleId();
                usersCollectionUsers.setRoleId(roles);
                usersCollectionUsers = em.merge(usersCollectionUsers);
                if (oldRoleIdOfUsersCollectionUsers != null) {
                    oldRoleIdOfUsersCollectionUsers.getUsersCollection().remove(usersCollectionUsers);
                    oldRoleIdOfUsersCollectionUsers = em.merge(oldRoleIdOfUsersCollectionUsers);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRoles(roles.getRoleId()) != null) {
                throw new PreexistingEntityException("Roles " + roles + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Roles roles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles persistentRoles = em.find(Roles.class, roles.getRoleId());
            Collection<Users> usersCollectionOld = persistentRoles.getUsersCollection();
            Collection<Users> usersCollectionNew = roles.getUsersCollection();
            List<String> illegalOrphanMessages = null;
            for (Users usersCollectionOldUsers : usersCollectionOld) {
                if (!usersCollectionNew.contains(usersCollectionOldUsers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Users " + usersCollectionOldUsers + " since its roleId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Users> attachedUsersCollectionNew = new ArrayList<Users>();
            for (Users usersCollectionNewUsersToAttach : usersCollectionNew) {
                usersCollectionNewUsersToAttach = em.getReference(usersCollectionNewUsersToAttach.getClass(), usersCollectionNewUsersToAttach.getUserId());
                attachedUsersCollectionNew.add(usersCollectionNewUsersToAttach);
            }
            usersCollectionNew = attachedUsersCollectionNew;
            roles.setUsersCollection(usersCollectionNew);
            roles = em.merge(roles);
            for (Users usersCollectionNewUsers : usersCollectionNew) {
                if (!usersCollectionOld.contains(usersCollectionNewUsers)) {
                    Roles oldRoleIdOfUsersCollectionNewUsers = usersCollectionNewUsers.getRoleId();
                    usersCollectionNewUsers.setRoleId(roles);
                    usersCollectionNewUsers = em.merge(usersCollectionNewUsers);
                    if (oldRoleIdOfUsersCollectionNewUsers != null && !oldRoleIdOfUsersCollectionNewUsers.equals(roles)) {
                        oldRoleIdOfUsersCollectionNewUsers.getUsersCollection().remove(usersCollectionNewUsers);
                        oldRoleIdOfUsersCollectionNewUsers = em.merge(oldRoleIdOfUsersCollectionNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = roles.getRoleId();
                if (findRoles(id) == null) {
                    throw new NonexistentEntityException("The roles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles roles;
            try {
                roles = em.getReference(Roles.class, id);
                roles.getRoleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Users> usersCollectionOrphanCheck = roles.getUsersCollection();
            for (Users usersCollectionOrphanCheckUsers : usersCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Roles (" + roles + ") cannot be destroyed since the Users " + usersCollectionOrphanCheckUsers + " in its usersCollection field has a non-nullable roleId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(roles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Roles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    public List<Roles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    private List<Roles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Roles.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Roles findRoles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Roles.class, id);
        } finally {
            em.close();
        }
    }

    public int getRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Roles> rt = cq.from(Roles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
