package rdotzaz.footnote.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rdotzaz.footnote.R;
import rdotzaz.footnote.TeamActivity;
import rdotzaz.footnote.database.Player;

public class PlayerAdapter extends RecyclerView.Adapter<MyVH>
{

    ArrayList<Player> players;
    TeamActivity activity;
    Integer field;

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public PlayerAdapter(TeamActivity activity, ArrayList<Player> players, Integer field)
    {
        this.activity=activity;
        this.field=field;
        this.players=getByFields(players);
    }

    private ArrayList<Player> getByFields(ArrayList<Player> players)
    {
        ArrayList<Player> tmp = new ArrayList<>();
        for(Player p : players)
        {
            if(p.getField().equals(field)) tmp.add(p);
        }
        return tmp;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.player_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH holder, int position)
    {
        final TextView num = holder.itemView.findViewById(R.id.num_card);
        final TextView name = holder.itemView.findViewById(R.id.name_card);
        final TextView ovr = holder.itemView.findViewById(R.id.ovr_card);
        final TextView age = holder.itemView.findViewById(R.id.age_card);
        final CardView cardView = holder.itemView.findViewById(R.id.card_layout);

        /*
        switch (field)
        {
            case 1:
                cardView.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorDfDarkest));
                break;

            case 2:
                cardView.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorGreenDarkest));
                break;

            case 3:
                cardView.setBackgroundColor(ContextCompat.getColor(activity,R.color.colorStDarkest));
                break;
        }

         */


        num.setText(players.get(holder.getAdapterPosition()).getNumber().toString());
        name.setText(players.get(holder.getAdapterPosition()).getName());
        ovr.setText(players.get(holder.getAdapterPosition()).getOvr().toString());
        age.setText("-" + players.get(holder.getAdapterPosition()).getAge().toString() + "-");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.showDialogP(players.get(holder.getAdapterPosition()),holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
