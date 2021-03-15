/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.entity;

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
@Table(name = "Comments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c")
    , @NamedQuery(name = "Comments.findByCommentId", query = "SELECT c FROM Comments c WHERE c.commentId = :commentId")
    , @NamedQuery(name = "Comments.findByCommentContent", query = "SELECT c FROM Comments c WHERE c.commentContent = :commentContent")
    , @NamedQuery(name = "Comments.findByDate", query = "SELECT c FROM Comments c WHERE c.date = :date")
    , @NamedQuery(name = "Comments.findByStatus", query = "SELECT c FROM Comments c WHERE c.status = :status")})
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "commentId")
    private Integer commentId;
    @Basic(optional = false)
    @Column(name = "commentContent")
    private String commentContent;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    @ManyToOne(optional = false)
    private Articles articleId;
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private Users userId;

    public Comments() {
    }

    public Comments(Integer commentId) {
        this.commentId = commentId;
    }

    public Comments(Integer commentId, String commentContent, Date date, boolean status) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.date = date;
        this.status = status;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
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
        hash += (commentId != null ? commentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comments)) {
            return false;
        }
        Comments other = (Comments) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.entity.Comments[ commentId=" + commentId + " ]";
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
