package aueb.hci.humancomputerinteraction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramListAdapter extends BaseAdapter {

    private Context context;
    private List<Program> dataList, copyOfData;
    private LayoutInflater inflater;

    public Program selectedProgram;

    private View previousView;

    public ProgramListAdapter(Context context) {
        this.context = context;
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
            view = layoutInflater.inflate(R.layout.cloth_list_adapter, null);
        }

        ((ConstraintLayout)view.findViewById(R.id.clothListLayout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(previousView==view){
                    view.setBackgroundColor(Color.WHITE);
                    selectedProgram = null;
                    previousView = null;
                }else {
                    selectedProgram = prog;
                    if (previousView != null) previousView.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.GRAY);
                    previousView = view;
                }
                HomeScreen.selectedProgram = selectedProgram;
            }
        });

        ((TextView) view.findViewById(R.id.tvClothListFavName)).setText(prog.getName());

        ((ImageView)view.findViewById(R.id.ivClothListXsign)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Are you sure you want to unfavorite " + prog.getName()+" ?");

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                prog.setFavorited(false);
                                dataList.remove(prog);
                                notifyDataSetChanged();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        return view;
    }

    public void loadData(List<Program> data){
        List<Program> dataFav = data.stream().filter(p -> p.isFavorited()).collect(Collectors.toList());
        this.dataList = dataFav;
        this.copyOfData = dataList.subList(0,dataList.size());
        notifyDataSetChanged();
    }

}
