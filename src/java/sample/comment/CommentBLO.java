package sample.comment;

import sample.account.UsersBLO;
import sample.article.ArticlesBLO;
import sample.entity.Articles;
import sample.entity.Comments;
import sample.entity.Notifications;
import sample.entity.Users;
import sample.jpa_controller.CommentsJpaController;
import sample.jpa_controller.NotificationsJpaController;
import sample.notification.NotificationBLO;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentBLO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialNetworkMiniPU");

    public Comments create(String userId, int articleId, String content){
        Date date = new Date();
        CommentsJpaController commentsJpaController = new CommentsJpaController(emf);

        UsersBLO usersBLO = new UsersBLO();
        ArticlesBLO articlesBLO = new ArticlesBLO();
        try {
            if (usersBLO.isActiveUser(userId) &&
                    articlesBLO.get(articleId) != null &&
                    !content.isEmpty()) {
                Comments comment = new Comments(0, content, date, true);
                comment.setArticleId(new Articles(articleId));
                comment.setUserId(new Users(userId));

                commentsJpaController.create(comment);

                //creat noti
                if (!articlesBLO.get(articleId).getUserId().getUserId().equals(userId)){
                    NotificationBLO notificationBLO = new NotificationBLO();
                    notificationBLO.createCommentNoti(userId, articleId, NotificationBLO.COMMENT, content);
                }
                return comment;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Comments> getCommentListByArticleIdSortByTime(int articleId){
        List<Comments> commentList = new ArrayList<>();

        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT comment " +
                "FROM Comments comment   " +
                "WHERE comment.status = true AND comment.articleId = :articleId " +
                "ORDER BY comment.date DESC";
        Query query = entityManager.createQuery(sql, Comments.class);
        
        query.setParameter("articleId", new Articles(articleId));
        commentList = query.getResultList();

        return commentList;
    }

    public long getNumberOfCommentByArticleId(int articleId){
        EntityManager entityManager = emf.createEntityManager();
        String sql = "SELECT count (comment) " +
                "FROM Comments comment " +
                "WHERE comment.status = true AND comment.articleId = :articleId ";
        Query query = entityManager.createQuery(sql, Long.class);
        query.setParameter("articleId", new Articles(articleId));
        long commentNumber = (long) query.getSingleResult();
        return commentNumber;
    }

    public Comments get(int commendId){
        CommentsJpaController commentsJpaController = new CommentsJpaController(emf);
        return commentsJpaController.findComments(commendId);
    }

    public static final String getTimeAgo(Comments comment){
        String timeAgo = "";
        Date date = new Date();
        //milliseconds
        long different = date.getTime() - comment.getDate().getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays == 0){
            if (elapsedHours == 0){
                if (elapsedMinutes == 0){
                    timeAgo = elapsedSeconds + " ago";
                } else {
                    timeAgo = elapsedMinutes + " ago";
                }
            } else {
                timeAgo = elapsedHours + " ago";
            }
        } else timeAgo = elapsedDays + " ago";

        return timeAgo;
    }

    public void delete(int commentId) {
        CommentsJpaController commentsJpaController = new CommentsJpaController(emf);

        Comments comment = get(commentId);
        comment.setStatus(false);
        try {
            commentsJpaController.edit(comment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
