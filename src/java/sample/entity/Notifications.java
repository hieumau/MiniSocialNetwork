/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.entity;

import sample.account.UsersBLO;
import sample.article.ArticlesBLO;
import sample.notification.NotificationBLO;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author saost
 */
@Entity
@Table(name = "Notifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n")
    , @NamedQuery(name = "Notifications.findByNotificationId", query = "SELECT n FROM Notifications n WHERE n.notificationId = :notificationId")
    , @NamedQuery(name = "Notifications.findByActivityType", query = "SELECT n FROM Notifications n WHERE n.activityType = :activityType")
    , @NamedQuery(name = "Notifications.findByIsSeen", query = "SELECT n FROM Notifications n WHERE n.isSeen = :isSeen")
    , @NamedQuery(name = "Notifications.findByDate", query = "SELECT n FROM Notifications n WHERE n.date = :date")
    , @NamedQuery(name = "Notifications.findByCommentContent", query = "SELECT n FROM Notifications n WHERE n.commentContent = :commentContent")})
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "notificationId")
    private Integer notificationId;
    @Basic(optional = false)
    @Column(name = "activityType")
    private int activityType;
    @Basic(optional = false)
    @Column(name = "isSeen")
    private boolean isSeen;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "commentContent")
    private String commentContent;
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    @ManyToOne(optional = false)
    private Articles articleId;
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private Users userId;

    public Notifications() {
    }

    public Notifications(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Notifications(Integer notificationId, int activityType, boolean isSeen, Date date) {
        this.notificationId = notificationId;
        this.activityType = activityType;
        this.isSeen = isSeen;
        this.date = date;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Articles getArticleId() {
        return articleId;
    }

    public void setArticleId(Articles articleId) {
        this.articleId = articleId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notificationId != null ? notificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.notificationId == null && other.notificationId != null) || (this.notificationId != null && !this.notificationId.equals(other.notificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        UsersBLO usersBLO = new UsersBLO();
        ArticlesBLO articlesBLO = new ArticlesBLO();
        NotificationBLO notificationBLO = new NotificationBLO();

        String userName = articlesBLO.get(articleId.getArticleId()).getUserId().getFullName();

        String content = "";
        if (activityType == NotificationBLO.LIKE){
            content = " like your post ";
        } if (activityType == NotificationBLO.UNLIKE){
            content = " unlike your post ";
        } if (activityType == NotificationBLO.DISLIKE){
            content = " dislike your post ";
        } if (activityType == NotificationBLO.UNDISLIKE){
            content = " undislike your post ";
        } if (activityType == NotificationBLO.COMMENT){
            if (getCommentContent() != null)
            content = " comment \"<b>" + getCommentContent()  + "</b>\" on post ";
            else content = " comment on post ";

        }
        return content;
    }

    public String getInteractUserName(){
        UsersBLO usersBLO = new UsersBLO();
        return usersBLO.get(userId.getUserId()).getFullName();
    }

    public String getArticleTitle(){
        ArticlesBLO articlesBLO = new ArticlesBLO();
        return articlesBLO.get(articleId.getArticleId()).getTitle();
    }

    public final String getTimeAgo(){
        String timeAgo = "";
        Date date = new Date();
        //milliseconds
        long different = date.getTime() - this.getDate().getTime();

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
                    if (elapsedSeconds < 5) timeAgo = "just now";
                    else timeAgo = elapsedSeconds + " seconds ago";
                } else {
                    if (elapsedMinutes == 1) timeAgo = elapsedMinutes + " minute ago";
                    else timeAgo = elapsedMinutes + " minutes ago";
                }
            } else {
                if (elapsedHours == 1) timeAgo = elapsedHours + " hour ago";
                timeAgo = elapsedHours + " hours ago";
            }
        } else {
            if (elapsedDays == 1) timeAgo = elapsedDays + " day ago";
            else timeAgo = elapsedDays + " days ago";
        }

        return timeAgo;
    }
    
}
