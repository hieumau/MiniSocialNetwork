/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.controller;

import sample.account.controller.*;
import sample.article.controller.DeleteArticleController;
import sample.article.controller.PostArticleController;
import sample.article.controller.ViewArticleDetailController;
import sample.article.controller.ViewHomePageController;
import sample.comment.controller.DeleteCommentController;
import sample.comment.controller.PostCommentController;
import sample.emotion.controller.ReactController;
import sample.notification.controller.ViewNotificationController;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author saost
 */
public class MainController extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String LOGIN = LoginController.class.getSimpleName();
    private static final String LOGOUT = LogoutController.class.getSimpleName();
    private static final String POST_ARTICLE = PostArticleController.class.getSimpleName();
    private static final String VIEW_ARTICLE_DETAIL = ViewArticleDetailController.class.getSimpleName();
    private static final String VIEW_HOME_PAGE = ViewHomePageController.class.getSimpleName();
    private static final String VIEW_NOTIFICATION_LIST = ViewNotificationController.class.getSimpleName();
    private static final String CHECK_VERIFY_CODE = VerifyAccountController.class.getSimpleName();
    private static final String RESEND_VERIFY_CODE = ResendVerifyCodeController.class.getSimpleName();
    private static final String VIEW_VERIFY_PAGE = "verify_user_account_page.jsp";
    private static final String DELETE_ARTICLE = DeleteArticleController.class.getSimpleName();
    private static final String DELETE_COMMENT = DeleteCommentController.class.getSimpleName();


    private static final String POST_COMMENT = PostCommentController.class.getSimpleName();
    private static final String PRESS_REACT = ReactController.class.getSimpleName();


    private static final String CREATE_USER_ACCOUNT = CreateUserAccountController.class.getSimpleName();

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
            String action = request.getParameter("btnAction");

            if (action.equals("Create user account")) {
                url = CREATE_USER_ACCOUNT;
            } else if (action.equals("Login")){
                url = LOGIN;
            } else if (action.equals("Logout")){
                url = LOGOUT;
            } else if (action.equals("Post article")){
                url = POST_ARTICLE;
            } else if (action.equals("View article detail")) {
                url = VIEW_ARTICLE_DETAIL;
            } else if (action.equals("Post comment")) {
                url = POST_COMMENT;
            } else if (action.equals("Press react")) {
                url = PRESS_REACT;
            } else if (action.equals("View home page")) {
                url = VIEW_HOME_PAGE;
            } else if (action.equals("View notification")) {
                url = VIEW_NOTIFICATION_LIST;
            } else if (action.equals("Check verify code")) {
                url = CHECK_VERIFY_CODE;
            } else if (action.equals("Resend verify code")) {
                url = RESEND_VERIFY_CODE;
            } else if (action.equals("View verify page")) {
                url = VIEW_VERIFY_PAGE;
            } else if (action.equals("Delete article")) {
                url = DELETE_ARTICLE;
            } else if (action.equals("Delete comment")) {
                url = DELETE_COMMENT;
            }

        } catch (Exception e){
            log("ERROR AT MAINCONTROLLER" + e.toString());
        }

        request.getRequestDispatcher(url).forward(request, response);
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
