package edu.bluejack16_2.blueprint;

/**
 * Created by BAGAS on 16-Jul-17.
 */
class PostComments {

    String userId, commentContent;

    public PostComments() {

    }

    public PostComments(String userId, String commentContent) {
        this.userId = userId;
        this.commentContent = commentContent;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
