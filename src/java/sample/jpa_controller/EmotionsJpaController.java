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
import sample.entity.Emotions;
import sample.entity.EmotionsPK;
import sample.entity.Users;
import sample.jpa_controller.exceptions.NonexistentEntityException;
import sample.jpa_controller.exceptions.PreexistingEntityException;

/**
 *
 * @author saost
 */
public class EmotionsJpaController implements Serializable {

    public EmotionsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emotions emotions) throws PreexistingEntityException, Exception {
        if (emotions.getEmotionsPK() == null) {
            emotions.setEmotionsPK(new EmotionsPK());
        }
        emotions.getEmotionsPK().setArticleId(emotions.getArticles().getArticleId());
        emotions.getEmotionsPK().setUserId(emotions.getUsers().getUserId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articles articles = emotions.getArticles();
            if (articles != null) {
                articles = em.getReference(articles.getClass(), articles.getArticleId());
                emotions.setArticles(articles);
            }
            Users users = emotions.getUsers();
            if (users != null) {
                users = em.getReference(users.getClass(), users.getUserId());
                emotions.setUsers(users);
            }
            em.persist(emotions);
            if (articles != null) {
                articles.getEmotionsCollection().add(emotions);
                articles = em.merge(articles);
            }
            if (users != null) {
                users.getEmotionsCollection().add(emotions);
                users = em.merge(users);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmotions(emotions.getEmotionsPK()) != null) {
                throw new PreexistingEntityException("Emotions " + emotions + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emotions emotions) throws NonexistentEntityException, Exception {
        emotions.getEmotionsPK().setArticleId(emotions.getArticles().getArticleId());
        emotions.getEmotionsPK().setUserId(emotions.getUsers().getUserId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emotions persistentEmotions = em.find(Emotions.class, emotions.getEmotionsPK());
            Articles articlesOld = persistentEmotions.getArticles();
            Articles articlesNew = emotions.getArticles();
            Users usersOld = persistentEmotions.getUsers();
            Users usersNew = emotions.getUsers();
            if (articlesNew != null) {
                articlesNew = em.getReference(articlesNew.getClass(), articlesNew.getArticleId());
                emotions.setArticles(articlesNew);
            }
            if (usersNew != null) {
                usersNew = em.getReference(usersNew.getClass(), usersNew.getUserId());
                emotions.setUsers(usersNew);
            }
            emotions = em.merge(emotions);
            if (articlesOld != null && !articlesOld.equals(articlesNew)) {
                articlesOld.getEmotionsCollection().remove(emotions);
                articlesOld = em.merge(articlesOld);
            }
            if (articlesNew != null && !articlesNew.equals(articlesOld)) {
                articlesNew.getEmotionsCollection().add(emotions);
                articlesNew = em.merge(articlesNew);
            }
            if (usersOld != null && !usersOld.equals(usersNew)) {
                usersOld.getEmotionsCollection().remove(emotions);
                usersOld = em.merge(usersOld);
            }
            if (usersNew != null && !usersNew.equals(usersOld)) {
                usersNew.getEmotionsCollection().add(emotions);
                usersNew = em.merge(usersNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EmotionsPK id = emotions.getEmotionsPK();
                if (findEmotions(id) == null) {
                    throw new NonexistentEntityException("The emotions with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EmotionsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emotions emotions;
            try {
                emotions = em.getReference(Emotions.class, id);
                emotions.getEmotionsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emotions with id " + id + " no longer exists.", enfe);
            }
            Articles articles = emotions.getArticles();
            if (articles != null) {
                articles.getEmotionsCollection().remove(emotions);
                articles = em.merge(articles);
            }
            Users users = emotions.getUsers();
            if (users != null) {
                users.getEmotionsCollection().remove(emotions);
                users = em.merge(users);
            }
            em.remove(emotions);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emotions> findEmotionsEntities() {
        return findEmotionsEntities(true, -1, -1);
    }

    public List<Emotions> findEmotionsEntities(int maxResults, int firstResult) {
        return findEmotionsEntities(false, maxResults, firstResult);
    }

    private List<Emotions> findEmotionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emotions.class));
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

    public Emotions findEmotions(EmotionsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emotions.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmotionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emotions> rt = cq.from(Emotions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
