package aueb.hci.humancomputerinteraction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class InfoProgram extends Activity {
    ProgramDAO programdao = new ProgramDAOmemory();
    static Program info_program = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_info_program);
        Button ok = findViewById(R.id.ok_info);
        TextView rpms = findViewById(R.id.rpms);
        TextView temps = findViewById(R.id.temps);
        TextView times = findViewById(R.id.times);
        TextView eco_bool = findViewById(R.id.eco_bool);
        TextView prewash_bool = findViewById(R.id.prewash_bool);
        TextView dryer_bool = findViewById(R.id.dryer_bool);
        TextView description = findViewById(R.id.description);
        ImageView program_image = findViewById(R.id.program_image);

        if (info_program != null){
            setTitle(info_program.getName());
            rpms.setText(Integer.toString(info_program.getRPM()));
            temps.setText(Integer.toString(info_program.getTEMP()));
            times.setText(Integer.toString(info_program.getTIME()));
            eco_bool.setText((info_program.isEco()?" Yes":" No"));
            prewash_bool.setText((info_program.isPrewash()?" Yes":" No"));
            dryer_bool.setText((info_program.isDryer()?" Yes":" No"));
            description.setText(" " + info_program.getDescription() + " ");
            if(info_program.getImage()==null) {
                program_image.setImageResource(R.drawable.shirt_cartoon_deault);
            }else{
                program_image.setImageBitmap(info_program.getImage());
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
