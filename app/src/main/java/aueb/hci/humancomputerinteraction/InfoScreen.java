package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InfoScreen extends Activity {

    static int textId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_screen);

        TextView info = findViewById(R.id.info_screen_text);
        Button ok = findViewById(R.id.info_screen_ok);

        switch(textId){
            case 0:
                setTitle("Select Program Info");
                info.setText("In this screen you can select a program to start.");
                break;
            case 1:
                setTitle("Advanced Program Info");
                info.setText("In this screen you can start and/or create an advanced program.");
                break;
            case 2:
                setTitle("Manage Programs Info");
                info.setText("In this screen you can manage your programs.");
                break;
            case 3:
                setTitle("Edit Program Info");
                info.setText("In this screen you can edit your program.");
                break;
            default:
                break;
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
