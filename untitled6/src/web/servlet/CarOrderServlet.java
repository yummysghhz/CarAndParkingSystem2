package web.servlet;

import domain.CarOrder;
import domain.User;
import service.CarOrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "CarOrderServlet")
public class CarOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if ("findAll".equals(method)) {
            findAll(request, response);
        } else if ("createRentingMsg".equals(method)) {
            createRentingMsg(request, response);
        } else if ("makeChecked".equals(method)) {
            makeChecked(request, response);
        } else if ("applyOrder".equals(method)) {
            applyOrder(request, response);
        } else if ("checkCompleted".equals(method)) {
            checkCompleted(request, response);
        } else if ("cancel".equals(method)) {
            cancel(request, response);
        } else if ("findMine".equals(method)) {
            findMine(request, response);
        } else if ("selectState".equals(method)) {
            selectState(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    private void selectState(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String state = request.getParameter("state");

        if ("all".equals(state)) {
            findAll(request, response);
        } else {
            CarOrderService service = new CarOrderService();
            try {
                List<CarOrder> list = service.findByState(state);
                request.setAttribute("list", list);
                request.getRequestDispatcher("showCarOrder.jsp").forward(request, response);
            } catch (SQLException e) {
                response.setContentType("text/html;charset=utf-8");
                response.getWriter().println("查询车辆订单信息失败，返回<a href='index.jsp'>首页</a>");
            }
        }
    }

    private void findMine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        CarOrderService service = new CarOrderService();

        try {
            List<CarOrder> list = service.findMine(user.getId());
            request.setAttribute("list", list);
            request.getRequestDispatcher("showCarOrder.jsp").forward(request, response);
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("查询我的出租信息失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    private void cancel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        CarOrderService service = new CarOrderService();

        try {
            service.cancel(id);
            response.sendRedirect("CarOrderServlet?method=findMine");
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("取消出租信息失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    private void checkCompleted(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        CarOrderService service = new CarOrderService();

        try {
            service.checkCompleted(id);
            response.sendRedirect("CarOrderServlet?method=findMine");
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("确认车辆订单失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    private void applyOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        CarOrderService service = new CarOrderService();
        User user = (User) request.getSession().getAttribute("user");

        try {
            service.applyOrder(id, user.getId());
            response.sendRedirect("CarOrderServlet?method=findAll");
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("求租车辆失败，返回<a href='index.jsp'>首页</a>");
        }

    }

    private void makeChecked(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        CarOrderService service = new CarOrderService();
        try {
            service.makeChecked(id);
            response.sendRedirect("CarOrderServlet?method=findAll");
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("审核信息失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    private void createRentingMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CarOrder order = new CarOrder();
        User user = (User) request.getSession().getAttribute("user");
        String licensePlate = request.getParameter("licensePlate");

        order.setId(UUID.randomUUID().toString());
        order.setLicense_plate(licensePlate);
        order.setOwner_id(user.getId());
        order.setTenant_id(1);
        order.setState("待审核");
        order.setUpdate_time(new Date());

        CarOrderService service = new CarOrderService();
        try {
            service.createRentingMsg(order);
            response.sendRedirect("CarOrderServlet?method=findMine");
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("发布出租信息失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    private void findAll(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        CarOrderService service = new CarOrderService();
        User user = (User) request.getSession().getAttribute("user");

        try {
            List<CarOrder> list = service.findAll(user);
            request.setAttribute("list", list);
            request.getRequestDispatcher("showCarOrder.jsp").forward(request, response);
        } catch (SQLException e) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("查询订单信息失败，返回<a href='index.jsp'>首页</a>");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
