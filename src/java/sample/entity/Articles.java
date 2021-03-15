/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.entity;

import sample.article.ArticlesBLO;
import sample.emotion.EmotionBLO;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author saost
 */
@Entity
@Table(name = "Articles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articles.findAll", query = "SELECT a FROM Articles a")
    , @NamedQuery(name = "Articles.findByArticleId", query = "SELECT a FROM Articles a WHERE a.articleId = :articleId")
    , @NamedQuery(name = "Articles.findByTitle", query = "SELECT a FROM Articles a WHERE a.title = :title")
    , @NamedQuery(name = "Articles.findByDescription", query = "SELECT a FROM Articles a WHERE a.description = :description")
    , @NamedQuery(name = "Articles.findByImagePath", query = "SELECT a FROM Articles a WHERE a.imagePath = :imagePath")
    , @NamedQuery(name = "Articles.findByDate", query = "SELECT a FROM Articles a WHERE a.date = :date")
    , @NamedQuery(name = "Articles.findByStatus", query = "SELECT a FROM Articles a WHERE a.status = :status")})
public class Articles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "articleId")
    private Integer articleId;
    @Basic(optional = false)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "imagePath")
    private String imagePath;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private Users userId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private Collection<Comments> commentsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articleId")
    private Collection<Notifications> notificationsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articles")
    private Collection<Emotions> emotionsCollection;

    public Articles() {
    }

    public Articles(Integer articleId) {
        this.articleId = articleId;
    }

    public Articles(Integer articleId, String title, String description, String imagePath, Date date, boolean status) {
        this.articleId = articleId;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.date = date;
        this.status = status;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    @XmlTransient
    public Collection<Comments> getCommentsCollection() {
        return commentsCollection;
    }

    public void setCommentsCollection(Collection<Comments> commentsCollection) {
        this.commentsCollection = commentsCollection;
    }

    @XmlTransient
    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    @XmlTransient
    public Collection<Emotions> getEmotionsCollection() {
        return emotionsCollection;
    }

    public void setEmotionsCollection(Collection<Emotions> emotionsCollection) {
        this.emotionsCollection = emotionsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (articleId != null ? articleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articles)) {
            return false;
        }
        Articles other = (Articles) object;
        if ((this.articleId == null && other.articleId != null) || (this.articleId != null && !this.articleId.equals(other.articleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.entity.Articles[ articleId=" + articleId + " ]";
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

    public final int getReact(String userId){
        if (EmotionBLO.isLiked(articleId, userId)) return Emotions.LIKE;
        if (EmotionBLO.isDisliked(articleId, userId)) return Emotions.DISLIKE;

        return Emotions.NONE;
    }



}
