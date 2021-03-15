/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.emotion.controller;

import sample.account.UsersBLO;
import sample.article.ArticlesBLO;
import sample.article.controller.ViewArticleDetailController;
import sample.article.controller.ViewHomePageController;
import sample.emotion.EmotionBLO;
import sample.entity.Articles;
import sample.entity.Users;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saost
 */
public class ReactController extends HttpServlet {
    private static final String ERROR = "login.jsp";
//    private static final String SUCCESS = "home_page.jsp";
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
            int emotionType = Integer.parseInt(request.getParameter("emotionType"));
            String isHomePage = "";
            isHomePage = request.getParameter("isHomePage");
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("AUTH_USER");
            ArticlesBLO articlesBLO = new ArticlesBLO();
            UsersBLO usersBLO = new UsersBLO();
            EmotionBLO emotionBLO = new EmotionBLO();

            if (articlesBLO.get(articleId) != null){
                if (emotionType == 1){
                    emotionBLO.pressLikeEmotion(articleId, user.getUserId());
                } else if (emotionType == 2){
                    emotionBLO.pressDislikeEmotion(articleId, user.getUserId());
                }
            }
            if (isHomePage != null){
                if (isHomePage.toLowerCase().equals("no")){
                    url = ViewArticleDetailController.getArticleDetailLink(articleId);
                } else url = ViewHomePageController.class.getSimpleName();
            } else {
                url = ViewHomePageController.class.getSimpleName();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            response.sendRedirect(url);
//            request.getRequestDispatcher(url).forward(request, response);
        }
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
