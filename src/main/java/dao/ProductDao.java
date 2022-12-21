package dao;

import entity.Product;
import exception.DaoException;
import util.ConnectionManager;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDao {
    private static final ProductDao INSTANCE = new ProductDao();
    private static final String DELETE_SQL = """
            DELETE FROM products
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO products (discount_number, product_name, price,is_opt) 
            VALUES (?,?,?,?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE products
            SET discount_number = ?,
                product_name =?,
                price = ?,
                is_opt =?
            WHERE id = ?
            """;
    private static final String FIND_ALL = """
            SELECT id,discount_number,product_name,price,is_opt
            FROM products
            """;
    private static final String FIND_BY_ID = FIND_ALL+"""
            WHERE id = ?
            """;

    private ProductDao(){}

    public static ProductDao getInstance(){
        return INSTANCE;
    }

    public List<Product> findAll(){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(FIND_ALL)) {
            List<Product> products=new ArrayList<>();
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                products.add(buildProduct(resultSet));
            }
            return products;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Optional<Product> findById(Long id){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1,id);

            var resultSet = preparedStatement.executeQuery();
            Product product = null;
            if (resultSet.next()){
                product = buildProduct(resultSet);
                return Optional.of(product);
            }

            return Optional.ofNullable(product);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private Product buildProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getLong("id"),
                resultSet.getInt("discount_number"),
                resultSet.getString("product_name"),
                resultSet.getDouble("price"),
                resultSet.getBoolean("is_opt")
        );
    }

    public boolean delete(Long id){
        try (var connection = ConnectionManager.get();
            var preparedStatement =connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1,id);
            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Boolean update(Product product){
        try (var connection = ConnectionManager.get();
            var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setObject(1,product.getDiscountNumber());
            preparedStatement.setString(2,product.getProductName());
            preparedStatement.setDouble(3,product.getPrice());
            preparedStatement.setBoolean(4,product.getOpt());
            preparedStatement.setLong(5,product.getId());

            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public Product save (Product product){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1,product.getDiscountNumber());
            preparedStatement.setString(2,product.getProductName());
            preparedStatement.setDouble(3,product.getPrice());
            preparedStatement.setBoolean(4,product.getOpt());
            preparedStatement.executeUpdate();

            var  generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()){
                product.setId(generatedKeys.getLong("id"));
            }
            return product;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

}
