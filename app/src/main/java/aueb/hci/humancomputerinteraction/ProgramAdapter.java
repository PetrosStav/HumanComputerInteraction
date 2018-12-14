package aueb.hci.humancomputerinteraction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import aueb.hci.humancomputerinteraction.DAO.ProgramDAO;

public class ProgramAdapter extends BaseAdapter {

    private Context context;
    private List<Program> dataList, copyOfData;
    private LayoutInflater inflater;
    private Boolean what_activity;

    public Program selectedProgram;

    private View previousView;

    private ProgramDAO programdao = new ProgramDAOmemory();

    public ProgramAdapter(Context context) {
        this.context = context;
        this.what_activity = false;
        this.dataList = new ArrayList<>();
        this.copyOfData = new ArrayList<>();
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

    public List<Program> getCopyOfData(){ return this.copyOfData; }

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

        if(!what_activity){

            ((ImageView)view.findViewById(R.id.ivClothImgProgram)).setImageResource(R.drawable.shirt_cartoon_deault); // TODO: replace default image with image path of program

            ((TextView) view.findViewById(R.id.tvClothImgName)).setText(prog.getName());

            if(prog.isFavorited()){
                ((ImageView)view.findViewById(R.id.ClothImgHeart)).setImageResource(R.drawable.filled_heart);
            }else{
                ((ImageView)view.findViewById(R.id.ClothImgHeart)).setImageResource(R.drawable.heartico);
            }

            ((ImageView)view.findViewById(R.id.ClothImgInfo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), prog.getDescription(), Toast.LENGTH_LONG).show();
                }
            });

            ((ImageView)view.findViewById(R.id.ClothImgHeart)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prog.setFavorited(!prog.isFavorited());
                    if(prog.isFavorited()){
                        ((ImageView)view.findViewById(R.id.ClothImgHeart)).setImageResource(R.drawable.filled_heart);
                    }else{
                        ((ImageView)view.findViewById(R.id.ClothImgHeart)).setImageResource(R.drawable.heartico);
                    }
                    Toast.makeText(view.getContext(), prog.getName()+ (prog.isFavorited()?" Favorited!":" Unfavorited!"), Toast.LENGTH_LONG).show();
                }
            });

            view.findViewById(R.id.ClothImgView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedProgram = prog;
                    if(previousView!=null) previousView.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.GRAY);
                    previousView = view;

                }
            });

        }else{

            ((ImageView)view.findViewById(R.id.ivClothImgMngProgram)).setImageResource(R.drawable.shirt_cartoon_deault); // TODO: replace default image with image path of program

            ((TextView) view.findViewById(R.id.tvClothImgMngName)).setText(prog.getName());

            if(prog.isFavorited()){
                ((ImageView)view.findViewById(R.id.ClothImgMngHeart)).setImageResource(R.drawable.filled_heart);
            }else{
                ((ImageView)view.findViewById(R.id.ClothImgMngHeart)).setImageResource(R.drawable.heartico);
            }

            ((ImageView)view.findViewById(R.id.ClothImgMngInfo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), prog.getDescription(), Toast.LENGTH_LONG).show();
                }
            });

            ((ImageView)view.findViewById(R.id.ClothImgMngHeart)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prog.setFavorited(!prog.isFavorited());
                    if(prog.isFavorited()){
                        ((ImageView)view.findViewById(R.id.ClothImgMngHeart)).setImageResource(R.drawable.filled_heart);
                    }else{
                        ((ImageView)view.findViewById(R.id.ClothImgMngHeart)).setImageResource(R.drawable.heartico);
                    }
                    Toast.makeText(view.getContext(), prog.getName()+ (prog.isFavorited()?" Favorited!":" Unfavorited!"), Toast.LENGTH_LONG).show();
                }
            });

            ((ImageView)view.findViewById(R.id.ClothImgMngDelete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                    alert.setCancelable(true);
                    DialogInterface.OnClickListener Positivelistener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dataList.remove(i);
                            programdao.delete(prog);
                            notifyDataSetChanged();
                        }
                    };

                    alert.setMessage("Are you sure you want to delete program: " + prog.getName() + " ?");
                    alert.setPositiveButton(R.string.yes, Positivelistener);
                    alert.setNegativeButton(R.string.no, null);
                    alert.create().show();
                }
            });

            view.findViewById(R.id.ClothImgView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedProgram = prog;
                    if(previousView!=null) previousView.setBackgroundColor(Color.WHITE);
                    view.setBackgroundColor(Color.GRAY);
                    previousView = view;
                }
            });

        }

        return view;
    }

    public void loadData(List<Program> data, boolean what_activity){
        this.dataList = data;
        this.what_activity = what_activity;
        this.copyOfData = new ArrayList<>(dataList);
        notifyDataSetChanged();
    }
}
