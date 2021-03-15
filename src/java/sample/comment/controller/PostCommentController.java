/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.comment.controller;

import sample.article.ArticlesBLO;
import sample.article.controller.ViewArticleDetailController;
import sample.comment.CommentBLO;
import sample.entity.Comments;
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
public class PostCommentController extends HttpServlet {

    private static final String ERROR = ViewArticleDetailController.class.getSimpleName();
//    private static final String SUCCESS = ViewArticleDetailController.class.getSimpleName();
    private static final String SUCCESS = "";

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
            boolean check = true;
            HttpSession session = request.getSession();
            Users user = (Users) session.getAttribute("AUTH_USER");
            String articleId = request.getParameter("articleId");
            String content = request.getParameter("content");

            CommentBLO commentBLO = new CommentBLO();
            if (commentBLO.create(user.getUserId(), Integer.parseInt(articleId), content) != null){
                request.setAttribute("MESSAGE", "Comment successful");
            }
            url = ViewArticleDetailController.getArticleDetailLink(Integer.parseInt(articleId));


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
