/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.emotion;

import sample.account.UsersBLO;
import sample.article.ArticlesBLO;
import sample.entity.*;
import sample.jpa_controller.EmotionsJpaController;
import sample.notification.NotificationBLO;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.Date;

/**
 *
 * @author saost
 */
public class EmotionBLO {
    private final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialNetworkMiniPU");

    public long getLikeEmotionOfArticle (int articleId){
        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT count (emotion) " +
                "FROM Emotions  emotion " +
                "WHERE emotion.emotionType = 1 AND emotion.articles.articleId = :articleId ";
        Query query = entityManager.createQuery(sql, Long.class);
        query.setParameter("articleId", articleId);

        return (long) query.getSingleResult();
    }

    public long getDislikeEmotionOfArticle (int articleId){
        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT count (emotion) " +
                "FROM Emotions  emotion " +
                "WHERE emotion.emotionType = 2 AND emotion.articles.articleId = :articleId ";
        Query query = entityManager.createQuery(sql, Long.class);
        query.setParameter("articleId", articleId);

        return (long) query.getSingleResult();
    }

    public Emotions pressLikeEmotion(int articleId, String userId){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);

        UsersBLO usersBLO = new UsersBLO();
        ArticlesBLO articlesBLO = new ArticlesBLO();

        try {
            if (usersBLO.isActiveUser(userId) &&
                    articlesBLO.get(articleId) != null) {

                Emotions emotion = new Emotions(userId, articleId);
                emotion.setUsers(new Users(userId));
                emotion.setArticles(new Articles(articleId));
                NotificationBLO notificationBLO = new NotificationBLO();

                if (isReactBefore(articleId, userId)){
                    if (isLiked(articleId, userId)){
                        emotion.setEmotionType(0);
                        if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                        notificationBLO.create(userId, articleId, NotificationBLO.UNLIKE);
                    } else {
                        emotion.setEmotionType(1);
                        if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                            notificationBLO.create(userId, articleId, NotificationBLO.LIKE);

                    }

                    emotionsJpaController.edit(emotion);
                } else {
                    emotion.setEmotionType(1);
                    if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                    notificationBLO.create(userId, articleId, NotificationBLO.LIKE);

                    emotionsJpaController.create(emotion);
                }

                return emotion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Emotions pressDislikeEmotion(int articleId, String userId){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);

        UsersBLO usersBLO = new UsersBLO();
        ArticlesBLO articlesBLO = new ArticlesBLO();

        try {
            if (usersBLO.isActiveUser(userId) &&
                    articlesBLO.get(articleId) != null) {

                Emotions emotion = new Emotions(userId, articleId);
                emotion.setUsers(new Users(userId));
                emotion.setArticles(new Articles(articleId));
                NotificationBLO notificationBLO = new NotificationBLO();

                if (isReactBefore(articleId, userId)){
                    if (isDisliked(articleId, userId)){
                        emotion.setEmotionType(0);
                        if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                            notificationBLO.create(userId, articleId, NotificationBLO.UNDISLIKE);

                    } else {
                        emotion.setEmotionType(2);
                        if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                            notificationBLO.create(userId, articleId, NotificationBLO.DISLIKE);
                    }

                    emotionsJpaController.edit(emotion);
                } else {
                    emotion.setEmotionType(2);
                    if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId))
                    notificationBLO.create(userId, articleId, NotificationBLO.DISLIKE);
                    emotionsJpaController.create(emotion);
                }

                return emotion;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isLiked(int articleId, String userId){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);
        Emotions emotion = emotionsJpaController.findEmotions(new EmotionsPK(userId, articleId));
        if (emotion != null && emotion.getEmotionType() == 1){
            return true;
        }
        return false;
    }

    public static boolean isDisliked(int articleId, String userId){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);
        Emotions emotion = emotionsJpaController.findEmotions(new EmotionsPK(userId, articleId));
        if (emotion != null && emotion.getEmotionType() == 2){
            return true;
        }
        return false;
    }

    public boolean isReactBefore(int articleId, String userId){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);
        Emotions emotion = emotionsJpaController.findEmotions(new EmotionsPK(userId, articleId));
        if (emotion != null){
            return true;
        }
        return false;
    }

    public Emotions create(Emotions emotion){
        EmotionsJpaController emotionsJpaController = new EmotionsJpaController(emf);
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(emotion);
            entityManager.getTransaction().commit();
            return emotion;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
