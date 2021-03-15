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
import sample.entity.Comments;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.entity.Articles;
import sample.entity.Notifications;
import sample.entity.Emotions;
import sample.jpa_controller.exceptions.IllegalOrphanException;
import sample.jpa_controller.exceptions.NonexistentEntityException;

/**
 *
 * @author saost
 */
public class ArticlesJpaController implements Serializable {

    public ArticlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articles articles) {
        if (articles.getCommentsCollection() == null) {
            articles.setCommentsCollection(new ArrayList<Comments>());
        }
        if (articles.getNotificationsCollection() == null) {
            articles.setNotificationsCollection(new ArrayList<Notifications>());
        }
        if (articles.getEmotionsCollection() == null) {
            articles.setEmotionsCollection(new ArrayList<Emotions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userId = articles.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                articles.setUserId(userId);
            }
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : articles.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getCommentId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            articles.setCommentsCollection(attachedCommentsCollection);
            Collection<Notifications> attachedNotificationsCollection = new ArrayList<Notifications>();
            for (Notifications notificationsCollectionNotificationsToAttach : articles.getNotificationsCollection()) {
                notificationsCollectionNotificationsToAttach = em.getReference(notificationsCollectionNotificationsToAttach.getClass(), notificationsCollectionNotificationsToAttach.getNotificationId());
                attachedNotificationsCollection.add(notificationsCollectionNotificationsToAttach);
            }
            articles.setNotificationsCollection(attachedNotificationsCollection);
            Collection<Emotions> attachedEmotionsCollection = new ArrayList<Emotions>();
            for (Emotions emotionsCollectionEmotionsToAttach : articles.getEmotionsCollection()) {
                emotionsCollectionEmotionsToAttach = em.getReference(emotionsCollectionEmotionsToAttach.getClass(), emotionsCollectionEmotionsToAttach.getEmotionsPK());
                attachedEmotionsCollection.add(emotionsCollectionEmotionsToAttach);
            }
            articles.setEmotionsCollection(attachedEmotionsCollection);
            em.persist(articles);
            if (userId != null) {
                userId.getArticlesCollection().add(articles);
                userId = em.merge(userId);
            }
            for (Comments commentsCollectionComments : articles.getCommentsCollection()) {
                Articles oldArticleIdOfCommentsCollectionComments = commentsCollectionComments.getArticleId();
                commentsCollectionComments.setArticleId(articles);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldArticleIdOfCommentsCollectionComments != null) {
                    oldArticleIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldArticleIdOfCommentsCollectionComments = em.merge(oldArticleIdOfCommentsCollectionComments);
                }
            }
            for (Notifications notificationsCollectionNotifications : articles.getNotificationsCollection()) {
                Articles oldArticleIdOfNotificationsCollectionNotifications = notificationsCollectionNotifications.getArticleId();
                notificationsCollectionNotifications.setArticleId(articles);
                notificationsCollectionNotifications = em.merge(notificationsCollectionNotifications);
                if (oldArticleIdOfNotificationsCollectionNotifications != null) {
                    oldArticleIdOfNotificationsCollectionNotifications.getNotificationsCollection().remove(notificationsCollectionNotifications);
                    oldArticleIdOfNotificationsCollectionNotifications = em.merge(oldArticleIdOfNotificationsCollectionNotifications);
                }
            }
            for (Emotions emotionsCollectionEmotions : articles.getEmotionsCollection()) {
                Articles oldArticlesOfEmotionsCollectionEmotions = emotionsCollectionEmotions.getArticles();
                emotionsCollectionEmotions.setArticles(articles);
                emotionsCollectionEmotions = em.merge(emotionsCollectionEmotions);
                if (oldArticlesOfEmotionsCollectionEmotions != null) {
                    oldArticlesOfEmotionsCollectionEmotions.getEmotionsCollection().remove(emotionsCollectionEmotions);
                    oldArticlesOfEmotionsCollectionEmotions = em.merge(oldArticlesOfEmotionsCollectionEmotions);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articles articles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articles persistentArticles = em.find(Articles.class, articles.getArticleId());
            Users userIdOld = persistentArticles.getUserId();
            Users userIdNew = articles.getUserId();
            Collection<Comments> commentsCollectionOld = persistentArticles.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = articles.getCommentsCollection();
            Collection<Notifications> notificationsCollectionOld = persistentArticles.getNotificationsCollection();
            Collection<Notifications> notificationsCollectionNew = articles.getNotificationsCollection();
            Collection<Emotions> emotionsCollectionOld = persistentArticles.getEmotionsCollection();
            Collection<Emotions> emotionsCollectionNew = articles.getEmotionsCollection();
            List<String> illegalOrphanMessages = null;
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comments " + commentsCollectionOldComments + " since its articleId field is not nullable.");
                }
            }
            for (Notifications notificationsCollectionOldNotifications : notificationsCollectionOld) {
                if (!notificationsCollectionNew.contains(notificationsCollectionOldNotifications)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notifications " + notificationsCollectionOldNotifications + " since its articleId field is not nullable.");
                }
            }
            for (Emotions emotionsCollectionOldEmotions : emotionsCollectionOld) {
                if (!emotionsCollectionNew.contains(emotionsCollectionOldEmotions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emotions " + emotionsCollectionOldEmotions + " since its articles field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                articles.setUserId(userIdNew);
            }
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getCommentId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            articles.setCommentsCollection(commentsCollectionNew);
            Collection<Notifications> attachedNotificationsCollectionNew = new ArrayList<Notifications>();
            for (Notifications notificationsCollectionNewNotificationsToAttach : notificationsCollectionNew) {
                notificationsCollectionNewNotificationsToAttach = em.getReference(notificationsCollectionNewNotificationsToAttach.getClass(), notificationsCollectionNewNotificationsToAttach.getNotificationId());
                attachedNotificationsCollectionNew.add(notificationsCollectionNewNotificationsToAttach);
            }
            notificationsCollectionNew = attachedNotificationsCollectionNew;
            articles.setNotificationsCollection(notificationsCollectionNew);
            Collection<Emotions> attachedEmotionsCollectionNew = new ArrayList<Emotions>();
            for (Emotions emotionsCollectionNewEmotionsToAttach : emotionsCollectionNew) {
                emotionsCollectionNewEmotionsToAttach = em.getReference(emotionsCollectionNewEmotionsToAttach.getClass(), emotionsCollectionNewEmotionsToAttach.getEmotionsPK());
                attachedEmotionsCollectionNew.add(emotionsCollectionNewEmotionsToAttach);
            }
            emotionsCollectionNew = attachedEmotionsCollectionNew;
            articles.setEmotionsCollection(emotionsCollectionNew);
            articles = em.merge(articles);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getArticlesCollection().remove(articles);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getArticlesCollection().add(articles);
                userIdNew = em.merge(userIdNew);
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Articles oldArticleIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getArticleId();
                    commentsCollectionNewComments.setArticleId(articles);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldArticleIdOfCommentsCollectionNewComments != null && !oldArticleIdOfCommentsCollectionNewComments.equals(articles)) {
                        oldArticleIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldArticleIdOfCommentsCollectionNewComments = em.merge(oldArticleIdOfCommentsCollectionNewComments);
                    }
                }
            }
            for (Notifications notificationsCollectionNewNotifications : notificationsCollectionNew) {
                if (!notificationsCollectionOld.contains(notificationsCollectionNewNotifications)) {
                    Articles oldArticleIdOfNotificationsCollectionNewNotifications = notificationsCollectionNewNotifications.getArticleId();
                    notificationsCollectionNewNotifications.setArticleId(articles);
                    notificationsCollectionNewNotifications = em.merge(notificationsCollectionNewNotifications);
                    if (oldArticleIdOfNotificationsCollectionNewNotifications != null && !oldArticleIdOfNotificationsCollectionNewNotifications.equals(articles)) {
                        oldArticleIdOfNotificationsCollectionNewNotifications.getNotificationsCollection().remove(notificationsCollectionNewNotifications);
                        oldArticleIdOfNotificationsCollectionNewNotifications = em.merge(oldArticleIdOfNotificationsCollectionNewNotifications);
                    }
                }
            }
            for (Emotions emotionsCollectionNewEmotions : emotionsCollectionNew) {
                if (!emotionsCollectionOld.contains(emotionsCollectionNewEmotions)) {
                    Articles oldArticlesOfEmotionsCollectionNewEmotions = emotionsCollectionNewEmotions.getArticles();
                    emotionsCollectionNewEmotions.setArticles(articles);
                    emotionsCollectionNewEmotions = em.merge(emotionsCollectionNewEmotions);
                    if (oldArticlesOfEmotionsCollectionNewEmotions != null && !oldArticlesOfEmotionsCollectionNewEmotions.equals(articles)) {
                        oldArticlesOfEmotionsCollectionNewEmotions.getEmotionsCollection().remove(emotionsCollectionNewEmotions);
                        oldArticlesOfEmotionsCollectionNewEmotions = em.merge(oldArticlesOfEmotionsCollectionNewEmotions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articles.getArticleId();
                if (findArticles(id) == null) {
                    throw new NonexistentEntityException("The articles with id " + id + " no longer exists.");
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
            Articles articles;
            try {
                articles = em.getReference(Articles.class, id);
                articles.getArticleId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Comments> commentsCollectionOrphanCheck = articles.getCommentsCollection();
            for (Comments commentsCollectionOrphanCheckComments : commentsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articles (" + articles + ") cannot be destroyed since the Comments " + commentsCollectionOrphanCheckComments + " in its commentsCollection field has a non-nullable articleId field.");
            }
            Collection<Notifications> notificationsCollectionOrphanCheck = articles.getNotificationsCollection();
            for (Notifications notificationsCollectionOrphanCheckNotifications : notificationsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articles (" + articles + ") cannot be destroyed since the Notifications " + notificationsCollectionOrphanCheckNotifications + " in its notificationsCollection field has a non-nullable articleId field.");
            }
            Collection<Emotions> emotionsCollectionOrphanCheck = articles.getEmotionsCollection();
            for (Emotions emotionsCollectionOrphanCheckEmotions : emotionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Articles (" + articles + ") cannot be destroyed since the Emotions " + emotionsCollectionOrphanCheckEmotions + " in its emotionsCollection field has a non-nullable articles field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userId = articles.getUserId();
            if (userId != null) {
                userId.getArticlesCollection().remove(articles);
                userId = em.merge(userId);
            }
            em.remove(articles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articles> findArticlesEntities() {
        return findArticlesEntities(true, -1, -1);
    }

    public List<Articles> findArticlesEntities(int maxResults, int firstResult) {
        return findArticlesEntities(false, maxResults, firstResult);
    }

    private List<Articles> findArticlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articles.class));
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

    public Articles findArticles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articles.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articles> rt = cq.from(Articles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
