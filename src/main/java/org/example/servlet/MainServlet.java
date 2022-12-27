package org.example.servlet;

import org.example.config.JavaConfig;
import org.example.controller.PostController;
import org.example.exception.NotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private PostController postController;
    private final String PATH_POSTS = "/api/posts";
    private final String PATH_WITH_NUMBER_POST = PATH_POSTS + "/\\d+";

    @Override
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        final var controller = context.getBean("postController");
        this.postController = (PostController) controller;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            if (method.equals("GET") && path.equals(PATH_POSTS)) {
                postController.all(resp);
                return;
            }
            if (method.equals("GET") && path.matches(PATH_WITH_NUMBER_POST)) {
                final var id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                postController.getById(id, resp);
                return;
            }
            if (method.equals("POST") && path.equals(PATH_POSTS)) {
                postController.save(req.getReader(), resp);
                return;
            }
            if (method.equals("DELETE") && path.matches(PATH_WITH_NUMBER_POST)) {
                // easy way
                final long id = Long.parseLong(path.substring(path.lastIndexOf("/") + 1));
                postController.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NotFoundException ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}