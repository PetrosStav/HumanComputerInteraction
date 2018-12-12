package aueb.hci.humancomputerinteraction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdapter extends BaseAdapter {

    private Context context;
    private List<Program> dataList, copyOfData;
    private LayoutInflater inflater;
    private Boolean what_activity;

    public ProgramAdapter(Context context) {
        this.context = context;
        what_activity = false;
        dataList = new ArrayList<>();
        copyOfData = new ArrayList<>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Program prog = dataList.get(i);
        if(view == null){
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            if (what_activity){
                view = layoutInflater.inflate(R.layout.cloth_image_adapter_manage_programs, null);
            }else{
                view = layoutInflater.inflate(R.layout.cloth_image_adapter, null);
            }

        }

        ((ImageView)view.findViewById(R.id.ivProgram)).setImageResource(R.drawable.shirt_cartoon_deault); // TODO: replace default image with image path of program

        ((TextView) view.findViewById(R.id.tvName)).setText(prog.getName());

        if(prog.isFavorited()){
            ((ImageView)view.findViewById(R.id.heart_custom)).setImageResource(R.drawable.filled_heart);
        }else{
            ((ImageView)view.findViewById(R.id.heart_custom)).setImageResource(R.drawable.heartico);
        }

        ((ImageView)view.findViewById(R.id.info_custom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), prog.getDescription(), Toast.LENGTH_LONG).show();
            }
        });

        ((ImageView)view.findViewById(R.id.heart_custom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prog.setFavorited(!prog.isFavorited());
                if(prog.isFavorited()){
                    ((ImageView)view.findViewById(R.id.heart_custom)).setImageResource(R.drawable.filled_heart);
                }else{
                    ((ImageView)view.findViewById(R.id.heart_custom)).setImageResource(R.drawable.heartico);
                }
                Toast.makeText(view.getContext(), prog.getName()+ (prog.isFavorited()?" Favorited!":" Unfavorited!"), Toast.LENGTH_LONG).show();
            }
        });

        if (what_activity){
            ((ImageView)view.findViewById(R.id.delete_program)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setCancelable(true);
                    DialogInterface.OnClickListener Positivelistener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dataList.remove(i);
                            notifyDataSetChanged();
                        }
                    };

                    alert.setMessage("Are you sure you want to delete program: " + prog.getName() + " ?");
                    alert.setPositiveButton(R.string.yes, Positivelistener);
                    alert.setNegativeButton(R.string.no, null);
                    alert.create().show();
                }
            });


        }

        return view;
    }

    public void loadData(List<Program> data, boolean what_activity){
        this.dataList = data;
        this.what_activity = what_activity;
        this.copyOfData = dataList.subList(0,dataList.size());
        notifyDataSetChanged();
    }
}
