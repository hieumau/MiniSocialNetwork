/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author saost
 */
@Embeddable
public class EmotionsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "userId")
    private String userId;
    @Basic(optional = false)
    @Column(name = "articleId")
    private int articleId;

    public EmotionsPK() {
    }

    public EmotionsPK(String userId, int articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        hash += (int) articleId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmotionsPK)) {
            return false;
        }
        EmotionsPK other = (EmotionsPK) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        if (this.articleId != other.articleId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.entity.EmotionsPK[ userId=" + userId + ", articleId=" + articleId + " ]";
    }
    
}
