package sample.notification;

import sample.account.UsersBLO;
import sample.article.ArticlesBLO;
import sample.entity.Articles;
import sample.entity.Notifications;
import sample.entity.Users;
import sample.jpa_controller.NotificationsJpaController;

import javax.jws.soap.SOAPBinding;
import javax.management.Notification;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationBLO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialNetworkMiniPU");

    public static final int LIKE = 1;
    public static final int UNLIKE = -1;
    public static final int DISLIKE = 2;
    public static final int UNDISLIKE = -2;
    public static final int COMMENT = 3;
    public static final int DELETED = 0;

    public Notifications create(String userId, int articleId, int activityType){
        NotificationsJpaController notificationsJpaController = new NotificationsJpaController(emf);
        Date date = new Date();
        try {
            ArticlesBLO articlesBLO = new ArticlesBLO();
            UsersBLO usersBLO = new UsersBLO();
            if (usersBLO.isActiveUser(userId) &&
                    articlesBLO.get(articleId) != null){
                Notifications notifications = new Notifications(0, activityType, false, date);
                notifications.setUserId(new Users(userId));
                notifications.setArticleId(new Articles(articleId));

                notificationsJpaController.create(notifications);
                return notifications;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Notifications createCommentNoti(String userId, int articleId, int activityType, String commentContent){
        NotificationsJpaController notificationsJpaController = new NotificationsJpaController(emf);
        Date date = new Date();
        try {
            ArticlesBLO articlesBLO = new ArticlesBLO();
            UsersBLO usersBLO = new UsersBLO();
            if (usersBLO.isActiveUser(userId) &&
                    articlesBLO.get(articleId) != null){
                Notifications notifications = new Notifications(0, activityType, false, date);
                notifications.setUserId(new Users(userId));
                notifications.setArticleId(new Articles(articleId));
                notifications.setCommentContent(commentContent);

                notificationsJpaController.create(notifications);
                return notifications;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Notifications set(Notifications notification){
        NotificationsJpaController notificationsJpaController = new NotificationsJpaController(emf);
            try {
                notificationsJpaController.edit(notification);
                return notification;
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }

    public Notifications changeSeenStatus(int notificationId){
        NotificationsJpaController notificationsJpaController = new NotificationsJpaController(emf);
        Notifications notifications = notificationsJpaController.findNotifications(notificationId);
        if (notifications != null){
            notifications.setIsSeen(true);

            try {
                notificationsJpaController.edit(notifications);
                return notifications;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public long getNumberOfUnseenNotificationOfUser(String userId){
        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT count (notification) " +
                "FROM Notifications notification " +
                "INNER JOIN Articles article on article.articleId = notification.articleId.articleId " +
                "WHERE notification.isSeen = false AND notification.activityType != 0 AND article.userId = :userId ";

        Query query = entityManager.createQuery(sql, Long.class);
        query.setParameter("userId", new Users(userId));
        return (long) query.getSingleResult();
    }

    public List<Notifications> getNotificationOfUser(String userId){
        List<Notifications> notificationList = new ArrayList<>();

        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT notification " +
                "FROM Notifications notification " +
                "INNER JOIN Articles article on article.articleId = notification.articleId.articleId " +
                "WHERE article.userId = :userId AND notification.activityType != 0 " +
                "ORDER BY notification.date DESC";
        Query query = entityManager.createQuery(sql, Notifications.class);
        query.setParameter("userId", new Users(userId));
        notificationList = query.getResultList();

        return notificationList;
    }

    public List<Notifications> getNotificationOfArticle(int articleId){
        List<Notifications> notificationList = new ArrayList<>();

        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT notification " +
                "FROM Notifications notification " +
                "WHERE notification.articleId = :articleId ";
        Query query = entityManager.createQuery(sql, Notifications.class);
        query.setParameter("articleId", new Articles(articleId));
        notificationList = query.getResultList();

        return notificationList;
    }

    public String translate(int notificationType){
        String type = "";
        if (notificationType == LIKE){
            type = "like";
        } else if (notificationType == DISLIKE){
            type = "dislike";
        } else if (notificationType == COMMENT){
            type = "comment";
        } else if (notificationType == UNLIKE){
            type = "unlike";
        } else if (notificationType == UNDISLIKE){
            type = "undislike";
        } else if (notificationType == DELETED){
            type = "deleted";
        }

        return type;
    }

    public void deleteAllNotiOfArticle(int articleId) {
        List<Notifications> notificationsList = getNotificationOfArticle(articleId);
        for (Notifications notification:
             notificationsList) {
            notification.setActivityType(DELETED);
            set(notification);
        }
    }
}
