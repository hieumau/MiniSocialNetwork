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
import sample.entity.Comments;
import sample.entity.Users;
import sample.jpa_controller.exceptions.NonexistentEntityException;

/**
 *
 * @author saost
 */
public class CommentsJpaController implements Serializable {

    public CommentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comments comments) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articles articleId = comments.getArticleId();
            if (articleId != null) {
                articleId = em.getReference(articleId.getClass(), articleId.getArticleId());
                comments.setArticleId(articleId);
            }
            Users userId = comments.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getUserId());
                comments.setUserId(userId);
            }
            em.persist(comments);
            if (articleId != null) {
                articleId.getCommentsCollection().add(comments);
                articleId = em.merge(articleId);
            }
            if (userId != null) {
                userId.getCommentsCollection().add(comments);
                userId = em.merge(userId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comments comments) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comments persistentComments = em.find(Comments.class, comments.getCommentId());
            Articles articleIdOld = persistentComments.getArticleId();
            Articles articleIdNew = comments.getArticleId();
            Users userIdOld = persistentComments.getUserId();
            Users userIdNew = comments.getUserId();
            if (articleIdNew != null) {
                articleIdNew = em.getReference(articleIdNew.getClass(), articleIdNew.getArticleId());
                comments.setArticleId(articleIdNew);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getUserId());
                comments.setUserId(userIdNew);
            }
            comments = em.merge(comments);
            if (articleIdOld != null && !articleIdOld.equals(articleIdNew)) {
                articleIdOld.getCommentsCollection().remove(comments);
                articleIdOld = em.merge(articleIdOld);
            }
            if (articleIdNew != null && !articleIdNew.equals(articleIdOld)) {
                articleIdNew.getCommentsCollection().add(comments);
                articleIdNew = em.merge(articleIdNew);
            }
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getCommentsCollection().remove(comments);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getCommentsCollection().add(comments);
                userIdNew = em.merge(userIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comments.getCommentId();
                if (findComments(id) == null) {
                    throw new NonexistentEntityException("The comments with id " + id + " no longer exists.");
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
            Comments comments;
            try {
                comments = em.getReference(Comments.class, id);
                comments.getCommentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comments with id " + id + " no longer exists.", enfe);
            }
            Articles articleId = comments.getArticleId();
            if (articleId != null) {
                articleId.getCommentsCollection().remove(comments);
                articleId = em.merge(articleId);
            }
            Users userId = comments.getUserId();
            if (userId != null) {
                userId.getCommentsCollection().remove(comments);
                userId = em.merge(userId);
            }
            em.remove(comments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comments> findCommentsEntities() {
        return findCommentsEntities(true, -1, -1);
    }

    public List<Comments> findCommentsEntities(int maxResults, int firstResult) {
        return findCommentsEntities(false, maxResults, firstResult);
    }

    private List<Comments> findCommentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comments.class));
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

    public Comments findComments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comments.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comments> rt = cq.from(Comments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
