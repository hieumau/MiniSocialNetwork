package sample.article;

import sample.account.UsersBLO;
import sample.entity.Articles;
import sample.entity.Users;
import sample.jpa_controller.ArticlesJpaController;
import sample.jpa_controller.exceptions.IllegalOrphanException;
import sample.jpa_controller.exceptions.NonexistentEntityException;
import sample.notification.NotificationBLO;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

public class ArticlesBLO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialNetworkMiniPU");
    public static final int MAX_ARTICLE_PER_PAGE = 10;

    public Articles create(String title, String description, String imagePath, String userId){
        Date date = new Date();
        ArticlesJpaController articlesJpaController = new ArticlesJpaController(emf);
        UsersBLO usersBLO = new UsersBLO();
        if (!title.isEmpty() && usersBLO.isActiveUser(userId)){
            Articles article = new Articles(0, title, description, imagePath, date, true);
            article.setUserId(new Users(userId));
            articlesJpaController.create(article);
            return article;
        }
        return null;
    }

    public List<Articles> getArticleListSortByDateTimeDesFilterByKeyword(int page, String keyword){
        List<Articles> articleList = new ArrayList<>();

        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT article " +
                "FROM Articles  article " +
                "WHERE article.status = true AND (article.title LIKE :keyword OR article.description LIKE :keyword) " +
                "ORDER BY article.date DESC";
        Query query = entityManager.createQuery(sql, Articles.class);
        query.setParameter("keyword", "%" + keyword + "%");

        query.setFirstResult((page-1) * MAX_ARTICLE_PER_PAGE);
        query.setMaxResults(MAX_ARTICLE_PER_PAGE);
        articleList = query.getResultList();
        
        return articleList;
    }

    public List<Articles> getArticleListSortByDateTimeDesFilterByKeyword(int page, String keyword, int maxNumberOfArticlesPerPage){
        List<Articles> articleList = new ArrayList<>();

        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT article.articleId " +
                "FROM Articles  article " +
                "WHERE article.status = true AND (article.title LIKE :keyword OR article.description LIKE :keyword) " +
                "ORDER BY article.date DESC";
        Query query = entityManager.createQuery(sql, Articles.class);
        query.setParameter("keyword", "%" + keyword + "%");

        query.setFirstResult((page-1) * maxNumberOfArticlesPerPage);
        query.setMaxResults(maxNumberOfArticlesPerPage);
        articleList = query.getResultList();

        return articleList;
    }

    public long getNumberOfArticleFilterByKeyword(String keyword){
        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT count (article.articleId) " +
                "FROM Articles  article " +
                "WHERE article.status = true AND (article.title LIKE :keyword OR article.description LIKE :keyword) ";
        Query query = entityManager.createQuery(sql, Long.class);
        query.setParameter("keyword", "%" + keyword + "%");

        return (long) query.getSingleResult();
    }

    public Articles get(int articleId){
        ArticlesJpaController articlesJpaController = new ArticlesJpaController(emf);
        return articlesJpaController.findArticles(articleId);
    }

    public void delete(int articleId) {
        ArticlesJpaController articlesJpaController = new ArticlesJpaController(emf);
        Articles article = get(articleId);
        article.setStatus(false);
        try {
            articlesJpaController.edit(article);
            // delete notification
            NotificationBLO notificationBLO = new NotificationBLO();
            notificationBLO.deleteAllNotiOfArticle(articleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
