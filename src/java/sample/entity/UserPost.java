package sample.entity;

public class UserPost {
    private Articles article;
    private long numberOfLike;
    private long numberOfDislike;
    private long numberOfComment;

    public UserPost() {
        
    }

    public UserPost(Articles article, long numberOfLike, long numberOfDislike, long numberOfComment) {
        this.article = article;
        this.numberOfLike = numberOfLike;
        this.numberOfDislike = numberOfDislike;
        this.numberOfComment = numberOfComment;
    }

    public Articles getArticle() {
        return article;
    }

    public void setArticle(Articles article) {
        this.article = article;
    }

    public long getNumberOfLike() {
        return numberOfLike;
    }

    public void setNumberOfLike(long numberOfLike) {
        this.numberOfLike = numberOfLike;
    }

    public long getNumberOfDislike() {
        return numberOfDislike;
    }

    public void setNumberOfDislike(long numberOfDislike) {
        this.numberOfDislike = numberOfDislike;
    }

    public long getNumberOfComment() {
        return numberOfComment;
    }

    public void setNumberOfComment(long numberOfComment) {
        this.numberOfComment = numberOfComment;
    }
}
