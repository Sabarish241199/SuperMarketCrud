package com.api.supermarketcrud.jdbcImpl;

import com.api.supermarketcrud.Exceptions.ResourceNotFoundException;
import com.api.supermarketcrud.model.ProductCost;
import com.api.supermarketcrud.model.UpdateRq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUtils {

    @Value("${spring.datasource.url}")
    public String url;
    private static String staticUrl;

    @PostConstruct
    public void inituri() {
        staticUrl = this.url;
    }

    public static String getUrl() {
        return staticUrl;
    }
    @Value("${spring.datasource.username}")
    public String userName;
    private static String staticUserName;

    @PostConstruct
    public void inituser() {
        staticUserName = this.userName;
    }

    public static String getStaticUserName() {
        return staticUserName;
    }
    @Value("${spring.datasource.password}")
    public String password;
    private static String staticPassword;

    @PostConstruct
    public void initpassword() {
        staticPassword = this.password;
    }

    public static String getStaticPassword() {
        return staticPassword;}


    public List<ProductCost> getProductCost(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
            List<ProductCost> productCostList = new ArrayList<ProductCost>();
                try{
                connection = DriverManager.getConnection(getUrl(), getStaticUserName(),getStaticPassword() );
                String sql = "select * from product_cost";
                preparedStatement= connection.prepareStatement(sql);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    String Product = resultSet.getString("Product");
                    int id = resultSet.getInt("id");
                    double Cost = resultSet.getDouble("Cost");
                    String batch_No = resultSet.getString("Batch_No");
                    ProductCost productCost = new ProductCost(id,batch_No,Product, Cost);
                    productCostList.add(productCost);
                }
            }catch(SQLException e){
                    e.printStackTrace();
            }finally {
                    try{
                        if(resultSet != null)
                            resultSet.close();
                        if(preparedStatement != null)
                            preparedStatement.close();
                        if(connection != null)
                            connection.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                }
                return productCostList;
        }

    public ProductCost getSingleProductCostElement(String Batch_no, String product){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(getUrl(), getStaticUserName(),getStaticPassword() );
            String sql = "select * from product_cost where Batch_No='" + Batch_no + "' and Product='" + product+"'";
            preparedStatement= connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String Product = resultSet.getString("Product");
                Double Cost = resultSet.getDouble("Cost");
                int id = resultSet.getInt("id");
                String batch_No = resultSet.getString("Batch_No");
                return new ProductCost(id,batch_No,Product,Cost);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if(resultSet != null)
                    resultSet.close();
                if(preparedStatement != null)
                    preparedStatement.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return new ProductCost();
    }

    public String updateBatchCount(UpdateRq updateRq) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection= DriverManager.getConnection(getUrl(), getStaticUserName(), getStaticPassword());
            String sql = "SET SQL_SAFE_UPDATES = 0";
            preparedStatement = connection.prepareStatement(sql);
            resultSet= preparedStatement.executeQuery();
            sql = "UPDATE batch" + " SET  Count = Count - " +updateRq.getCount()+  " WHERE Batch_No='" + updateRq.getBatch_No() + "' and Product='" + updateRq.getProduct()+"'" ;
            preparedStatement = connection.prepareStatement(sql);
            resultSet= preparedStatement.executeQuery();
            sql="commit";
            preparedStatement = connection.prepareStatement(sql);
            resultSet= preparedStatement.executeQuery();
            return "Count is updated";
        }catch ( SQLException e){
            throw new ResourceNotFoundException(updateRq.getBatch_No() + " of product " + updateRq.getProduct() + " is Not found in Database or there is issue connecting to DB");
        }finally {
            try{
                if(resultSet != null)
                    resultSet.close();
                if(preparedStatement != null)
                    preparedStatement.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }
}
