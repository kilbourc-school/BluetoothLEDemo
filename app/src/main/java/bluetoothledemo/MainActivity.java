package bluetoothledemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_ACCESS_COURSE_LOCATION = 1;
    private static SQLiteDatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new SQLiteDatabaseHandler(getApplicationContext());
        //db.onUpgrade(db.getDB(),1,3);
    }
    public static SQLiteDatabaseHandler getDatabase(){
        return db;
    }
}
