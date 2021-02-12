package rdotzaz.footnote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rdotzaz.footnote.R;
import rdotzaz.footnote.SquadActivity;
import rdotzaz.footnote.database.Player;

public class PlayerSquadAdapter extends RecyclerView.Adapter<MyVH>
{
    ArrayList<Player> players;
    SquadActivity activity;

    public PlayerSquadAdapter(SquadActivity activity, ArrayList<Player> players)
    {
        this.activity=activity;
        this.players=players;
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player1.getNumber().compareTo(player2.getNumber());
            }
        });
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.player_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH holder, int position) {

        final TextView num = holder.itemView.findViewById(R.id.num_card);
        final TextView name = holder.itemView.findViewById(R.id.name_card);
        final TextView ovr = holder.itemView.findViewById(R.id.ovr_card);
        final TextView age = holder.itemView.findViewById(R.id.age_card);
        final CardView cardView = holder.itemView.findViewById(R.id.card_layout);

        cardView.setBackgroundColor(ContextCompat.getColor(activity,R.color.grayDark));

        num.setText(players.get(holder.getAdapterPosition()).getNumber().toString());
        name.setText(players.get(holder.getAdapterPosition()).getName());
        ovr.setText(players.get(holder.getAdapterPosition()).getOvr().toString());
        age.setText("-" + players.get(holder.getAdapterPosition()).getAge().toString() + "-");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickPlayerCard(players.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
