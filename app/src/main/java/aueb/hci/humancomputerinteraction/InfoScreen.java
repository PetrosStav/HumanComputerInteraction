package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
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
                String htmlSource = "<p>In this screen you select the washing program you wish to start.</p>" +
                        "<br><p>You can press the heart icon to favorite and unfavorite.</p>" +
                        "<br><p>You can press the info button on a program to see it's details.</p>" +
                        "<br><p>You can also override the selected program's modes using<br> the Prewash, ECO and Dryer toggle-buttons.</p>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    info.setText(Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    info.setText(Html.fromHtml(htmlSource));
                }
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
