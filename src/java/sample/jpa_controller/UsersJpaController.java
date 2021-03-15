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
import sample.entity.Roles;
import sample.entity.Articles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import sample.entity.Comments;
import sample.entity.Notifications;
import sample.entity.Emotions;
import sample.entity.Users;
import sample.jpa_controller.exceptions.IllegalOrphanException;
import sample.jpa_controller.exceptions.NonexistentEntityException;
import sample.jpa_controller.exceptions.PreexistingEntityException;

/**
 *
 * @author saost
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws PreexistingEntityException, Exception {
        if (users.getArticlesCollection() == null) {
            users.setArticlesCollection(new ArrayList<Articles>());
        }
        if (users.getCommentsCollection() == null) {
            users.setCommentsCollection(new ArrayList<Comments>());
        }
        if (users.getNotificationsCollection() == null) {
            users.setNotificationsCollection(new ArrayList<Notifications>());
        }
        if (users.getEmotionsCollection() == null) {
            users.setEmotionsCollection(new ArrayList<Emotions>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Roles roleId = users.getRoleId();
            if (roleId != null) {
                roleId = em.getReference(roleId.getClass(), roleId.getRoleId());
                users.setRoleId(roleId);
            }
            Collection<Articles> attachedArticlesCollection = new ArrayList<Articles>();
            for (Articles articlesCollectionArticlesToAttach : users.getArticlesCollection()) {
                articlesCollectionArticlesToAttach = em.getReference(articlesCollectionArticlesToAttach.getClass(), articlesCollectionArticlesToAttach.getArticleId());
                attachedArticlesCollection.add(articlesCollectionArticlesToAttach);
            }
            users.setArticlesCollection(attachedArticlesCollection);
            Collection<Comments> attachedCommentsCollection = new ArrayList<Comments>();
            for (Comments commentsCollectionCommentsToAttach : users.getCommentsCollection()) {
                commentsCollectionCommentsToAttach = em.getReference(commentsCollectionCommentsToAttach.getClass(), commentsCollectionCommentsToAttach.getCommentId());
                attachedCommentsCollection.add(commentsCollectionCommentsToAttach);
            }
            users.setCommentsCollection(attachedCommentsCollection);
            Collection<Notifications> attachedNotificationsCollection = new ArrayList<Notifications>();
            for (Notifications notificationsCollectionNotificationsToAttach : users.getNotificationsCollection()) {
                notificationsCollectionNotificationsToAttach = em.getReference(notificationsCollectionNotificationsToAttach.getClass(), notificationsCollectionNotificationsToAttach.getNotificationId());
                attachedNotificationsCollection.add(notificationsCollectionNotificationsToAttach);
            }
            users.setNotificationsCollection(attachedNotificationsCollection);
            Collection<Emotions> attachedEmotionsCollection = new ArrayList<Emotions>();
            for (Emotions emotionsCollectionEmotionsToAttach : users.getEmotionsCollection()) {
                emotionsCollectionEmotionsToAttach = em.getReference(emotionsCollectionEmotionsToAttach.getClass(), emotionsCollectionEmotionsToAttach.getEmotionsPK());
                attachedEmotionsCollection.add(emotionsCollectionEmotionsToAttach);
            }
            users.setEmotionsCollection(attachedEmotionsCollection);
            em.persist(users);
            if (roleId != null) {
                roleId.getUsersCollection().add(users);
                roleId = em.merge(roleId);
            }
            for (Articles articlesCollectionArticles : users.getArticlesCollection()) {
                Users oldUserIdOfArticlesCollectionArticles = articlesCollectionArticles.getUserId();
                articlesCollectionArticles.setUserId(users);
                articlesCollectionArticles = em.merge(articlesCollectionArticles);
                if (oldUserIdOfArticlesCollectionArticles != null) {
                    oldUserIdOfArticlesCollectionArticles.getArticlesCollection().remove(articlesCollectionArticles);
                    oldUserIdOfArticlesCollectionArticles = em.merge(oldUserIdOfArticlesCollectionArticles);
                }
            }
            for (Comments commentsCollectionComments : users.getCommentsCollection()) {
                Users oldUserIdOfCommentsCollectionComments = commentsCollectionComments.getUserId();
                commentsCollectionComments.setUserId(users);
                commentsCollectionComments = em.merge(commentsCollectionComments);
                if (oldUserIdOfCommentsCollectionComments != null) {
                    oldUserIdOfCommentsCollectionComments.getCommentsCollection().remove(commentsCollectionComments);
                    oldUserIdOfCommentsCollectionComments = em.merge(oldUserIdOfCommentsCollectionComments);
                }
            }
            for (Notifications notificationsCollectionNotifications : users.getNotificationsCollection()) {
                Users oldUserIdOfNotificationsCollectionNotifications = notificationsCollectionNotifications.getUserId();
                notificationsCollectionNotifications.setUserId(users);
                notificationsCollectionNotifications = em.merge(notificationsCollectionNotifications);
                if (oldUserIdOfNotificationsCollectionNotifications != null) {
                    oldUserIdOfNotificationsCollectionNotifications.getNotificationsCollection().remove(notificationsCollectionNotifications);
                    oldUserIdOfNotificationsCollectionNotifications = em.merge(oldUserIdOfNotificationsCollectionNotifications);
                }
            }
            for (Emotions emotionsCollectionEmotions : users.getEmotionsCollection()) {
                Users oldUsersOfEmotionsCollectionEmotions = emotionsCollectionEmotions.getUsers();
                emotionsCollectionEmotions.setUsers(users);
                emotionsCollectionEmotions = em.merge(emotionsCollectionEmotions);
                if (oldUsersOfEmotionsCollectionEmotions != null) {
                    oldUsersOfEmotionsCollectionEmotions.getEmotionsCollection().remove(emotionsCollectionEmotions);
                    oldUsersOfEmotionsCollectionEmotions = em.merge(oldUsersOfEmotionsCollectionEmotions);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsers(users.getUserId()) != null) {
                throw new PreexistingEntityException("Users " + users + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUserId());
            Roles roleIdOld = persistentUsers.getRoleId();
            Roles roleIdNew = users.getRoleId();
            Collection<Articles> articlesCollectionOld = persistentUsers.getArticlesCollection();
            Collection<Articles> articlesCollectionNew = users.getArticlesCollection();
            Collection<Comments> commentsCollectionOld = persistentUsers.getCommentsCollection();
            Collection<Comments> commentsCollectionNew = users.getCommentsCollection();
            Collection<Notifications> notificationsCollectionOld = persistentUsers.getNotificationsCollection();
            Collection<Notifications> notificationsCollectionNew = users.getNotificationsCollection();
            Collection<Emotions> emotionsCollectionOld = persistentUsers.getEmotionsCollection();
            Collection<Emotions> emotionsCollectionNew = users.getEmotionsCollection();
            List<String> illegalOrphanMessages = null;
            for (Articles articlesCollectionOldArticles : articlesCollectionOld) {
                if (!articlesCollectionNew.contains(articlesCollectionOldArticles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Articles " + articlesCollectionOldArticles + " since its userId field is not nullable.");
                }
            }
            for (Comments commentsCollectionOldComments : commentsCollectionOld) {
                if (!commentsCollectionNew.contains(commentsCollectionOldComments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comments " + commentsCollectionOldComments + " since its userId field is not nullable.");
                }
            }
            for (Notifications notificationsCollectionOldNotifications : notificationsCollectionOld) {
                if (!notificationsCollectionNew.contains(notificationsCollectionOldNotifications)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Notifications " + notificationsCollectionOldNotifications + " since its userId field is not nullable.");
                }
            }
            for (Emotions emotionsCollectionOldEmotions : emotionsCollectionOld) {
                if (!emotionsCollectionNew.contains(emotionsCollectionOldEmotions)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Emotions " + emotionsCollectionOldEmotions + " since its users field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (roleIdNew != null) {
                roleIdNew = em.getReference(roleIdNew.getClass(), roleIdNew.getRoleId());
                users.setRoleId(roleIdNew);
            }
            Collection<Articles> attachedArticlesCollectionNew = new ArrayList<Articles>();
            for (Articles articlesCollectionNewArticlesToAttach : articlesCollectionNew) {
                articlesCollectionNewArticlesToAttach = em.getReference(articlesCollectionNewArticlesToAttach.getClass(), articlesCollectionNewArticlesToAttach.getArticleId());
                attachedArticlesCollectionNew.add(articlesCollectionNewArticlesToAttach);
            }
            articlesCollectionNew = attachedArticlesCollectionNew;
            users.setArticlesCollection(articlesCollectionNew);
            Collection<Comments> attachedCommentsCollectionNew = new ArrayList<Comments>();
            for (Comments commentsCollectionNewCommentsToAttach : commentsCollectionNew) {
                commentsCollectionNewCommentsToAttach = em.getReference(commentsCollectionNewCommentsToAttach.getClass(), commentsCollectionNewCommentsToAttach.getCommentId());
                attachedCommentsCollectionNew.add(commentsCollectionNewCommentsToAttach);
            }
            commentsCollectionNew = attachedCommentsCollectionNew;
            users.setCommentsCollection(commentsCollectionNew);
            Collection<Notifications> attachedNotificationsCollectionNew = new ArrayList<Notifications>();
            for (Notifications notificationsCollectionNewNotificationsToAttach : notificationsCollectionNew) {
                notificationsCollectionNewNotificationsToAttach = em.getReference(notificationsCollectionNewNotificationsToAttach.getClass(), notificationsCollectionNewNotificationsToAttach.getNotificationId());
                attachedNotificationsCollectionNew.add(notificationsCollectionNewNotificationsToAttach);
            }
            notificationsCollectionNew = attachedNotificationsCollectionNew;
            users.setNotificationsCollection(notificationsCollectionNew);
            Collection<Emotions> attachedEmotionsCollectionNew = new ArrayList<Emotions>();
            for (Emotions emotionsCollectionNewEmotionsToAttach : emotionsCollectionNew) {
                emotionsCollectionNewEmotionsToAttach = em.getReference(emotionsCollectionNewEmotionsToAttach.getClass(), emotionsCollectionNewEmotionsToAttach.getEmotionsPK());
                attachedEmotionsCollectionNew.add(emotionsCollectionNewEmotionsToAttach);
            }
            emotionsCollectionNew = attachedEmotionsCollectionNew;
            users.setEmotionsCollection(emotionsCollectionNew);
            users = em.merge(users);
            if (roleIdOld != null && !roleIdOld.equals(roleIdNew)) {
                roleIdOld.getUsersCollection().remove(users);
                roleIdOld = em.merge(roleIdOld);
            }
            if (roleIdNew != null && !roleIdNew.equals(roleIdOld)) {
                roleIdNew.getUsersCollection().add(users);
                roleIdNew = em.merge(roleIdNew);
            }
            for (Articles articlesCollectionNewArticles : articlesCollectionNew) {
                if (!articlesCollectionOld.contains(articlesCollectionNewArticles)) {
                    Users oldUserIdOfArticlesCollectionNewArticles = articlesCollectionNewArticles.getUserId();
                    articlesCollectionNewArticles.setUserId(users);
                    articlesCollectionNewArticles = em.merge(articlesCollectionNewArticles);
                    if (oldUserIdOfArticlesCollectionNewArticles != null && !oldUserIdOfArticlesCollectionNewArticles.equals(users)) {
                        oldUserIdOfArticlesCollectionNewArticles.getArticlesCollection().remove(articlesCollectionNewArticles);
                        oldUserIdOfArticlesCollectionNewArticles = em.merge(oldUserIdOfArticlesCollectionNewArticles);
                    }
                }
            }
            for (Comments commentsCollectionNewComments : commentsCollectionNew) {
                if (!commentsCollectionOld.contains(commentsCollectionNewComments)) {
                    Users oldUserIdOfCommentsCollectionNewComments = commentsCollectionNewComments.getUserId();
                    commentsCollectionNewComments.setUserId(users);
                    commentsCollectionNewComments = em.merge(commentsCollectionNewComments);
                    if (oldUserIdOfCommentsCollectionNewComments != null && !oldUserIdOfCommentsCollectionNewComments.equals(users)) {
                        oldUserIdOfCommentsCollectionNewComments.getCommentsCollection().remove(commentsCollectionNewComments);
                        oldUserIdOfCommentsCollectionNewComments = em.merge(oldUserIdOfCommentsCollectionNewComments);
                    }
                }
            }
            for (Notifications notificationsCollectionNewNotifications : notificationsCollectionNew) {
                if (!notificationsCollectionOld.contains(notificationsCollectionNewNotifications)) {
                    Users oldUserIdOfNotificationsCollectionNewNotifications = notificationsCollectionNewNotifications.getUserId();
                    notificationsCollectionNewNotifications.setUserId(users);
                    notificationsCollectionNewNotifications = em.merge(notificationsCollectionNewNotifications);
                    if (oldUserIdOfNotificationsCollectionNewNotifications != null && !oldUserIdOfNotificationsCollectionNewNotifications.equals(users)) {
                        oldUserIdOfNotificationsCollectionNewNotifications.getNotificationsCollection().remove(notificationsCollectionNewNotifications);
                        oldUserIdOfNotificationsCollectionNewNotifications = em.merge(oldUserIdOfNotificationsCollectionNewNotifications);
                    }
                }
            }
            for (Emotions emotionsCollectionNewEmotions : emotionsCollectionNew) {
                if (!emotionsCollectionOld.contains(emotionsCollectionNewEmotions)) {
                    Users oldUsersOfEmotionsCollectionNewEmotions = emotionsCollectionNewEmotions.getUsers();
                    emotionsCollectionNewEmotions.setUsers(users);
                    emotionsCollectionNewEmotions = em.merge(emotionsCollectionNewEmotions);
                    if (oldUsersOfEmotionsCollectionNewEmotions != null && !oldUsersOfEmotionsCollectionNewEmotions.equals(users)) {
                        oldUsersOfEmotionsCollectionNewEmotions.getEmotionsCollection().remove(emotionsCollectionNewEmotions);
                        oldUsersOfEmotionsCollectionNewEmotions = em.merge(oldUsersOfEmotionsCollectionNewEmotions);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = users.getUserId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUserId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Articles> articlesCollectionOrphanCheck = users.getArticlesCollection();
            for (Articles articlesCollectionOrphanCheckArticles : articlesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Articles " + articlesCollectionOrphanCheckArticles + " in its articlesCollection field has a non-nullable userId field.");
            }
            Collection<Comments> commentsCollectionOrphanCheck = users.getCommentsCollection();
            for (Comments commentsCollectionOrphanCheckComments : commentsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Comments " + commentsCollectionOrphanCheckComments + " in its commentsCollection field has a non-nullable userId field.");
            }
            Collection<Notifications> notificationsCollectionOrphanCheck = users.getNotificationsCollection();
            for (Notifications notificationsCollectionOrphanCheckNotifications : notificationsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Notifications " + notificationsCollectionOrphanCheckNotifications + " in its notificationsCollection field has a non-nullable userId field.");
            }
            Collection<Emotions> emotionsCollectionOrphanCheck = users.getEmotionsCollection();
            for (Emotions emotionsCollectionOrphanCheckEmotions : emotionsCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the Emotions " + emotionsCollectionOrphanCheckEmotions + " in its emotionsCollection field has a non-nullable users field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Roles roleId = users.getRoleId();
            if (roleId != null) {
                roleId.getUsersCollection().remove(users);
                roleId = em.merge(roleId);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
