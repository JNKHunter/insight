/**
 * Created by jhunter on 4/5/17.
 */
public class Client {

    public static void main(String[] args) {



        Hosts hosts = new Hosts("insight_testsuite/tests/test_features/log_input/log.txt", 10);
        Resources resources = new Resources("insight_testsuite/tests/test_features/log_input/log.txt", 10);
        Hours hours = new Hours("insight_testsuite/tests/test_features/log_input/log.txt", 10);
        Blocked blocked = new Blocked("insight_testsuite/tests/test_features/log_input/log.txt");
    }

}
