/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.article.controller;

import sample.article.ArticlesBLO;
import sample.comment.CommentBLO;
import sample.emotion.EmotionBLO;
import sample.entity.Articles;
import sample.entity.Comments;
import sample.entity.Users;
import sample.notification.NotificationBLO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saost
 */
public class ViewArticleDetailController extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "article_detail.jsp";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            int articleId = Integer.parseInt(request.getParameter("articleId"));
            ArticlesBLO articlesBLO = new ArticlesBLO();
            CommentBLO commentBLO = new CommentBLO();
            EmotionBLO emotionBLO = new EmotionBLO();
            NotificationBLO notificationBLO = new NotificationBLO();

            int notificationId = 0;
            try {
                notificationId = Integer.parseInt(request.getParameter("notificationId"));
                notificationBLO.changeSeenStatus(notificationId);
            }catch (Exception e){
                e.printStackTrace();
            }

            Articles article = articlesBLO.get(articleId);
            if (article != null) {

                //show content
                request.setAttribute("ARTICLE", article);

                //show comment
                List<Comments> commentList = commentBLO.getCommentListByArticleIdSortByTime(article.getArticleId());
                long commentNumber = commentBLO.getNumberOfCommentByArticleId(article.getArticleId());
                request.setAttribute("COMMENT_LIST", commentList);
                request.setAttribute("COMMENT_NUMBER", commentNumber);

                //show react
                request.setAttribute("LIKE_EMOTION_NUMBER", emotionBLO.getLikeEmotionOfArticle(articleId));
                request.setAttribute("DISLIKE_EMOTION_NUMBER", emotionBLO.getDislikeEmotionOfArticle(articleId));

            }

            url = SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
//            response.sendRedirect(url);
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    public static final String getArticleDetailLink(int articleId){
        return  "MainController?btnAction=View article detail&articleId=" + articleId;
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
