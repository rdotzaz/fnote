package rdotzaz.footnote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import rdotzaz.footnote.MainActivity;
import rdotzaz.footnote.R;
import rdotzaz.footnote.database.Team;

public class TeamAdapter extends RecyclerView.Adapter<MyVH>
{
    ArrayList<Team> teams;
    MainActivity activity;

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public TeamAdapter(MainActivity activity, ArrayList<Team> teams)
    {
        this.teams=teams;
        this.activity=activity;
    }

    @NonNull
    @Override
    public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.team_card,parent,false);
        return new MyVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyVH holder, int position) {

        final TextView name = holder.itemView.findViewById(R.id.name_teamcard);
        final TextView coach = holder.itemView.findViewById(R.id.coach_teamcard);
        final CardView cardView = holder.itemView.findViewById(R.id.teamcard_layout);

        final Button edit = holder.itemView.findViewById(R.id.edit_teamcard);
        final Button delete = holder.itemView.findViewById(R.id.delete_teamcard);

        cardView.setBackgroundResource(R.drawable.team_back);

        name.setText(teams.get(holder.getAdapterPosition()).getName());
        coach.setText("Coach: " + teams.get(holder.getAdapterPosition()).getCoach());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openTeam(teams.get(holder.getAdapterPosition()).getId());
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showDialogT(holder.getAdapterPosition());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.deleteTeam(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }
}
