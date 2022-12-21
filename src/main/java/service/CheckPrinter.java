package service;

import dao.DiscountCardDao;
import dao.ProductDao;
import entity.DiscountCard;
import entity.Product;
import util.ConnectionManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Map;
import java.util.Optional;

public class CheckPrinter {

    private static final Double VAT_VALUE = 0.17;
    private static final Double OPT_DISCOUNT = 0.1;
    private String cashierNumber;

    public CheckPrinter(String cashierNumber){
        this.cashierNumber= cashierNumber;
    }

    private static final String HEADER = """
                    CASH RECEIPT
                Supermarket Galactic
     Belarus, Minsk, Bogdanovicha street, 29
                Tel: +375299452781
     Cashier:{CashierNumber}\t\t\tDate: {Date}
                            \tTime: {Time}    
     -----------------------------------------------------
            """+ new Formatter().format("%3s %15s %15s %15s", "QTY", "Description","Price","Total")+"\n";

    private static final String FOOTER = """
     =====================================================
     TAXABLE TOT.\t\t\t\t\t\t\t\t{TAX}
     VAT17%\t\t\t\t\t\t\t\t\t\t{VAT}
     TOTAL:\t\t\t\t\t\t\t\t\t\t{TotalPrice}
           """;


    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat TIME_FORMAT =new SimpleDateFormat("HH:mm:ss");

    public void GenerateCheck(Map<Integer,Integer> productsIds, Integer discountCardId,String checkName) throws IOException, SQLException {
        File file = Path.of("checks",checkName+".txt").toFile();
        ProductDao productDao = ProductDao.getInstance();
        DiscountCardDao discountCardDao = DiscountCardDao.getInstance();

        try (var connection = ConnectionManager.get();
             var writer = new BufferedWriter(new FileWriter(file))) {
            Optional<DiscountCard> discountCard = discountCardDao.findById(discountCardId.longValue());
            Date date = new Date();
            //Add header to check
            writer.append(
                    HEADER.replace("{Date}",DATE_FORMAT.format(date))
                            .replace("{Time}",TIME_FORMAT.format(date))
                            .replace("{CashierNumber}",this.cashierNumber)
            );

            Double totalPrice = Double.valueOf(0);
            //Generate body of check
            for (Integer prodId : productsIds.keySet()){
                Product product = productDao.findById(prodId.longValue()).get();

                Integer QTY = productsIds.get(prodId);
                Double total = QTY*product.getPrice();


                if (product.getDiscountNumber() == discountCard.get().getDiscountNumber().intValue()){
                    total-=total*discountCard.get().getDiscount()/100;
                }
                if (QTY>=10 && product.getOpt()){
                    total-=total*OPT_DISCOUNT;
                }

                Formatter formatter = new Formatter();
                totalPrice+=total;
                writer.append(formatter.format("%3s %15s %15s %15s", QTY, product.getProductName(),"$"+product.getPrice(), "$"+total).toString());
                writer.newLine();
            }

            //Add footer to check
            String tax = String.format("%.2f", totalPrice-(totalPrice*VAT_VALUE));
            String vat = String.format("%.2f", totalPrice*VAT_VALUE);
            writer.append(
                    FOOTER.replace("{TotalPrice}","$"+totalPrice)
                            .replace("{VAT}","$"+vat)
                            .replace("{TAX}","$"+tax)
            );
        }

    }
}
