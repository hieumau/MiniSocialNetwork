/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.filter;

import sample.account.controller.CreateUserAccountController;
import sample.account.controller.LoginController;
import sample.account.controller.ResendVerifyCodeController;
import sample.account.controller.VerifyAccountController;
import sample.article.controller.DeleteArticleController;
import sample.article.controller.PostArticleController;
import sample.article.controller.ViewArticleDetailController;
import sample.article.controller.ViewHomePageController;
import sample.comment.controller.DeleteCommentController;
import sample.comment.controller.PostCommentController;
import sample.controller.MainController;
import sample.emotion.controller.ReactController;
import sample.entity.Roles;
import sample.entity.Users;
import sample.notification.controller.ViewNotificationController;
import sample.utils.EmailUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;

/**
 *
 * @author saost
 */
public class AuthenFilter implements Filter {
    private static final boolean debug = true;

    private static List<String> unVerifiedUserResource;
    private static List<String> verifiedUserResource;
    private static List<String> adminResource;
    private static final String LOGIN_PAGE = "login.jsp";

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;
    
    public AuthenFilter() {
        verifiedUserResource = new ArrayList<>();
        unVerifiedUserResource = new ArrayList<>();
        adminResource = new ArrayList<>();

        verifiedUserResource.add("");
        verifiedUserResource.add("home_page.jsp");
        verifiedUserResource.add("create_article_page.jsp");
        verifiedUserResource.add(PostArticleController.class.getSimpleName());
        verifiedUserResource.add(ViewArticleDetailController.class.getSimpleName());
        verifiedUserResource.add("article_detail.jsp");
        verifiedUserResource.add(PostCommentController.class.getSimpleName());
        verifiedUserResource.add(ReactController.class.getSimpleName());
        verifiedUserResource.add(ViewHomePageController.class.getSimpleName());
        verifiedUserResource.add(ViewNotificationController.class.getSimpleName());
        verifiedUserResource.add("notification.jsp");
        verifiedUserResource.add(DeleteArticleController.class.getSimpleName());
        verifiedUserResource.add(DeleteCommentController.class.getSimpleName());

        unVerifiedUserResource.add("");
        unVerifiedUserResource.add("verify_user_account_page.jsp");
        unVerifiedUserResource.add(VerifyAccountController.class.getSimpleName());
        unVerifiedUserResource.add(ResendVerifyCodeController.class.getSimpleName());

        adminResource.add("");
        adminResource.add("home_page.jsp");
        adminResource.add(ViewArticleDetailController.class.getSimpleName());
        adminResource.add("article_detail.jsp");
        adminResource.add(ViewHomePageController.class.getSimpleName());
        adminResource.add(DeleteArticleController.class.getSimpleName());
    }    
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthenFilter:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
        /*
	for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    String values[] = request.getParameterValues(name);
	    int n = values.length;
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    for(int i=0; i < n; i++) {
	        buf.append(values[i]);
	        if (i < n-1)
	            buf.append(",");
	    }
	    log(buf.toString());
	}
         */
    }    
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("AuthenFilter:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        try {
            HttpServletRequest rq = (HttpServletRequest) request;
            HttpServletResponse rs = (HttpServletResponse) response;
            String uri = rq.getRequestURI();
            log("uri = " + uri);

            if ((uri.contains(".js")
                    || uri.contains(".css")
                    || uri.contains(".eot")
                    || uri.contains(".svg")
                    || uri.contains(".ttf")
                    || uri.contains(".woff")
                    || uri.contains(".woff2")
                    || uri.contains(".jpg")
                    || uri.contains(".png")
                    || uri.contains(".jpeg"))
                    && !uri.contains(".jsp")) {
                chain.doFilter(request, response);
                return;
            } else {
                if (uri.contains("login.jsp")
                        || uri.contains(MainController.class.getSimpleName())
                        || uri.contains(LoginController.class.getSimpleName())
//                        || uri.contains(L.class.getSimpleName())
                        || uri.contains(CreateUserAccountController.class.getSimpleName())
                        || uri.contains("creat_user_account.jsp")
                        ) {
                    chain.doFilter(request, response);
                    return;
                }
            }

            int index = uri.lastIndexOf("/");
            String resource = uri.substring(index + 1);
            HttpSession session = rq.getSession();
            if (session == null || session.getAttribute("AUTH_USER") == null) {
                log("access denined");
                rs.sendRedirect(LOGIN_PAGE);
            } else {
                Users dto = (Users) session.getAttribute("AUTH_USER");
                Roles role = dto.getRoleId();
                if (role.getRoleId() == 2 &&
                        adminResource.contains(resource)) {
                    chain.doFilter(request, response);
                } else if (role.getRoleId() == 1 &&
                        dto.getStatus().equals("active") &&
                        verifiedUserResource.contains(resource)) {
                    chain.doFilter(request, response);
                } else if (role.getRoleId() == 1 &&
                        dto.getStatus().equals("new") &&
                        unVerifiedUserResource.contains(resource)) {
                    chain.doFilter(request, response);
                } else {
                    log("access denined");
                    rs.sendRedirect(LOGIN_PAGE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {        
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {        
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {                
                log("AuthenFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("AuthenFilter()");
        }
        StringBuffer sb = new StringBuffer("AuthenFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);        
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);                
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");                
                pw.print(stackTrace);                
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        filterConfig.getServletContext().log(msg);        
    }
    
}
