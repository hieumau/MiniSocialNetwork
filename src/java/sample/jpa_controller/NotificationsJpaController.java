/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.jpa_controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import sample.entity.Articles;
import sample.entity.Notifications;
import sample.entity.Users;
import sample.jpa_controller.exceptions.NonexistentEntityException;

/**
 *
 * @author saost
 */
public class NotificationsJpaController implements Serializable {

    public NotificationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Notifications notifications) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articles articleId = notifications.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getArticleId());
                notifications.setArticleId(articleId);
            }
            Users userId = notifications.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                notifications.setUserId(userId);
            }
            em.persist(notifications);
            if (articleId != null) {
                articleId.getNotificationsCollection().add(notifications);
                articleId = em.merge(articleId);
            }
            if (userId != null) {
                userId.getNotificationsCollection().add(notifications);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Notifications notifications) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notifications persistentNotifications = em.find(Notifications.class, notifications.getNotificationId());
            Articles articleIdOld = persistentNotifications.getArticleId();
            Articles articleIdNew = notifications.getArticleId();
            Users userIdOld = persistentNotifications.getUserId();
            Users userIdNew = notifications.getUserId();
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getArticleId());
                notifications.setArticleId(articleIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                notifications.setUserId(userIdNew);
            }
            notifications = em.merge(notifications);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getNotificationsCollection().remove(notifications);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getNotificationsCollection().add(notifications);
                articleIdNew = em.merge(articleIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getNotificationsCollection().remove(notifications);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getNotificationsCollection().add(notifications);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = notifications.getNotificationId();
                if (findNotifications(id) == null) {
                    throw new NonexistentEntityException("The notifications with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Notifications notifications;
            try {
                notifications = em.getReference(Notifications.class, id);
                notifications.getNotificationId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The notifications with id " + id + " no longer exists.", enfe);
            }
            Articles articleId = notifications.getArticleId();
            if (articleId != null) {
                articleId.getNotificationsCollection().remove(notifications);
                articleId = em.merge(articleId);
            }
            Users userId = notifications.getUserId();
            if (userId != null) {
                userId.getNotificationsCollection().remove(notifications);
                userId = em.merge(userId);
            }
            em.remove(notifications);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Notifications> findNotificationsEntities() {
        return findNotificationsEntities(true, -1, -1);
    }

    public List<Notifications> findNotificationsEntities(int maxResults, int firstResult) {
        return findNotificationsEntities(false, maxResults, firstResult);
    }

    private List<Notifications> findNotificationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Notifications.class));
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

    public Notifications findNotifications(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Notifications.class, id);
        } finally {
            em.close();
        }
    }

    public int getNotificationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Notifications> rt = cq.from(Notifications.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
