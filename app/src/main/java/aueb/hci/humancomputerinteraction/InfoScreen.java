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
                htmlSource = "<p>In this screen you create an advanced program to start.</p>" +
                        "<br><p>You can choose the rpms, the temp and the time by rotating the knobs.</p>" +
                        "<br><p>You can set the selected program's modes using<br> the Prewash, ECO and Dryer toggle-buttons.</p>" +
                        "<br><p>You can add the newly created advanced program to your custom programs by clicking on the checkbox.</p>" +
                        "<br><p>If you choose to add it in your custom programs, you can name it, write its description,favorite it and select an icon for it.</p>" +
                        "<br><p>Later you can hit Save and Start, to save it and start it.</p>" +
                        "<br><p>You can always cancel by clicking the CANCEL button.</p>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    info.setText(Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    info.setText(Html.fromHtml(htmlSource));
                }
                break;
            case 2:
                setTitle("Manage Programs Info");
                htmlSource = "<p>In this screen you can manage all of your programs.</p>" +
                        "<br><p>You can delete a program by clicking the X button.</p>" +
                        "<br><p>You can press the heart icon to favorite and unfavorite.</p>" +
                        "<br><p>You can view it's details by pressing the info button on the program's image.</p>" +
                        "<br><p>By clicking on a program it will turn grey and then you can edit it by clicking the edit button.</p>" +
                        "<br><p>You can always go back by pressing the button Go Back.</p>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    info.setText(Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    info.setText(Html.fromHtml(htmlSource));
                }
                break;
            case 3:
                setTitle("Edit Program Info");
                htmlSource = "<p>In this screen you can edit the program you just selected.</p>" +
                        "<br><p>You can choose the rpms, the temp and the time by rotating the knobs.</p>" +
                        "<br><p>You can set the selected program's modes using<br> the Prewash, ECO and Dryer toggle-buttons.</p>" +
                        "<br><p>You can name it, write its description,favorite it or select an icon for it.</p>" +
                        "<br><p>Later you can hit Save,to save it's new configuration.</p>" +
                        "<br><p>You can always cancel by clicking the CANCEL button.</p>";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    info.setText(Html.fromHtml(htmlSource, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    info.setText(Html.fromHtml(htmlSource));
                }
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
