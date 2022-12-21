import service.CheckPrinter;
import util.InputParser;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CheckGeneratorRunner {
    static final String[] TEST_ARGS ={"3-1","2-10","5-1","4-10","card-235"};

    private static final SimpleDateFormat CHECK_NAME_BY_DATE = new SimpleDateFormat("HH-mm-ss__dd-MM");

    public static void main(String[] args) throws SQLException, IOException {

        if (args.length==0) args = TEST_ARGS;
        Date date = new Date();

        CheckPrinter checkPrinter = new CheckPrinter("H123h42");

        String checkName = "check_"+CHECK_NAME_BY_DATE.format(date);

        checkPrinter.GenerateCheck(
                InputParser.getIdsAndAmount(args),
                InputParser.getCardNumber(args),
                checkName
        );
    }
}
