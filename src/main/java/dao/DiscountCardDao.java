package dao;

import entity.DiscountCard;
import exception.DaoException;
import util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiscountCardDao {
    private static final DiscountCardDao INSTANCE = new DiscountCardDao();

    private DiscountCardDao(){}

    public static DiscountCardDao getInstance(){
        return INSTANCE;
    }
    private static final String DELETE_SQL = """
            DELETE FROM discount_cards
            WHERE discount_number = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO discount_cards (discount_number, discount) 
            VALUES (?,?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE discount_cards
            SET discount_number = ?,
                discount =?
            WHERE discount_number = ?
            """;
    private static final String FIND_ALL = """
            SELECT discount_number,discount
            FROM discount_cards
            """;
    private static final String FIND_BY_ID = FIND_ALL+"""
            WHERE discount_number = ?
            """;

    public List<DiscountCard> findAll(){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(FIND_ALL)) {
            List<DiscountCard> discountCards=new ArrayList<>();
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                discountCards.add(buildDiscountCard(resultSet));
            }
            return discountCards;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    public Optional<DiscountCard> findById(Long id){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1,id);

            var resultSet = preparedStatement.executeQuery();
            DiscountCard discountCard = null;
            if (resultSet.next()){
                discountCard = buildDiscountCard(resultSet);
                return Optional.of(discountCard);
            }

            return Optional.ofNullable(discountCard);
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

    private DiscountCard buildDiscountCard(ResultSet resultSet) throws SQLException {
        return new DiscountCard(
                resultSet.getLong("discount_number"),
                resultSet.getInt("discount")
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

    public Boolean update(DiscountCard discountCard){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setInt(1,discountCard.getDiscount());
            preparedStatement.setLong(2,discountCard.getDiscountNumber());

            return preparedStatement.executeUpdate()>0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public DiscountCard save (DiscountCard discountCard){
        try (var connection = ConnectionManager.get();
             var preparedStatement =connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setLong(1,discountCard.getDiscountNumber());
            preparedStatement.setInt(2,discountCard.getDiscount());
            preparedStatement.executeUpdate();
            return discountCard;
        }catch (SQLException e){
            throw new DaoException(e);
        }
    }

}
