/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.account.controller;

import sample.account.UsersBLO;
import sample.account.UsersErrorDTO;
import sample.entity.Users;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class CreateUserAccountController extends HttpServlet {
//    private static final String ERROR = "creat_user_account.jsp";
    private static final String ERROR = "login.jsp";
    private static final String SUCCESS = "verify_user_account_page.jsp";
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
        UsersErrorDTO usersErrorDTO = new UsersErrorDTO();

        try {
            String userId = request.getParameter("id");
            String password = request.getParameter("password");
            String passwordRepeat = request.getParameter("passwordRepeat");
            String fullName = request.getParameter("fullName");

            boolean check = true;

            UsersBLO usersBLO = new UsersBLO();

            if (!isValidEmail(userId)){
                usersErrorDTO.setUserIdError("Not valid Email");
                check = false;
            }
            if (usersBLO.isExitsUserId(userId)){
                usersErrorDTO.setUserIdError("This email is used");
                check = false;
            }
            if (!passwordRepeat.equals(password)){
                usersErrorDTO.setPasswordRepeatError("Password is not match");
                check = false;
            }
            if (check) {
                Users user = usersBLO.create(userId, password, fullName);
                if (user != null){
                    HttpSession session = request.getSession();
                    session.setAttribute("AUTH_USER", user);
                    url = SUCCESS;
                }
            } else {
                request.setAttribute("USER_ERROR",usersErrorDTO);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
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
