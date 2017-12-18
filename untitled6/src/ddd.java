import domain.ParkingSpaceOrder;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import utils.DataSourceUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ddd {
    public static void main(String[] args) throws SQLException {
//        insertUsers();
//        insertParkingSpace();
//        insertParkingSpaceOrders();
//        selectSpaceOrders();
//        insertDemands();
        insertCarOrders();
    }

    private static void insertCarOrders() throws SQLException {
        String sql = "insert into car_order value (?,?,?,?,?,?,?,?)";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Object[][] objs = new Object[20][8];
        for (int i = 0; i < 20; i++) {
            String uuid = UUID.randomUUID().toString();
            objs[i][0] = uuid;
            objs[i][1] = "license" + (i + 50);
            objs[i][2] = 1;
            objs[i][3] = 21;
            objs[i][4] = 50;
            objs[i][5] = 10;
            objs[i][6] = "待付款";
            objs[i][7] = new Date();
            for (Object obj : objs[i]) {
                System.out.print(obj + " ");
            }
            System.out.println();
        }
        runner.batch(sql, objs);
    }

    private static void insertDemands() throws SQLException {
        String sql = "insert into demand_list value (?,?,?,?,?)";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Object[][] objs = new Object[10][5];
        for (int i = 0; i < 10; i++) {
            String str = UUID.randomUUID().toString().replace("-", "");
            System.out.println(str);
            objs[i][0] = str;
            objs[i][1] = (i + 1);
            objs[i][2] = "My description is " + UUID.randomUUID().toString();
            objs[i][3] = "待审核";
            objs[i][4] = new Date();
        }
        runner.batch(sql, objs);
    }

    public static void insertUsers() throws SQLException {
        String sql = "insert into user value(?,?,?,?,?)";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Object[][] objs = new Object[20][5];
        for (int i = 0; i < 20; i++) {
            objs[i][0] = (i + 1);
            objs[i][1] = "name" + (i + 1);
            objs[i][2] = "pswd" + (i + 1);
            objs[i][3] = "user";
            objs[i][4] = "license" + (i + 1);
        }
        runner.batch(sql, objs);
    }

    public static void insertParkingSpace() throws SQLException {
        String sql = "insert into parking_space value(?,?,?,?)";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Object[][] objs = new Object[20][4];
        for (int i = 0; i < 20; i++) {
            objs[i][0] = (i + 1);
            objs[i][1] = "using";
            objs[i][2] = "zone" + (i + 1);
            objs[i][3] = new Date();
        }
        runner.batch(sql, objs);
    }

    public static void insertParkingSpaceOrders() throws SQLException {
        Random random = new Random();
        String sql = "insert into parking_space_order value(?,?,?,?,?,?,?,?)";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        Object[][] objs = new Object[10][8];
        for (int i = 0; i < 10; i++) {
            objs[i][0] = UUID.randomUUID().toString().replace("-", "");
            objs[i][1] = (i + 1);
            objs[i][2] = "plate" + (i + 21);
            objs[i][3] = (i + 1);
            objs[i][4] = "待付款";
            objs[i][5] = new Date();
            int mon = random.nextInt(6);
            int day = random.nextInt(31);
            objs[i][6] = mon + "个月" + day + "天";
            objs[i][7] = (mon * 1000 + day * 60);
        }
        runner.batch(sql, objs);
    }

    public static void selectSpaceOrders() throws SQLException {
        String sql = "select parking_space_order.*,name from parking_space_order,user where parking_space_order.user_id=user.id";
        QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
        List<ParkingSpaceOrder> list = runner.query(sql, new BeanListHandler<ParkingSpaceOrder>(ParkingSpaceOrder.class));
        for (ParkingSpaceOrder p : list) {
            System.out.print(p.getId());
            System.out.print(p.getSpace_id());
            System.out.print(p.getLicense_plate());
            System.out.print(p.getName());
            System.out.print(p.getState());
            System.out.print(p.getUpdate_time());
            System.out.print(p.getDuration());
            System.out.print(p.getPrice_in_all());
            System.out.println();
        }
    }

}
