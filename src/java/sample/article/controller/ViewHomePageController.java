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
import sample.entity.UserPost;
import sample.entity.Users;
import sample.notification.NotificationBLO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saost
 */
public class ViewHomePageController extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "home_page.jsp";
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
            UserPost userPost;
            List<UserPost> userPostList = new ArrayList<>();
            ArticlesBLO articlesBLO = new ArticlesBLO();
            CommentBLO commentBLO = new CommentBLO();
            EmotionBLO emotionBLO = new EmotionBLO();

                  
            //set Unseen notifi
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("AUTH_USER");
            NotificationBLO notificationBLO = new NotificationBLO();
            request.setAttribute("NUMBER_OF_UNSEEN_NOTIFICATION", notificationBLO.getNumberOfUnseenNotificationOfUser(user.getUserId()));

            //paging
            String searchKeyword = request.getParameter("searchKeyword");
            if (searchKeyword == null){
                searchKeyword = "";
            }
            request.setAttribute("SEARCH_KEYWORD", searchKeyword);

            long totalPage = (long) articlesBLO.getNumberOfArticleFilterByKeyword(searchKeyword) / ArticlesBLO.MAX_ARTICLE_PER_PAGE + 1;
            request.setAttribute("TOTAL_PAGE", totalPage);
            int pageNumber;
            try {
                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            } catch (Exception e){
                pageNumber = 1;
            }
            request.setAttribute("CURRENT_PAGE", pageNumber);

            String message = (String) request.getAttribute("MESSAGE");
            request.setAttribute("MESSAGE", message);

            List<Articles> articleList = new ArrayList<>();
            articleList = articlesBLO.getArticleListSortByDateTimeDesFilterByKeyword(pageNumber, searchKeyword);
            if (articleList.size() > 0){
                for (Articles article: articleList) {
                    userPost = new UserPost();
                    userPost.setArticle(article);
                    userPost.setNumberOfLike(emotionBLO.getLikeEmotionOfArticle(article.getArticleId()));
                    userPost.setNumberOfDislike(emotionBLO.getDislikeEmotionOfArticle(article.getArticleId()));
                    userPost.setNumberOfComment(commentBLO.getNumberOfCommentByArticleId(article.getArticleId()));

                    userPostList.add(userPost);
                }
            }

            request.setAttribute("USER_POST_LIST", userPostList);
            url = SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    public static final String getHomePageLink(){
        return "MainController?btnAction=View home page";
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
