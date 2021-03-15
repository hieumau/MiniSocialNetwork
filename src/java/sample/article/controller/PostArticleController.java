/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.article.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sample.account.UsersBLO;
import sample.account.controller.LogoutController;
import sample.article.ArticlesBLO;
import sample.entity.Articles;
import sample.entity.Users;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.activation.FileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author saost
 */
public class PostArticleController extends HttpServlet {
    private static final AtomicLong counter = new AtomicLong(System.currentTimeMillis());
    private static final String ERROR = LogoutController.class.getSimpleName();
//    private static final String SUCCESS = ViewHomePageController.class.getSimpleName();
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
            String title = "";
            String description = "";
            String imagePath = "";

            //decrypt request
            boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
            if (isMultiPart){
                FileItemFactory fileItemFactory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(fileItemFactory);

                //get list file upload
                List<FileItem> fileItemList = null;
                try {
                    fileItemList = upload.parseRequest(request);
                } catch (FileUploadException e){
                    e.printStackTrace();
                }


                Iterator iterator = fileItemList.iterator();
                Hashtable params = new Hashtable();
                String fileName = null;

                while (iterator.hasNext()){
                    FileItem item = (FileItem) iterator.next();
                    if (item.isFormField()){
                        params.put(item.getFieldName(), item.getString("UTF-8"));
                    } else {
                        try {
                            String itemName = item.getName();
                            String fileExt = "";
                            String relativePath = "";
                            if (item.getSize() > 0){
                                if (itemName.contains(".")){
                                    fileExt = itemName.substring(itemName.lastIndexOf(".") + 1).toLowerCase();
                                }
                                //check is valid file
                                if (fileExt.equals("jpg") || fileExt.equals("png")){
                                    String newFileName = counter.getAndIncrement() + "." + fileExt;
                                    relativePath = "/images/" + newFileName;

                                    String realPath = getServletContext().getRealPath("/") + relativePath;

                                    File savedFile = new File(realPath);
                                    item.write(savedFile);
                                }
                            }


                            params.put(item.getFieldName(), relativePath);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                title = (String) params.get("title");
                description = (String) params.get("description");

                imagePath = (String) params.get("imageUpload");
            }

            ArticlesBLO articlesBLO = new ArticlesBLO();
            articlesBLO.create(title, description, imagePath, user.getUserId());
            if (check) {
                request.setAttribute("MESSAGE", "Post sucessful");
            }
            url = ViewHomePageController.getHomePageLink();
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
