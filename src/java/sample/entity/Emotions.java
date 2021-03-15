/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author saost
 */
@Entity
@Table(name = "Emotions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Emotions.findAll", query = "SELECT e FROM Emotions e")
    , @NamedQuery(name = "Emotions.findByUserId", query = "SELECT e FROM Emotions e WHERE e.emotionsPK.userId = :userId")
    , @NamedQuery(name = "Emotions.findByEmotionType", query = "SELECT e FROM Emotions e WHERE e.emotionType = :emotionType")
    , @NamedQuery(name = "Emotions.findByArticleId", query = "SELECT e FROM Emotions e WHERE e.emotionsPK.articleId = :articleId")})
public class Emotions implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EmotionsPK emotionsPK;
    @Basic(optional = false)
    @Column(name = "emotionType")
    private int emotionType;
    @JoinColumn(name = "articleId", referencedColumnName = "articleId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Articles articles;
    @JoinColumn(name = "userId", referencedColumnName = "userId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Users users;

    public Emotions() {
    }

    public Emotions(EmotionsPK emotionsPK) {
        this.emotionsPK = emotionsPK;
    }

    public Emotions(EmotionsPK emotionsPK, int emotionType) {
        this.emotionsPK = emotionsPK;
        this.emotionType = emotionType;
    }

    public Emotions(String userId, int articleId) {
        this.emotionsPK = new EmotionsPK(userId, articleId);
    }

    public EmotionsPK getEmotionsPK() {
        return emotionsPK;
    }

    public void setEmotionsPK(EmotionsPK emotionsPK) {
        this.emotionsPK = emotionsPK;
    }

    public int getEmotionType() {
        return emotionType;
    }

    public void setEmotionType(int emotionType) {
        this.emotionType = emotionType;
    }

    public Articles getArticles() {
        return articles;
    }

    public void setArticles(Articles articles) {
        this.articles = articles;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (emotionsPK != null ? emotionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emotions)) {
            return false;
        }
        Emotions other = (Emotions) object;
        if ((this.emotionsPK == null && other.emotionsPK != null) || (this.emotionsPK != null && !this.emotionsPK.equals(other.emotionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.entity.Emotions[ emotionsPK=" + emotionsPK + " ]";
    }

    public static final int LIKE = 1;
    public static final int DISLIKE = 2;
    public static final int NONE = 0;
}
