/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.account.controller;

import sample.account.UsersBLO;
import sample.article.controller.ViewHomePageController;
import sample.entity.Users;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saost
 */
public class LoginController extends HttpServlet {
    private static final String ERROR = "login.jsp";
    private static final String ADMIN = ViewHomePageController.class.getSimpleName();
    private static final String VERIFIED_USER = ViewHomePageController.class.getSimpleName();
    private static final String UN_VERIFIED_USER = "verify_user_account_page.jsp";

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
            //clear session


            String userID = request.getParameter("userID");
            String password = request.getParameter("password");
            UsersBLO usersBLO = new UsersBLO();
            Users user = usersBLO.checkLogin(userID, password);
            if (user != null) {
                if (user.getRoleId().getRoleId() == 2) {
                    url = ADMIN;
                } else if (user.getRoleId().getRoleId() == 1 && user.getStatus().equals("new")){
                    url = UN_VERIFIED_USER;
                } else if (user.getRoleId().getRoleId() == 1 && user.getStatus().equals("active")){
                    url = VERIFIED_USER;
                }
                HttpSession session = request.getSession();
                session.setAttribute("AUTH_USER", user);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("ERROR_MESSAGE", "Wrong username or password!");
            }

        } catch (Exception e) {
            log("Error at LoginServlet " + e.toString());
        } finally {
            response.sendRedirect(url);
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
