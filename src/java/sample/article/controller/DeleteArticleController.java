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
public class DeleteArticleController extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = ViewHomePageController.class.getSimpleName();
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
            ArticlesBLO articlesBLO = new ArticlesBLO();
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("AUTH_USER");
            int articleId = Integer.parseInt(request.getParameter("articleId"));
            Articles article = articlesBLO.get(articleId);

            //check right people
            if (user.getUserId().equals(article.getUserId().getUserId()) || user.getRoleId().getRoleId() == Users.ADMIN_ROLE){
                articlesBLO.delete(articleId);
                url = SUCCESS;
                request.setAttribute("MESSAGE", "Delete sucessful");
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
//            response.sendRedirect(url);
            request.getRequestDispatcher(url).forward(request, response);
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
